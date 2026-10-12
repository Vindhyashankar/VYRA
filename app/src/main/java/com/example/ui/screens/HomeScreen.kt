package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductEntity
import com.example.ui.components.InteractiveSearchHeader
import com.example.ui.components.ProductCard
import com.example.ui.components.PromotionalCarousel

@Composable
fun HomeScreen(
    products: List<ProductEntity>,
    recommendedProducts: List<ProductEntity>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    cartMap: Map<Long, Int>,
    onQuantityChanged: (Long, Int) -> Unit,
    onProductClick: (Long) -> Unit,
    onCategorySelected: (String) -> Unit,
    locationName: String,
    onLocationClick: () -> Unit
) {
    val categories = listOf(
        "All",
        "Fruits & Veggies",
        "Dairy & Breakfast",
        "Munchies & Snacks",
        "Beverages",
        "Instant & Frozen",
        "Household",
        "Personal Care"
    )

    val searchSuggestions = if (searchQuery.isBlank()) emptyList() else {
        products.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true) ||
                    it.tags.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingBottomWindowInsets
    ) {
        item {
            InteractiveSearchHeader(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                suggestions = searchSuggestions,
                onSuggestionClick = { productId ->
                    onProductClick(productId)
                },
                locationName = locationName,
                onLocationClick = onLocationClick
            )
        }

        // Promotional Offers & Seasonal Discounts Carousel
        item {
            Spacer(modifier = Modifier.height(16.dp))
            PromotionalCarousel()
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Categories Carousel
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Explore Categories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(categories.drop(1)) { cat ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(75.dp)
                                .clickable { onCategorySelected(cat) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF6B38FB).copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat.take(2).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6B38FB),
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Personalized Recommendations Section
        if (recommendedProducts.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✨ Recommended for You",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Based on your activity",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(recommendedProducts.take(6)) { product ->
                            ProductCard(
                                product = product,
                                quantity = cartMap[product.id] ?: 0,
                                onQuantityChanged = { qty -> onQuantityChanged(product.id, qty) },
                                onClick = { onProductClick(product.id) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Bestsellers Section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "🔥 VYRA Bestsellers",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(products.filter { it.isBestseller }) { product ->
                        ProductCard(
                            product = product,
                            quantity = cartMap[product.id] ?: 0,
                            onQuantityChanged = { qty -> onQuantityChanged(product.id, qty) },
                            onClick = { onProductClick(product.id) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // All Groceries Section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "🛒 All Groceries & Essentials",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        items(products.chunked(2)) { rowProducts ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (product in rowProducts) {
                    Box(modifier = Modifier.weight(1f)) {
                        ProductCard(
                            product = product,
                            quantity = cartMap[product.id] ?: 0,
                            onQuantityChanged = { qty -> onQuantityChanged(product.id, qty) },
                            onClick = { onProductClick(product.id) }
                        )
                    }
                }
                if (rowProducts.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

private val PaddingBottomWindowInsets = PaddingValues(bottom = 90.dp)
