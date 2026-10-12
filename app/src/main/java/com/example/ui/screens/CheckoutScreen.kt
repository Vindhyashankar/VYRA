package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AddressEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    grandTotal: Double,
    addresses: List<AddressEntity>,
    onPlaceOrder: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf("UPI") }
    var selectedAddress by remember { mutableStateOf(addresses.firstOrNull()?.fullAddress ?: "Indiranagar, Bangalore") }
    var showScannerDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout & Payment ⚡") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
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
                        Text(text = "Total Amount", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = "₹${grandTotal.toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (selectedPaymentMethod == "Scanner") {
                                showScannerDialog = true
                            } else {
                                onPlaceOrder(selectedAddress, selectedPaymentMethod)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B38FB)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Pay ₹${grandTotal.toInt()} & Place Order ⚡", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Delivery Address", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = selectedAddress, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Text(text = "Select Payment Method", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            PaymentOptionCard(
                title = "UPI (GPay / PhonePe / Paytm)",
                subtitle = "Instant payment via UPI handle",
                icon = Icons.Default.Smartphone,
                isSelected = selectedPaymentMethod == "UPI",
                onClick = { selectedPaymentMethod = "UPI" }
            )

            PaymentOptionCard(
                title = "QR Code Scanner",
                subtitle = "Scan UPI QR code with camera",
                icon = Icons.Default.QrCodeScanner,
                isSelected = selectedPaymentMethod == "Scanner",
                onClick = {
                    selectedPaymentMethod = "Scanner"
                    showScannerDialog = true
                }
            )

            PaymentOptionCard(
                title = "Cash on Delivery (COD)",
                subtitle = "Pay cash when your 10-min order arrives",
                icon = Icons.Default.LocalAtm,
                isSelected = selectedPaymentMethod == "COD",
                onClick = { selectedPaymentMethod = "COD" }
            )

            PaymentOptionCard(
                title = "Credit / Debit Card",
                subtitle = "Visa, MasterCard, RuPay",
                icon = Icons.Default.CreditCard,
                isSelected = selectedPaymentMethod == "Card",
                onClick = { selectedPaymentMethod = "Card" }
            )
        }
    }

    if (showScannerDialog) {
        AlertDialog(
            onDismissRequest = { showScannerDialog = false },
            title = { Text("Scan UPI QR Code") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color.LightGray, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.QrCodeScanner, contentDescription = "QR", modifier = Modifier.size(80.dp), tint = Color(0xFF6B38FB))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Scanning UPI QR...", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Payment of ₹${grandTotal.toInt()} simulated successfully!")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showScannerDialog = false
                        onPlaceOrder(selectedAddress, "QR Scanner")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B38FB))
                ) {
                    Text("Confirm Payment & Order")
                }
            },
            dismissButton = {
                TextButton(onClick = { showScannerDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PaymentOptionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF6B38FB).copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF6B38FB)) else null,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) Color(0xFF6B38FB) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color(0xFF6B38FB)
                )
            }
        }
    }
}
