package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val category: String,
    val price: Double,
    val mrp: Double,
    val unit: String,
    val imageUrl: String,
    val discountText: String,
    val stock: Int,
    val rating: Float,
    val isBestseller: Boolean,
    val tags: String
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val productId: Long,
    val quantity: Int
)

@Entity(tableName = "browsing_history")
data class BrowsingHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val productId: Long,
    val timestamp: Long
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: Long,
    val totalAmount: Double,
    val status: String, // "Delivered", "Packing", "Out for Delivery"
    val deliveryAddress: String
)

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val orderId: Long,
    val productId: Long,
    val title: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String
)

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val fullAddress: String,
    val isDefault: Boolean
)
