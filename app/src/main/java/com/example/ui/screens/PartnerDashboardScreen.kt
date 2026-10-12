package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.OrderEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerDashboardScreen(
    partnerName: String,
    partnerPhone: String,
    partnerBikeNo: String,
    partnerLicence: String,
    orders: List<OrderEntity>,
    onUpdateOrderStatus: (Long, String) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Delivery Partner Portal ⚡") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6B38FB), titleContentColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Partner Profile Card
            Surface(
                color = Color(0xFFFFE600),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DeliveryDining, contentDescription = "Partner", tint = Color.Black, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Welcome, $partnerName", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 16.sp)
                            Text(text = "Phone: $partnerPhone • Bike: $partnerBikeNo", style = MaterialTheme.typography.bodySmall, color = Color.Black.copy(alpha = 0.8f))
                            Text(text = "Licence: $partnerLicence", style = MaterialTheme.typography.bodySmall, color = Color.Black.copy(alpha = 0.8f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Incoming 10-Min Orders (Accept / Reject)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No pending orders right now.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders) { order ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Surface(
                                        color = if (order.status.contains("Rejected")) Color.Red else Color(0xFF6B38FB),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = order.status,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = "Loc", tint = Color(0xFF6B38FB), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = order.deliveryAddress, style = MaterialTheme.typography.bodyMedium)
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Amount: ₹${order.totalAmount.toInt()}", fontWeight = FontWeight.Bold, color = Color(0xFF6B38FB))

                                Spacer(modifier = Modifier.height(12.dp))

                                // Accept / Reject / Out for Delivery / Delivered options
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { onUpdateOrderStatus(order.id, "Rejected ❌") },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Reject")
                                    }
                                    Button(
                                        onClick = { onUpdateOrderStatus(order.id, "Accepted & Preparing ⚡") },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B38FB)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Accept")
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { onUpdateOrderStatus(order.id, "Out for Delivery ⚡") },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Out for Delivery")
                                    }
                                    Button(
                                        onClick = { onUpdateOrderStatus(order.id, "Delivered Successfully ✅") },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0.1f, 0.7f, 0.2f, 1f)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Delivered")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
