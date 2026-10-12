package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onLoginAsCustomer: (String, String, String) -> Unit,
    onLoginAsPartner: (String, String, String, String) -> Unit
) {
    var selectedRole by remember { mutableStateOf("Customer") } // "Customer" or "Partner"

    // Customer fields
    var customerName by remember { mutableStateOf("") }
    var customerLocation by remember { mutableStateOf("Indiranagar, Bangalore") }
    var customerPhone by remember { mutableStateOf("") }

    // Delivery Partner fields
    var partnerName by remember { mutableStateOf("") }
    var partnerPhone by remember { mutableStateOf("") }
    var partnerBikeNo by remember { mutableStateOf("") }
    var partnerLicence by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "VYRA ⚡",
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF6B38FB)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Lightning-fast 10-minute quick commerce",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            TabRow(
                selectedTabIndex = if (selectedRole == "Customer") 0 else 1,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = Color(0xFF6B38FB)
            ) {
                Tab(
                    selected = selectedRole == "Customer",
                    onClick = { selectedRole = "Customer" },
                    text = { Text("Customer", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Customer") }
                )
                Tab(
                    selected = selectedRole == "Partner",
                    onClick = { selectedRole = "Partner" },
                    text = { Text("Delivery Partner", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.DeliveryDining, contentDescription = "Partner") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedRole == "Customer") {
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Your Full Name") },
                    placeholder = { Text("Vindhya Shankar") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = customerLocation,
                    onValueChange = { customerLocation = it },
                    label = { Text("Current Location / Address") },
                    placeholder = { Text("Indiranagar, Bangalore") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it },
                    label = { Text("Phone Number") },
                    placeholder = { Text("+91 98765 43210") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                OutlinedTextField(
                    value = partnerName,
                    onValueChange = { partnerName = it },
                    label = { Text("Partner Full Name") },
                    placeholder = { Text("Ramesh Kumar") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = partnerPhone,
                    onValueChange = { partnerPhone = it },
                    label = { Text("Phone Number") },
                    placeholder = { Text("+91 98765 12345") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = partnerBikeNo,
                    onValueChange = { partnerBikeNo = it },
                    label = { Text("Bike Number") },
                    placeholder = { Text("KA-01-EV-9821") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = partnerLicence,
                    onValueChange = { partnerLicence = it },
                    label = { Text("Driving Licence Number") },
                    placeholder = { Text("DL-1420230012345") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (selectedRole == "Customer") {
                        onLoginAsCustomer(
                            customerName.ifBlank { "VYRA Customer" },
                            customerLocation.ifBlank { "Indiranagar, Bangalore" },
                            customerPhone.ifBlank { "+91 98765 43210" }
                        )
                    } else {
                        onLoginAsPartner(
                            partnerName.ifBlank { "Ramesh Kumar" },
                            partnerPhone.ifBlank { "+91 98765 12345" },
                            partnerBikeNo.ifBlank { "KA-01-EV-9821" },
                            partnerLicence.ifBlank { "DL-1420230012345" }
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B38FB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (selectedRole == "Customer") "Continue as Customer ⚡" else "Partner Portal Login ⚡",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
