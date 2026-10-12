package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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

data class PromoBanner(
    val title: String,
    val subtitle: String,
    val discountBadge: String,
    val gradientColors: List<Color>
)

@Composable
fun PromotionalCarousel() {
    val banners = listOf(
        PromoBanner(
            title = "Mega Grocery Carnival ⚡",
            subtitle = "Up to 40% OFF on Fresh Fruits, Vegetables & Dairy Essentials",
            discountBadge = "SAVE ₹200",
            gradientColors = listOf(Color(0xFF6B38FB), Color(0xFF9D65FF))
        ),
        PromoBanner(
            title = "Summer Mango Festival 🥭",
            subtitle = "Direct farm-fresh Alphonso Mangos delivered in 10 minutes",
            discountBadge = "25% OFF",
            gradientColors = listOf(Color(0xFF8E24AA), Color(0xFFAB47BC))
        ),
        PromoBanner(
            title = "Midnight Munchies & Snacks 🍿",
            subtitle = "Chips, chocolates, cold drinks & ice creams delivered chilled",
            discountBadge = "FREE DELIVERY",
            gradientColors = listOf(Color(0xFF5E35B1), Color(0xFF7E57C2))
        )
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "🌟 Seasonal Offers & Promotions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(banners) { banner ->
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(140.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.horizontalGradient(banner.gradientColors))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = Color(0xFFFFE600),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = banner.discountBadge,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = banner.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = banner.subtitle,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}
