package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Product
import com.example.ui.components.PantauPanenCard
import com.example.ui.components.ProductCard
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaGreenDeep
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.theme.TernivaHarvestGoldDark
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

data class HomeCategory(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

data class ServiceShortcut(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val action: () -> Unit
)

@Composable
fun HomeScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()
    val programs by viewModel.programs.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredProducts = remember(products, selectedCategory, searchQuery) {
        products.filter { product ->
            val matchCat = selectedCategory == "Semua" || product.category.contains(selectedCategory, ignoreCase = true)
            val matchQuery = searchQuery.isEmpty() ||
                    product.name.contains(searchQuery, ignoreCase = true) ||
                    product.category.contains(searchQuery, ignoreCase = true) ||
                    product.location.contains(searchQuery, ignoreCase = true)
            matchCat && matchQuery
        }
    }

    val categories = listOf(
        HomeCategory("Semua", Icons.Default.AllInclusive, TernivaGreen),
        HomeCategory("Pertanian", Icons.Default.Grass, Color(0xFF2E7D32)),
        HomeCategory("Peternakan", Icons.Default.Pets, Color(0xFFD84315)),
        HomeCategory("Perikanan", Icons.Default.SetMeal, Color(0xFF0277BD)),
        HomeCategory("Buah & Sayur", Icons.Default.LocalFlorist, Color(0xFFE65100)),
        HomeCategory("Hasil Laut", Icons.Default.Water, Color(0xFF00838F)),
        HomeCategory("Pupuk", Icons.Default.Compost, Color(0xFF558B2F)),
        HomeCategory("Pakan", Icons.Default.Grain, Color(0xFFEF6C00)),
        HomeCategory("Obat & Vaksin", Icons.Default.Medication, Color(0xFFC2185B)),
        HomeCategory("Bibit & Benih", Icons.Default.Spa, Color(0xFF388E3C)),
        HomeCategory("Alat Tani", Icons.Default.Handyman, Color(0xFF455A64)),
        HomeCategory("Alat Ternak", Icons.Default.Hardware, Color(0xFF5D4037))
    )

    val shortcuts = listOf(
        ServiceShortcut("Pantau Panen", Icons.Default.Visibility, Color(0xFF00875A)) {
            viewModel.navigateTo(CurrentScreen.PantauPanenCatalog)
        },
        ServiceShortcut("Gabung Mitra", Icons.Default.Handshake, Color(0xFF059669)) {
            viewModel.navigateTo(CurrentScreen.PantauPanenCatalog)
        },
        ServiceShortcut("Ekspor", Icons.Default.Public, Color(0xFF10B981)) {
            viewModel.navigateTo(CurrentScreen.ExportHub)
        },
        ServiceShortcut("Konsultasi\n(Dok Hewan/Tanaman)", Icons.Default.MedicalServices, Color(0xFF0284C7)) {
            viewModel.navigateTo(CurrentScreen.AiConsultationHub)
        }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8F6)),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // 1. Modern Interactive Agritech Atmosphere & Bento Command Hub
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF064E3B),
                                Color(0xFF047857),
                                TernivaGreen
                            )
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Live Agro-Sensor Pulse Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFF10B981),
                            shape = CircleShape,
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LIVE AGRO-SENSOR · Panen Q2: 14.820 Ton Terdistribusi",
                            color = Color(0xFFA7F3D0),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "24.8°C",
                        color = Color(0xFF6EE7B7),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Main Hero Bento Card: Pantau Panen IoT (Preserved & Featured)
                Card(
                    shape = RoundedCornerShape(0.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clickable { viewModel.navigateTo(CurrentScreen.PantauPanenCatalog) }
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.banner_pantau_panen_1794441162382),
                            contentDescription = "Pantau Panen IoT",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xCC000000))
                                    )
                                )
                        )
                        // Top live badges
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color(0xFFE11D48),
                                shape = RoundedCornerShape(0.dp)
                            ) {
                                Text(
                                    text = "● CCTV LIVE",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Surface(
                                color = Color(0x99000000),
                                shape = RoundedCornerShape(0.dp)
                            ) {
                                Text(
                                    text = "Suhu 24.8°C · ADG +1.38 kg",
                                    color = Color(0xFF6EE7B7),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // Bottom caption
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Pantau Panen: Titip Tani & Ternak",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Monitoring sensor & CCTV kandang 24/7",
                                color = Color(0xFFD1D5DB),
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 3 Companion Modern Cards: Gabung Mitra, Ekspor, Konsultasi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Gabung Mitra
                    Card(
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.navigateTo(CurrentScreen.PantauPanenCatalog) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Icon(
                                imageVector = Icons.Default.Handshake,
                                contentDescription = "Gabung Mitra",
                                tint = Color(0xFFA7F3D0),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Gabung Mitra",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Bagi Hasil +28%",
                                color = Color(0xFF6EE7B7),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Ekspor
                    Card(
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.navigateTo(CurrentScreen.ExportHub) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = "Ekspor",
                                tint = Color(0xFF5EEAD4),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Ekspor Global",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "14 Negara",
                                color = Color(0xFF5EEAD4),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Konsultasi (Dok Hewan/Tanaman)
                    Card(
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.navigateTo(CurrentScreen.AiConsultationHub) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = "Konsultasi",
                                tint = Color(0xFF7DD3FC),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Konsultasi Dok",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Hewan / Tanaman",
                                color = Color(0xFF7DD3FC),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // 3. Category Selector Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(Color.White)
                    .padding(vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kategori Komoditas & Pasokan",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "Semua Kategori >",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TernivaGreen,
                        modifier = Modifier.clickable { viewModel.setCategory("Semua") }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat.name
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCategory(cat.name) },
                            label = { Text(cat.name, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            leadingIcon = {
                                Icon(
                                    imageVector = cat.icon,
                                    contentDescription = cat.name,
                                    tint = if (isSelected) Color.White else cat.color,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TernivaGreen,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("category_chip_${cat.name}")
                        )
                    }
                }
            }
        }

        // 4. Shopee-Style Flash Sale Section
        item {
            Card(
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                    // Flash Sale Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ FLASH SALE",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = TernivaHarvestGold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Countdown timer pills
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = Color.Black, shape = RoundedCornerShape(3.dp)) {
                                Text("02", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                            }
                            Text(" : ", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Surface(color = Color.Black, shape = RoundedCornerShape(3.dp)) {
                                Text("45", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                            }
                            Text(" : ", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Surface(color = Color.Black, shape = RoundedCornerShape(3.dp)) {
                                Text("18", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Lihat Semua >",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Horizontal Flash Products
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val flashList = products.filter { it.isFlashSale }
                        items(flashList) { product ->
                            Card(
                                modifier = Modifier
                                    .width(130.dp)
                                    .clickable { viewModel.navigateTo(CurrentScreen.ProductDetail(product)) },
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                                elevation = CardDefaults.cardElevation(1.dp)
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFE8F5E9)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Eco,
                                            contentDescription = null,
                                            tint = TernivaGreen,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Surface(
                                            color = TernivaHarvestGold,
                                            shape = RoundedCornerShape(bottomStart = 6.dp),
                                            modifier = Modifier.align(Alignment.TopEnd)
                                        ) {
                                            Text(
                                                "-${product.discountPercent}%",
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = product.name,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Rp ${String.format("%,d", product.price)}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TernivaHarvestGold
                                    )
                                    // Progress bar "Segera Habis"
                                    LinearProgressIndicator(
                                        progress = { 0.75f },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .padding(top = 4.dp),
                                        color = TernivaHarvestGold,
                                        trackColor = Color(0xFFFFCCBC)
                                    )
                                    Text(
                                        text = "⚡ Segera Habis",
                                        fontSize = 8.sp,
                                        color = TernivaHarvestGold,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Section: Pantau Panen — Titip Tani & Ternak
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🌾 Pantau Panen",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = TernivaGreen
                            )
                            Surface(
                                color = TernivaGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.padding(start = 6.dp)
                            ) {
                                Text(
                                    text = "Titip Tani & Ternak",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TernivaGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Titip kelola komoditas tani/ternak dengan transparansi & monitoring berkala",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Pantau Panen Banner
                Card(
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clickable { viewModel.navigateTo(CurrentScreen.PantauPanenDetail(programs.first())) }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.banner_pantau_panen_1794441162382),
                        contentDescription = "Pantau Panen Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Active Programs Carousel Cards
                programs.take(2).forEach { program ->
                    PantauPanenCard(
                        program = program,
                        onClick = { viewModel.navigateTo(CurrentScreen.PantauPanenDetail(program)) },
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
            }
        }

        // 6. Section: Rekomendasi Untuk Kamu (Shopee-Style 2-Column Product Grid)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(16.dp)
                            .background(TernivaGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "REKOMENDASI UNTUK KAMU",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${filteredProducts.size} Produk",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Render 2-column grid rows
        val productPairs = filteredProducts.chunked(2)
        items(productPairs) { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProductCard(
                    product = pair[0],
                    onClick = { viewModel.navigateTo(CurrentScreen.ProductDetail(pair[0])) },
                    modifier = Modifier.weight(1f)
                )

                if (pair.size > 1) {
                    ProductCard(
                        product = pair[1],
                        onClick = { viewModel.navigateTo(CurrentScreen.ProductDetail(pair[1])) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
