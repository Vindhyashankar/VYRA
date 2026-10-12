package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ZeptoRepository(
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val browsingHistoryDao: BrowsingHistoryDao,
    private val orderDao: OrderDao,
    private val addressDao: AddressDao
) {
    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val cartItems: Flow<List<CartItemEntity>> = cartDao.getCartItems()
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    val allOrderItems: Flow<List<OrderItemEntity>> = orderDao.getAllOrderItems()
    val addresses: Flow<List<AddressEntity>> = addressDao.getAddresses()

    suspend fun seedInitialData() {
        val current = productDao.getAllProducts().first()
        if (current.isEmpty()) {
            val sampleProducts = listOf(
                ProductEntity(
                    title = "Fresh Organic Tomato",
                    category = "Fruits & Veggies",
                    price = 35.0,
                    mrp = 50.0,
                    unit = "500 g",
                    imageUrl = "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=500&q=80",
                    discountText = "30% OFF",
                    stock = 50,
                    rating = 4.6f,
                    isBestseller = true,
                    tags = "vegetable,organic,fresh,tomato"
                ),
                ProductEntity(
                    title = "Fresh Alphonso Mango",
                    category = "Fruits & Veggies",
                    price = 149.0,
                    mrp = 199.0,
                    unit = "1 kg",
                    imageUrl = "https://images.unsplash.com/photo-1553279768-865429fa0078?w=500&q=80",
                    discountText = "25% OFF",
                    stock = 30,
                    rating = 4.8f,
                    isBestseller = true,
                    tags = "fruit,mango,fresh"
                ),
                ProductEntity(
                    title = "Amul Fresh Toned Milk",
                    category = "Dairy & Breakfast",
                    price = 32.0,
                    mrp = 34.0,
                    unit = "500 ml",
                    imageUrl = "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=500&q=80",
                    discountText = "6% OFF",
                    stock = 100,
                    rating = 4.9f,
                    isBestseller = true,
                    tags = "dairy,milk,amul,breakfast"
                ),
                ProductEntity(
                    title = "Brown Bread - Whole Wheat",
                    category = "Dairy & Breakfast",
                    price = 45.0,
                    mrp = 55.0,
                    unit = "400 g",
                    imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=500&q=80",
                    discountText = "18% OFF",
                    stock = 40,
                    rating = 4.5f,
                    isBestseller = false,
                    tags = "bakery,bread,breakfast"
                ),
                ProductEntity(
                    title = "Lays Classic Salted Chips",
                    category = "Munchies & Snacks",
                    price = 20.0,
                    mrp = 20.0,
                    unit = "50 g",
                    imageUrl = "https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=500&q=80",
                    discountText = "10% OFF",
                    stock = 80,
                    rating = 4.7f,
                    isBestseller = true,
                    tags = "snacks,chips,lays,munchies"
                ),
                ProductEntity(
                    title = "Cadbury Dairy Milk Silk",
                    category = "Munchies & Snacks",
                    price = 175.0,
                    mrp = 190.0,
                    unit = "150 g",
                    imageUrl = "https://images.unsplash.com/photo-1549007994-cb92caebd54b?w=500&q=80",
                    discountText = "8% OFF",
                    stock = 60,
                    rating = 4.9f,
                    isBestseller = true,
                    tags = "chocolate,cadbury,snacks,sweet"
                ),
                ProductEntity(
                    title = "Coca-Cola Cold Drink",
                    category = "Beverages",
                    price = 40.0,
                    mrp = 45.0,
                    unit = "750 ml",
                    imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500&q=80",
                    discountText = "11% OFF",
                    stock = 70,
                    rating = 4.6f,
                    isBestseller = false,
                    tags = "beverage,cola,drink,cold"
                ),
                ProductEntity(
                    title = "Nescafe Classic Instant Coffee",
                    category = "Beverages",
                    price = 150.0,
                    mrp = 180.0,
                    unit = "50 g",
                    imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=500&q=80",
                    discountText = "16% OFF",
                    stock = 40,
                    rating = 4.7f,
                    isBestseller = true,
                    tags = "coffee,nescafe,beverage"
                ),
                ProductEntity(
                    title = "Maggi 2-Minute Noodles",
                    category = "Instant & Frozen",
                    price = 14.0,
                    mrp = 15.0,
                    unit = "70 g",
                    imageUrl = "https://images.unsplash.com/photo-1612927601601-6638404737ce?w=500&q=80",
                    discountText = "6% OFF",
                    stock = 200,
                    rating = 4.8f,
                    isBestseller = true,
                    tags = "instant,noodles,maggi"
                ),
                ProductEntity(
                    title = "Surf Excel Easy Wash Detergent",
                    category = "Household",
                    price = 115.0,
                    mrp = 130.0,
                    unit = "1 kg",
                    imageUrl = "https://images.unsplash.com/photo-1585421514284-efb74c2b69ba?w=500&q=80",
                    discountText = "11% OFF",
                    stock = 35,
                    rating = 4.5f,
                    isBestseller = false,
                    tags = "household,detergent,cleaning"
                ),
                ProductEntity(
                    title = "Colgate Strong Teeth Toothpaste",
                    category = "Personal Care",
                    price = 98.0,
                    mrp = 110.0,
                    unit = "200 g",
                    imageUrl = "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500&q=80",
                    discountText = "10% OFF",
                    stock = 45,
                    rating = 4.7f,
                    isBestseller = true,
                    tags = "personal care,toothpaste,colgate"
                ),
                ProductEntity(
                    title = "Fresh Farm Eggs (6 pcs)",
                    category = "Dairy & Breakfast",
                    price = 48.0,
                    mrp = 60.0,
                    unit = "6 pack",
                    imageUrl = "https://images.unsplash.com/photo-1516448702829-3c87ab50897f?w=500&q=80",
                    discountText = "20% OFF",
                    stock = 50,
                    rating = 4.6f,
                    isBestseller = true,
                    tags = "dairy,eggs,breakfast"
                )
            )
            productDao.insertAll(sampleProducts)
        }

        val addresses = addressDao.getAddresses().first()
        if (addresses.isEmpty()) {
            addressDao.insertAddress(
                AddressEntity(
                    title = "Home",
                    fullAddress = "A-402, Skyline Towers, 4th Main Road, Indiranagar",
                    isDefault = true
                )
            )
        }
    }

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> {
        return productDao.getProductsByCategory(category)
    }

    fun searchProducts(query: String): Flow<List<ProductEntity>> {
        return productDao.searchProducts(query)
    }

    suspend fun getProductById(id: Long): ProductEntity? {
        return productDao.getProductById(id)
    }

    suspend fun recordBrowsing(productId: Long) {
        browsingHistoryDao.insertBrowsingHistory(
            BrowsingHistoryEntity(productId = productId, timestamp = System.currentTimeMillis())
        )
    }

    fun getRecentBrowsedProductIds(): Flow<List<Long>> {
        return browsingHistoryDao.getRecentBrowsedProductIds()
    }

    suspend fun updateCartQuantity(productId: Long, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteCartItem(productId)
        } else {
            cartDao.insertCartItem(CartItemEntity(productId = productId, quantity = quantity))
        }
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    suspend fun placeOrder(totalAmount: Double, address: String, items: List<Pair<ProductEntity, Int>>): Long {
        val orderId = orderDao.insertOrder(
            OrderEntity(
                date = System.currentTimeMillis(),
                totalAmount = totalAmount,
                status = "Delivering in 10 mins ⚡",
                deliveryAddress = address
            )
        )
        val orderItems = items.map { (product, qty) ->
            OrderItemEntity(
                orderId = orderId,
                productId = product.id,
                title = product.title,
                price = product.price,
                quantity = qty,
                imageUrl = product.imageUrl
            )
        }
        orderDao.insertOrderItems(orderItems)
        cartDao.clearCart()
        return orderId
    }
}
