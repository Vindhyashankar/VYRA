package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartItemEntity
import com.example.data.ProductEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartItemEntity>,
    products: List<ProductEntity>,
    loyaltyCoins: Int,
    onQuantityChanged: (Long, Int) -> Unit,
    onClearCart: () -> Unit,
    onProceedToCheckout: (Double, Int) -> Unit
) {
    var couponCode by remember { mutableStateOf("") }
    var appliedDiscountAmount by remember { mutableStateOf(0.0) }
    var couponMessage by remember { mutableStateOf<String?>(null) }
    var isCouponApplied by remember { mutableStateOf(false) }

    var isCoinsRedeemed by remember { mutableStateOf(false) }
    val redeemedCoins = if (isCoinsRedeemed) loyaltyCoins.coerceAtMost(100) else 0
    val coinDiscount = redeemedCoins.toDouble() // 1 coin = ₹1

    val productMap = products.associateBy { it.id }
    val cartProducts = cartItems.mapNotNull { item ->
        productMap[item.productId]?.let { product ->
            product to item.quantity
        }
    }

    val itemTotal = cartProducts.sumOf { (product, qty) -> product.price * qty }
    
    // Delivery charge: ₹8 per km (3.0 km = ₹24, free if itemTotal > 199)
    val distanceKm = 3.0
    val deliveryFeePerKm = 8.0
    val calculatedDeliveryFee = distanceKm * deliveryFeePerKm
    val deliveryFee = if (itemTotal > 199 || itemTotal == 0.0) 0.0 else calculatedDeliveryFee
    
    val handlingFee = if (itemTotal == 0.0) 0.0 else 4.0
    val grandTotal = (itemTotal + deliveryFee + handlingFee - appliedDiscountAmount - coinDiscount).coerceAtLeast(0.0)
    val totalSavings = cartProducts.sumOf { (product, qty) -> (product.mrp - product.price).coerceAtLeast(0.0) * qty } + appliedDiscountAmount + coinDiscount

    fun applyCoupon() {
        when (couponCode.trim().uppercase()) {
            "VYRA50" -> {
                val discount = (itemTotal * 0.50).coerceAtMost(150.0)
                appliedDiscountAmount = discount
                isCouponApplied = true
                couponMessage = "🎉 VYRA50 applied! Saved ₹${discount.toInt()}"
            }
            "FLAT100", "ZEPTO100" -> {
                if (itemTotal >= 300) {
                    appliedDiscountAmount = 100.0
                    isCouponApplied = true
                    couponMessage = "🎉 ${couponCode.trim().uppercase()} applied! Saved ₹100"
                } else {
                    couponMessage = "Minimum order of ₹300 required for this coupon."
                }
            }
            else -> {
                couponMessage = "Invalid coupon code. Try VYRA50 or FLAT100"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart (${cartProducts.sumOf { it.second }} items)") },
                actions = {
                    if (cartProducts.isNotEmpty()) {
                        IconButton(onClick = onClearCart) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear Cart")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (cartProducts.isNotEmpty()) {
                Surface(
                    tonalElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "To Pay", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(text = "₹${grandTotal.toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { onProceedToCheckout(grandTotal, redeemedCoins) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B38FB)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Proceed to Checkout ⚡", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🛒", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Your cart is empty",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Add groceries & essentials for 10-minute delivery",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Surface(
                        color = Color(0xFF6B38FB).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⚡", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Delivery in 10 minutes (₹8/km rate)",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6B38FB)
                                )
                                Text(
                                    text = "Total savings ₹${totalSavings.toInt()} on this order!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Loyalty Points (VYRA Coins) Redemption Card
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE600).copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = "Coins", tint = Color(0xFFF57F17))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = "VYRA Coins Balance: $loyaltyCoins", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(text = "Redeem up to 100 coins (₹100 OFF)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Switch(
                                checked = isCoinsRedeemed,
                                onCheckedChange = { isCoinsRedeemed = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF6B38FB))
                            )
                        }
                    }
                }

                // Discount Code Section
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocalOffer, contentDescription = "Offer", tint = Color(0xFF6B38FB), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Apply Discount Coupon", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = couponCode,
                                    onValueChange = { couponCode = it },
                                    placeholder = { Text("Try VYRA50 or FLAT100") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                )
                                Button(
                                    onClick = { applyCoupon() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B38FB)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Apply")
                                }
                            }
                            if (couponMessage != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = couponMessage!!,
                                    color = if (isCouponApplied) Color(0xFF2E7D32) else Color.Red,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                items(cartProducts) { (product, qty) ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = product.title, fontWeight = FontWeight.Bold)
                                Text(text = product.unit, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "₹${product.price.toInt()}", fontWeight = FontWeight.Bold)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(Color(0xFF6B38FB), RoundedCornerShape(8.dp))
                                    .height(36.dp)
                            ) {
                                IconButton(
                                    onClick = { onQuantityChanged(product.id, qty - 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text(text = "-", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = "$qty",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(
                                    onClick = { onQuantityChanged(product.id, qty + 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text(text = "+", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Bill Details", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Item Total")
                                Text("₹${itemTotal.toInt()}")
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Delivery Charge (${distanceKm} km @ ₹8/km)")
                                Text(if (deliveryFee == 0.0) "FREE" else "₹${deliveryFee.toInt()}")
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Handling Charge")
                                Text("₹${handlingFee.toInt()}")
                            }
                            if (appliedDiscountAmount > 0) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Coupon Discount", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                                    Text("-₹${appliedDiscountAmount.toInt()}", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                                }
                            }
                            if (coinDiscount > 0) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("VYRA Coins Redeemed", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                                    Text("-₹${coinDiscount.toInt()}", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                                }
                            }
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Grand Total", fontWeight = FontWeight.Bold)
                                Text("₹${grandTotal.toInt()}", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
