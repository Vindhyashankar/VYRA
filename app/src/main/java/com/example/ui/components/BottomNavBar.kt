package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun ZeptoBottomNav(
    currentRoute: String,
    cartCount: Int,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { onNavigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            modifier = Modifier.testTag("nav_home")
        )
        NavigationBarItem(
            selected = currentRoute == "browse",
            onClick = { onNavigate("browse") },
            icon = { Icon(Icons.Default.Search, contentDescription = "Browse") },
            label = { Text("Browse") },
            modifier = Modifier.testTag("nav_browse")
        )
        NavigationBarItem(
            selected = currentRoute == "recent_orders",
            onClick = { onNavigate("recent_orders") },
            icon = { Icon(Icons.Default.Refresh, contentDescription = "Reorder") },
            label = { Text("Reorder") },
            modifier = Modifier.testTag("nav_reorder")
        )
        NavigationBarItem(
            selected = currentRoute == "cart",
            onClick = { onNavigate("cart") },
            icon = {
                if (cartCount > 0) {
                    BadgedBox(badge = { Badge { Text("$cartCount") } }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                } else {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            },
            label = { Text("Cart") },
            modifier = Modifier.testTag("nav_cart")
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            modifier = Modifier.testTag("nav_profile")
        )
    }
}
