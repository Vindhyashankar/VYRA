package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.ZeptoViewModel
import com.example.ui.components.CartPreviewBar
import com.example.ui.components.LocationPickerDialog
import com.example.ui.components.ZeptoBottomNav
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: ZeptoViewModel = viewModel()
                val navController = rememberNavController()

                val products by viewModel.allProducts.collectAsState()
                val recommendedProducts by viewModel.recommendedProducts.collectAsState()
                val cartItems by viewModel.cartItems.collectAsState()
                val orders by viewModel.allOrders.collectAsState()
                val orderItems by viewModel.allOrderItems.collectAsState()
                val addresses by viewModel.addresses.collectAsState()
                val searchQuery by viewModel.searchQuery.collectAsState()
                val selectedCategory by viewModel.selectedCategory.collectAsState()
                val activeOrderState by viewModel.activeOrderState.collectAsState()
                val loyaltyCoins by viewModel.loyaltyCoins.collectAsState()

                var customerName by remember { mutableStateOf("VYRA Customer") }
                var customerLocation by remember { mutableStateOf("Indiranagar 4th Block, Bangalore") }
                var customerPhone by remember { mutableStateOf("+91 98765 43210") }
                var showLocationDialog by remember { mutableStateOf(false) }

                var partnerName by remember { mutableStateOf("Ramesh Kumar") }
                var partnerPhone by remember { mutableStateOf("+91 98765 12345") }
                var partnerBikeNo by remember { mutableStateOf("KA-01-EV-9821") }
                var partnerLicence by remember { mutableStateOf("DL-1420230012345") }

                val cartMap = cartItems.associate { it.productId to it.quantity }
                val cartCount = cartItems.sumOf { it.quantity }
                val subtotal = cartItems.sumOf { item ->
                    val product = products.firstOrNull { it.id == item.productId }
                    if (product != null) product.price * item.quantity else 0.0
                }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "login"

                val showBottomNav = currentRoute in listOf("home", "browse", "recent_orders", "cart", "profile")

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomNav) {
                            Column {
                                CartPreviewBar(
                                    cartCount = cartCount,
                                    subtotal = subtotal,
                                    onViewCart = { navController.navigate("cart") },
                                    onCheckout = { navController.navigate("cart") }
                                )
                                ZeptoBottomNav(
                                    currentRoute = currentRoute,
                                    cartCount = cartCount,
                                    onNavigate = { route ->
                                        navController.navigate(route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(
                                onLoginAsCustomer = { name, location, phone ->
                                    customerName = name
                                    customerLocation = location
                                    customerPhone = phone
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onLoginAsPartner = { name, phone, bikeNo, licence ->
                                    partnerName = name
                                    partnerPhone = phone
                                    partnerBikeNo = bikeNo
                                    partnerLicence = licence
                                    navController.navigate("partner_dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("partner_dashboard") {
                            PartnerDashboardScreen(
                                partnerName = partnerName,
                                partnerPhone = partnerPhone,
                                partnerBikeNo = partnerBikeNo,
                                partnerLicence = partnerLicence,
                                orders = orders,
                                onUpdateOrderStatus = { orderId, status ->
                                    viewModel.updateActiveOrderStatus(status, 300)
                                },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("home") {
                            HomeScreen(
                                products = products,
                                recommendedProducts = recommendedProducts,
                                searchQuery = searchQuery,
                                onSearchQueryChanged = { q -> viewModel.setSearchQuery(q) },
                                cartMap = cartMap,
                                onQuantityChanged = { id, qty -> viewModel.updateCart(id, qty) },
                                onProductClick = { id ->
                                    viewModel.recordBrowsing(id)
                                    navController.navigate("product_detail/$id")
                                },
                                onCategorySelected = { cat ->
                                    viewModel.setSelectedCategory(cat)
                                    navController.navigate("browse")
                                },
                                locationName = customerLocation,
                                onLocationClick = { showLocationDialog = true }
                            )
                        }

                        composable("browse") {
                            BrowseScreen(
                                products = products,
                                searchQuery = searchQuery,
                                selectedCategory = selectedCategory,
                                cartMap = cartMap,
                                onSearchChanged = { q -> viewModel.setSearchQuery(q) },
                                onCategorySelected = { cat -> viewModel.setSelectedCategory(cat) },
                                onQuantityChanged = { id, qty -> viewModel.updateCart(id, qty) },
                                onProductClick = { id ->
                                    viewModel.recordBrowsing(id)
                                    navController.navigate("product_detail/$id")
                                }
                            )
                        }

                        composable("recent_orders") {
                            RecentOrdersScreen(
                                orders = orders,
                                orderItems = orderItems,
                                onQuickAddToCart = { productId, title, price, imageUrl ->
                                    viewModel.updateCart(productId, 1)
                                },
                                onViewTracker = { navController.navigate("tracker") }
                            )
                        }

                        composable("cart") {
                            CartScreen(
                                cartItems = cartItems,
                                products = products,
                                loyaltyCoins = loyaltyCoins,
                                onQuantityChanged = { id, qty -> viewModel.updateCart(id, qty) },
                                onClearCart = { viewModel.clearCart() },
                                onProceedToCheckout = { total, redeemedCoins ->
                                    val defaultAddress = addresses.firstOrNull()?.fullAddress ?: customerLocation
                                    viewModel.placeOrder(total, defaultAddress, cartMap.mapKeys { entry -> products.first { it.id == entry.key } }, redeemedCoins) { orderId ->
                                        navController.navigate("tracker") {
                                            popUpTo("home")
                                        }
                                    }
                                }
                            )
                        }

                        composable("checkout") {
                            val grandTotal = subtotal + (if (subtotal > 199 || subtotal == 0.0) 0.0 else 24.0) + (if (subtotal == 0.0) 0.0 else 4.0)
                            CheckoutScreen(
                                grandTotal = grandTotal,
                                addresses = addresses,
                                onPlaceOrder = { address, paymentMethod ->
                                    viewModel.placeOrder(grandTotal, address, cartMap.mapKeys { entry -> products.first { it.id == entry.key } }, 0) { orderId ->
                                        navController.navigate("tracker") {
                                            popUpTo("home")
                                        }
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("profile") {
                            ProfileScreen(
                                addresses = addresses,
                                loyaltyCoins = loyaltyCoins,
                                onOpenAnalytics = { navController.navigate("analytics") },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("analytics") {
                            AnalyticsScreen(
                                orders = orders,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = "product_detail/{productId}",
                            arguments = listOf(navArgument("productId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
                            val product = products.firstOrNull { it.id == productId } ?: products.firstOrNull()
                            if (product != null) {
                                ProductDetailScreen(
                                    product = product,
                                    recommendedProducts = recommendedProducts,
                                    quantity = cartMap[product.id] ?: 0,
                                    onQuantityChanged = { qty -> viewModel.updateCart(product.id, qty) },
                                    onProductClick = { id ->
                                        viewModel.recordBrowsing(id)
                                        navController.navigate("product_detail/$id")
                                    },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }

                        composable("tracker") {
                            OrderTrackerScreen(
                                orderState = activeOrderState,
                                onBack = { navController.navigate("home") { popUpTo(0) } }
                            )
                        }
                    }

                    if (showLocationDialog) {
                        LocationPickerDialog(
                            currentLocation = customerLocation,
                            onLocationSelected = { newLoc ->
                                customerLocation = newLoc
                                showLocationDialog = false
                            },
                            onDismiss = { showLocationDialog = false }
                        )
                    }
                }
            }
        }
    }
}
