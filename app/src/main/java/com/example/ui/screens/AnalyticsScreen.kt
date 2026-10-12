package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.OrderEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    orders: List<OrderEntity>,
    onBack: () -> Unit
) {
    val monthlySpending = listOf(
        Pair("May", 1250f),
        Pair("Jun", 1820f),
        Pair("Jul", 2100f),
        Pair("Aug", 1650f),
        Pair("Sep", 2400f),
        Pair("Oct", 3100f)
    )

    val maxSpent = monthlySpending.maxOf { it.second }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spending Trends & Analytics 📊") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6B38FB).copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Total Spent (6M)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "₹12,220", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B38FB))
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE600).copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Orders Placed", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${orders.size + 24}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }

            // Spending Trend Bar Chart Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Monthly Spending Trend (May - Oct)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Track your quick commerce grocery expenditure", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Canvas Bar Chart
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height - 40f
                            val barWidth = canvasWidth / (monthlySpending.size * 2f)
                            val spacing = barWidth

                            monthlySpending.forEachIndexed { index, (month, amount) ->
                                val barHeight = (amount / maxSpent) * canvasHeight
                                val left = index * (barWidth + spacing) + spacing / 2f
                                val top = canvasHeight - barHeight

                                drawRect(
                                    color = Color(0xFF6B38FB),
                                    topLeft = Offset(left, top),
                                    size = Size(barWidth, barHeight)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for ((month, amount) in monthlySpending) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "₹${amount.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(text = month, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Order Frequency Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Order Frequency & Speed Insights", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "⚡ Average delivery time: 8 minutes 42 seconds\n⚡ Most ordered category: Fruits & Veggies\n⚡ Reorder frequency: Every 3 days", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
