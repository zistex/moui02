package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ProductCard
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaGreenDeep
import com.example.ui.theme.TernivaGreenLight
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreProfileScreen(
    supplierName: String,
    location: String = "Kab. Cianjur, Jawa Barat",
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val store = remember(supplierName) { viewModel.getStoreProfile(supplierName, location) }
    val allProducts by viewModel.products.collectAsState()
    val storeProducts = remember(allProducts, supplierName) {
        allProducts.filter { it.supplierName.equals(supplierName, ignoreCase = true) }.ifEmpty { allProducts.take(4) }
    }
    val reviews = remember { viewModel.getReviewsForProduct("PRD-01") }

    var selectedStoreTab by remember { mutableStateOf(0) } // 0: Produk Toko, 1: Ulasan Toko & Reputasi
    var isFollowing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(store.name, fontSize = 15.sp, maxLines = 1, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Share, contentDescription = "Bagikan Toko")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { isFollowing = !isFollowing },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TernivaGreen),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TernivaGreen)
                    ) {
                        Icon(if (isFollowing) Icons.Default.Check else Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isFollowing) "Mengikuti" else "+ Ikuti Toko", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.navigateTo(CurrentScreen.ChatRoom(store.name)) },
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Outlined.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Chat Penjual", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF7FAF7))
        ) {
            // 1. Store Header Profile
            item {
                Surface(
                    color = TernivaGreenDeep,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Store, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(36.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = store.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    if (store.isVerified) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.Verified, contentDescription = "Verified", tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp))
                                    }
                                }
                                Text("📍 ${store.location}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF4ADE80)))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(store.lastOnline, fontSize = 10.sp, color = Color.White.copy(alpha = 0.95f), fontWeight = FontWeight.Medium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Reputasi Toko Grid (Rating, Chat, Pengiriman, Pengikut)
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                                        Text(" ${store.rating}", fontSize = 13.sp, fontWeight = FontWeight.Black)
                                    }
                                    Text("${store.ratingCount} Ulasan", fontSize = 10.sp, color = Color.Gray)
                                }

                                Divider(modifier = Modifier.height(28.dp).width(1.dp), color = Color(0xFFE2E8F0))

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(store.chatResponseRate.take(3), fontSize = 13.sp, fontWeight = FontWeight.Black, color = TernivaGreen)
                                    Text("Performa Chat", fontSize = 10.sp, color = Color.Gray)
                                }

                                Divider(modifier = Modifier.height(28.dp).width(1.dp), color = Color(0xFFE2E8F0))

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(store.followersCount, fontSize = 13.sp, fontWeight = FontWeight.Black)
                                    Text("Pengikut", fontSize = 10.sp, color = Color.Gray)
                                }

                                Divider(modifier = Modifier.height(28.dp).width(1.dp), color = Color(0xFFE2E8F0))

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("< 24 Jam", fontSize = 13.sp, fontWeight = FontWeight.Black, color = TernivaGreen)
                                    Text("Kirim Cepat", fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }

            // 2. Tab: Produk Toko vs Ulasan
            item {
                Surface(color = Color.White, shadowElevation = 1.dp) {
                    TabRow(
                        selectedTabIndex = selectedStoreTab,
                        containerColor = Color.White,
                        contentColor = TernivaGreen
                    ) {
                        Tab(
                            selected = selectedStoreTab == 0,
                            onClick = { selectedStoreTab = 0 },
                            text = { Text("Katalog Produk (${storeProducts.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedStoreTab == 1,
                            onClick = { selectedStoreTab = 1 },
                            text = { Text("Reputasi & Ulasan (${reviews.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }

            if (selectedStoreTab == 0) {
                // Products grid in pairs
                val pairs = storeProducts.chunked(2)
                items(pairs) { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
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
            } else {
                // Ulasan & Reputasi Detail
                item {
                    Surface(
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Ringkasan Kepuasan Pembeli", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("4.9", fontSize = 28.sp, fontWeight = FontWeight.Black, color = TernivaGreen)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row {
                                        repeat(5) {
                                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                                        }
                                    }
                                    Text("98% Pembeli merekomendasikan supplier ini", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }

                items(reviews) { rev ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(rev.userName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text(rev.userRole, fontSize = 10.sp, color = TernivaGreen, fontWeight = FontWeight.SemiBold)
                                }
                                Text(rev.date, fontSize = 10.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row {
                                repeat(rev.rating) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(13.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(rev.comment, fontSize = 12.sp, color = Color(0xFF334155), lineHeight = 16.sp)

                            if (rev.supplierReply != null) {
                                Surface(
                                    color = Color(0xFFF8FAFC),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("Balasan dari Penjual:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                                        Text(rev.supplierReply, fontSize = 11.sp, color = Color(0xFF475569))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
