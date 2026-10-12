package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LocationPickerDialog(
    currentLocation: String,
    onLocationSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val locations = listOf(
        "Indiranagar 4th Block, Bangalore",
        "Koramangala 6th Block, Bangalore",
        "MG Road Central, Bangalore",
        "HSR Layout Sector 2, Bangalore",
        "Whitefield Main Road, Bangalore",
        "Jayanagar 4th Block, Bangalore"
    )

    var customAddress by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Map, contentDescription = "Map", tint = Color(0xFF6B38FB))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Choose Location via Google Maps 📍")
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Select from popular delivery hubs or enter your precise delivery address:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Simulated Map View Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Pin", tint = Color.Red, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "📍 Pinned: $currentLocation", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF2E7D32))
                        Text(text = "GPS Accuracy: High (10m radius)", fontSize = 11.sp, color = Color.Gray)
                    }
                }

                OutlinedTextField(
                    value = customAddress,
                    onValueChange = { customAddress = it },
                    placeholder = { Text("Or type custom street / building name...") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(text = "Popular Hubs:", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                for (loc in locations) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (currentLocation == loc) Color(0xFF6B38FB).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLocationSelected(loc) }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Loc", tint = Color(0xFF6B38FB), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = loc, fontSize = 13.sp, fontWeight = if (currentLocation == loc) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (customAddress.isNotBlank()) {
                        onLocationSelected(customAddress)
                    } else {
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B38FB))
            ) {
                Text("Confirm Location")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
