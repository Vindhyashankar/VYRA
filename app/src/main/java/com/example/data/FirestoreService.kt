package com.example.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun syncOrderToCloud(orderId: Long, totalAmount: Double, address: String) {
        try {
            val orderData = hashMapOf(
                "orderId" to orderId,
                "totalAmount" to totalAmount,
                "address" to address,
                "status" to "Packing at Dark Store ⚡",
                "partnerName" to "Ramesh Kumar",
                "partnerPhone" to "+91 98765 12345",
                "partnerVehicle" to "Electric Scooter (EV-09)",
                "estimatedMinutes" to 10,
                "timestamp" to System.currentTimeMillis()
            )
            firestore.collection("orders").document(orderId.toString()).set(orderData).await()
        } catch (e: Exception) {
            // Fallback gracefully if offline or unauthenticated
        }
    }

    fun listenToOrderRealtime(orderId: Long): Flow<CloudOrderState?> = callbackFlow {
        val listener = firestore.collection("orders").document(orderId.toString())
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || !snapshot.exists()) {
                    trySend(null)
                    return@addSnapshotListener
                }
                val data = snapshot.data
                if (data != null) {
                    val state = CloudOrderState(
                        orderId = (data["orderId"] as? Long) ?: orderId,
                        status = (data["status"] as? String) ?: "Out for Delivery",
                        partnerName = (data["partnerName"] as? String) ?: "Ramesh Kumar",
                        partnerPhone = (data["partnerPhone"] as? String) ?: "+91 98765 12345",
                        partnerVehicle = (data["partnerVehicle"] as? String) ?: "Electric Scooter",
                        estimatedMinutes = (data["estimatedMinutes"] as? Long)?.toInt() ?: 8
                    )
                    trySend(state)
                } else {
                    trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }
}

data class CloudOrderState(
    val orderId: Long,
    val status: String,
    val partnerName: String,
    val partnerPhone: String,
    val partnerVehicle: String,
    val estimatedMinutes: Int
)
