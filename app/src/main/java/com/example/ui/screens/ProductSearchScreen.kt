package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ProductCard
import com.example.ui.theme.TernivaGreen
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchScreen(
    initialQuery: String = "",
    initialCategory: String = "Semua",
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf(initialQuery) }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var selectedSort by remember { mutableStateOf("Terkait") } // Terkait, Terlaris, Termurah, Termahal, Rating

    val products by viewModel.products.collectAsState()

    val categories = listOf(
        "Semua",
        "Pertanian",
        "Peternakan",
        "Perikanan",
        "Pupuk",
        "Pakan",
        "Obat dan Vaksin",
        "Bibit dan Benih",
        "Alat Pertanian"
    )

    val popularKeywords = listOf(
        "Beras Cianjur",
        "Pupuk NPK Hayati",
        "Bibit Cabai Rawit",
        "Konsentrat Sapi",
        "Ikan Nila Segar",
        "Sprayer Hama"
    )

    val filteredProducts = remember(products, searchQuery, selectedCategory, selectedSort) {
        var list = products

        if (selectedCategory != "Semua") {
            list = list.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }

        if (searchQuery.isNotBlank()) {
            val q = searchQuery.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                it.category.lowercase().contains(q) ||
                it.supplierName.lowercase().contains(q) ||
                it.location.lowercase().contains(q)
            }
        }

        when (selectedSort) {
            "Terlaris" -> list.sortedByDescending { it.soldCount }
            "Termurah" -> list.sortedBy { it.price }
            "Termahal" -> list.sortedByDescending { it.price }
            "Rating" -> list.sortedByDescending { it.rating }
            else -> list
        }
    }

    Scaffold(
        topBar = {
            Surface(color = Color.White, shadowElevation = 2.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari komoditas, bibit, pupuk, pakan...", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(20.dp))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Hapus", modifier = Modifier.size(16.dp))
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("search_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TernivaGreen,
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
                    )

                    IconButton(onClick = { viewModel.navigateTo(CurrentScreen.Cart) }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Keranjang", tint = TernivaGreen)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F8F6))
        ) {
            // Category Filter Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TernivaGreen,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            // Quick Sort Row
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Urutkan:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )

                    listOf("Terkait", "Terlaris", "Termurah", "Termahal", "Rating").forEach { sortOption ->
                        val isSelected = selectedSort == sortOption
                        Text(
                            text = sortOption,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) TernivaGreen else Color(0xFF475569),
                            modifier = Modifier
                                .clickable { selectedSort = sortOption }
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Trending Keywords recommendation if query is empty
            if (searchQuery.isBlank()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Pencarian Populer Petani & Horeka:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        popularKeywords.forEach { keyword ->
                            Surface(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.clickable { searchQuery = keyword }
                            ) {
                                Text(
                                    text = keyword,
                                    fontSize = 10.sp,
                                    color = Color(0xFF334155),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Results Counter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ditemukan ${filteredProducts.size} Produk Agritech",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            // Product Grid
            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.SearchOff, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(54.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Produk Tidak Ditemukan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                        Text(
                            text = "Coba ubah kata kunci atau pilih kategori lain.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedButton(
                            onClick = {
                                searchQuery = ""
                                selectedCategory = "Semua"
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TernivaGreen)
                        ) {
                            Text("Reset Pencarian")
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            onClick = { viewModel.navigateTo(CurrentScreen.ProductDetail(product)) }
                        )
                    }
                }
            }
        }
    }
}
