package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ZeptoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ZeptoDatabase.getDatabase(application)
    private val repository = ZeptoRepository(
        database.productDao(),
        database.cartDao(),
        database.browsingHistoryDao(),
        database.orderDao(),
        database.addressDao()
    )
    private val firestoreService = FirestoreService()
    private val messagingService = VyraMessagingService(application)

    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItemEntity>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrderItems: StateFlow<List<OrderItemEntity>> = repository.allOrderItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val addresses: StateFlow<List<AddressEntity>> = repository.addresses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _recentBrowsedIds = MutableStateFlow<List<Long>>(emptyList())
    val recentBrowsedIds: StateFlow<List<Long>> = _recentBrowsedIds.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _activeOrderState = MutableStateFlow<OrderState?>(null)
    val activeOrderState: StateFlow<OrderState?> = _activeOrderState.asStateFlow()

    // Loyalty Points (VYRA Coins)
    private val _loyaltyCoins = MutableStateFlow(120) // Starting bonus coins
    val loyaltyCoins: StateFlow<Int> = _loyaltyCoins.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialData()
            repository.getRecentBrowsedProductIds().collect { ids ->
                _recentBrowsedIds.value = ids
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun recordBrowsing(productId: Long) {
        viewModelScope.launch {
            repository.recordBrowsing(productId)
        }
    }

    fun updateCart(productId: Long, qty: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(productId, qty)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    val recommendedProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        _recentBrowsedIds,
        allOrderItems
    ) { products, browsedIds, orderItems ->
        if (products.isEmpty()) return@combine emptyList()

        val scored = products.map { product ->
            var score = 0
            if (browsedIds.contains(product.id)) {
                score += 10
            }
            if (orderItems.any { it.productId == product.id }) {
                score += 15
            }
            if (product.isBestseller) {
                score += 5
            }
            product to score
        }

        scored.sortedByDescending { it.second }.map { it.first }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val previouslyPurchasedItems: StateFlow<List<OrderItemEntity>> = allOrderItems
        .map { items -> items.distinctBy { it.productId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun placeOrder(totalAmount: Double, address: String, cartMap: Map<ProductEntity, Int>, redeemedCoins: Int, onComplete: (Long) -> Unit) {
        viewModelScope.launch {
            val orderId = repository.placeOrder(totalAmount, address, cartMap.toList())
            
            // Deduct redeemed coins and earn new coins (10% of total)
            val earnedCoins = (totalAmount / 10).toInt()
            _loyaltyCoins.value = (_loyaltyCoins.value - redeemedCoins).coerceAtLeast(0) + earnedCoins

            firestoreService.syncOrderToCloud(orderId, totalAmount, address)
            messagingService.showOrderNotification("Order Confirmed ⚡", "Your order #$orderId is placed! Earned $earnedCoins VYRA Coins.")

            viewModelScope.launch {
                firestoreService.listenToOrderRealtime(orderId).collect { cloudState ->
                    if (cloudState != null) {
                        val oldStatus = _activeOrderState.value?.status
                        if (oldStatus != cloudState.status) {
                            messagingService.showOrderNotification("VYRA Order Update ⚡", "Status: ${cloudState.status}")
                        }
                        _activeOrderState.value = OrderState(
                            orderId = cloudState.orderId,
                            status = cloudState.status,
                            secondsRemaining = cloudState.estimatedMinutes * 60,
                            partnerName = cloudState.partnerName,
                            partnerPhone = cloudState.partnerPhone,
                            partnerVehicle = cloudState.partnerVehicle
                        )
                    }
                }
            }

            onComplete(orderId)
        }
    }

    fun updateActiveOrderStatus(status: String, secondsRemaining: Int) {
        val current = _activeOrderState.value
        messagingService.showOrderNotification("VYRA Delivery Alert ⚡", status)
        if (current != null) {
            _activeOrderState.value = current.copy(status = status, secondsRemaining = secondsRemaining)
        } else {
            _activeOrderState.value = OrderState(
                orderId = 1L,
                status = status,
                secondsRemaining = secondsRemaining
            )
        }
    }

    fun clearActiveOrder() {
        _activeOrderState.value = null
    }
}

data class OrderState(
    val orderId: Long,
    val status: String,
    val secondsRemaining: Int,
    val partnerName: String = "Ramesh Kumar",
    val partnerPhone: String = "+91 98765 12345",
    val partnerVehicle: String = "Electric Scooter (EV-09)"
)
