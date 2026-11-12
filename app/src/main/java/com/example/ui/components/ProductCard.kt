package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.theme.TernivaHarvestGoldLight

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Product Visual Header / Banner representation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                when (product.category) {
                                    "Pertanian" -> Color(0xFFE8F5E9)
                                    "Peternakan" -> Color(0xFFFFF3E0)
                                    "Perikanan" -> Color(0xFFE0F7FA)
                                    "Pupuk" -> Color(0xFFF1F8E9)
                                    else -> Color(0xFFF3E5F5)
                                },
                                Color.White
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(TernivaGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = product.name,
                            tint = TernivaGreen,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.category,
                        color = TernivaGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Discount Badge
                if (product.discountPercent > 0) {
                    Surface(
                        color = TernivaHarvestGold,
                        shape = RoundedCornerShape(bottomStart = 8.dp),
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "-${product.discountPercent}%",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Flash Sale / Promo Panen tag (top left)
                if (product.isFlashSale) {
                    Surface(
                        color = TernivaGreen,
                        shape = RoundedCornerShape(bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "🌿 PROMO PANEN",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Body info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Title (2 lines)
                Text(
                    text = product.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Price Row
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Rp",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TernivaGreen
                    )
                    Text(
                        text = String.format("%,d", product.price),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TernivaGreen
                    )
                    Text(
                        text = "/${product.unit}",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 2.dp, bottom = 1.dp)
                    )
                }

                // Original price with strike-through
                if (product.originalPrice != null && product.originalPrice > product.price) {
                    Text(
                        text = "Rp ${String.format("%,d", product.originalPrice)}",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Badges row: Gratis Ongkir & Verified Supplier
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.freeShippingBadge) {
                        Surface(
                            shape = RoundedCornerShape(2.dp),
                            color = Color(0xFF00BFA5).copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFF00BFA5))
                        ) {
                            Text(
                                text = "Gratis Ongkir",
                                color = Color(0xFF00796B),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    if (product.isSupplierVerified) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = TernivaGreen,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Rating & Sold & Location (Shopee compact footer)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "${product.rating}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155),
                        modifier = Modifier.padding(start = 2.dp)
                    )
                    Text(
                        text = " | ${if (product.soldCount > 1000) "${String.format("%.1f", product.soldCount / 1000f)}rb" else product.soldCount} terjual",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = product.location,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
