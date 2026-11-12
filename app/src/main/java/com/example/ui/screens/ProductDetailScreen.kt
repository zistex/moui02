package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.data.model.Product
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaOrange
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    var quantity by remember { mutableStateOf(product.minOrder) }
    var showAddedSnackbar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name, fontSize = 15.sp, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { viewModel.navigateTo(CurrentScreen.Cart) }
                    ) {
                        IconButton(onClick = { viewModel.navigateTo(CurrentScreen.Cart) }) {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Keranjang")
                        }
                        if (cartItems.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-4).dp, y = 4.dp)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(TernivaGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cartItems.size.toString(),
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
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
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Chat Supplier
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { viewModel.navigateTo(CurrentScreen.ChatRoom(product.supplierName, product)) }
                            .testTag("chat_supplier_button")
                    ) {
                        Icon(Icons.Outlined.Chat, contentDescription = "Chat", tint = TernivaGreen)
                        Text("Chat", fontSize = 10.sp, color = TernivaGreen, fontWeight = FontWeight.Bold)
                    }

                    // Add to Cart Button
                    OutlinedButton(
                        onClick = {
                            viewModel.addToCart(product, quantity)
                            showAddedSnackbar = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("add_to_cart_button"),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TernivaGreen),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TernivaGreen)
                    ) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Keranjang", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Buy Now Button
                    Button(
                        onClick = {
                            viewModel.addToCart(product, quantity)
                            val item = CartItem(product = product, quantity = quantity, isSelected = true)
                            viewModel.navigateTo(CurrentScreen.Checkout(listOf(item)))
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("buy_now_button"),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                    ) {
                        Text("Beli Sekarang", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F8F6))
        ) {
            // Visual Banner Area
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = product.name,
                                tint = TernivaGreen,
                                modifier = Modifier.size(54.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = TernivaGreen,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Kategori: ${product.category}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // Price & Title Card
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Price Row
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "Rp ${String.format("%,d", product.price)}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = TernivaGreen
                            )
                            Text(
                                text = " / ${product.unit}",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
                            )
                            if (product.discountPercent > 0) {
                                Surface(
                                    color = TernivaHarvestGold,
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text(
                                        text = "-${product.discountPercent}%",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        if (product.originalPrice != null) {
                            Text(
                                text = "Rp ${String.format("%,d", product.originalPrice)}",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = product.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFA000), modifier = Modifier.size(16.dp))
                            Text(" ${product.rating} | ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("${product.soldCount} Terjual | ", fontSize = 12.sp, color = Color.Gray)
                            Text("Stok: ${product.stock} ${product.unit}", fontSize = 12.sp, color = TernivaGreen, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Quantity selector
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Kuantitas Pembelian", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text("Min. pembelian: ${product.minOrder} ${product.unit}", fontSize = 11.sp, color = Color.Gray)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedIconButton(
                                onClick = { if (quantity > product.minOrder) quantity-- },
                                modifier = Modifier.size(32.dp),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "$quantity",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            OutlinedIconButton(
                                onClick = { if (quantity < product.stock) quantity++ },
                                modifier = Modifier.size(32.dp),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Supplier Profile Card
            item {
                val store = remember(product.supplierName) { viewModel.getStoreProfile(product.supplierName, product.location) }
                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(TernivaGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Store, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = product.supplierName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (product.isSupplierVerified) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            Icons.Default.Verified,
                                            contentDescription = "Verified",
                                            tint = TernivaGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "🟢 ${store.lastOnline} • 📍 ${product.location}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF16A34A),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    viewModel.navigateTo(
                                        CurrentScreen.StoreProfile(
                                            supplierName = product.supplierName,
                                            location = product.location,
                                            isVerified = product.isSupplierVerified
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TernivaGreen),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TernivaGreen),
                                modifier = Modifier.testTag("btn_kunjungi_toko")
                            ) {
                                Text("Kunjungi Toko", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Toko Reputation Metrics Row (Shopee style store stats)
                        Surface(
                            color = Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Penilaian", fontSize = 10.sp, color = Color.Gray)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(12.dp))
                                        Text(" ${store.rating} (${store.ratingCount})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                                    }
                                }
                                Divider(modifier = Modifier.height(20.dp).width(1.dp), color = Color(0xFFE2E8F0))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Performa Chat", fontSize = 10.sp, color = Color.Gray)
                                    Text(store.chatResponseRate, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                }
                                Divider(modifier = Modifier.height(20.dp).width(1.dp), color = Color(0xFFE2E8F0))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Kecepatan Kirim", fontSize = 10.sp, color = Color.Gray)
                                    Text(store.deliverySpeed, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                }
                            }
                        }
                    }
                }
            }

            // Specifications Table
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Spesifikasi Produk", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        product.specifications.forEach { (key, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = key,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.width(130.dp)
                                )
                                Text(
                                    text = value,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E293B)
                                )
                            }
                            Divider(color = Color(0xFFF1F5F9))
                        }
                    }
                }
            }

            // Description
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Deskripsi Lengkap", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = product.description,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = Color(0xFF334155)
                        )
                    }
                }
            }

            // Delivery Assurance
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Pengiriman & Jaminan Mutu", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Dukungan Ekspedisi Khusus Komoditas Tani & Cold-Chain", fontSize = 12.sp, color = Color(0xFF334155))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Jaminan Uang Kembali jika produk susut/rusak saat tiba", fontSize = 12.sp, color = Color(0xFF334155))
                        }
                    }
                }
            }

            // Penilaian & Ulasan Produk (Komentar Pembeli & Rate Toko)
            item {
                val reviews = remember(product.id) { viewModel.getReviewsForProduct(product.id) }
                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Penilaian & Ulasan Produk", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                                    Text(" ${product.rating} / 5.0", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E293B))
                                    Text(" (128 ulasan pembeli)", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                            Surface(
                                color = TernivaGreen.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "99% Pembeli Puas",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TernivaGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Review Cards
                        reviews.forEachIndexed { index, review ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(TernivaGreen.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = review.userName.take(1),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TernivaGreen
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = review.userName,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1E293B)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                Icons.Default.Verified,
                                                contentDescription = "Pembeli Terverifikasi",
                                                tint = TernivaGreen,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                        Text(
                                            text = "${review.userRole} • ${review.date}",
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    // Stars
                                    Row {
                                        repeat(review.rating) {
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color(0xFFF59E0B),
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Surface(
                                    color = Color(0xFFF1F5F9),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "Varian: ${review.purchasedVariant}",
                                        fontSize = 10.sp,
                                        color = Color(0xFF475569),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = review.comment,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = Color(0xFF334155)
                                )

                                // Seller Response if present
                                if (review.supplierReply != null) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Surface(
                                        color = Color(0xFFF0FDF4),
                                        shape = RoundedCornerShape(6.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCFCE7)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = "Respon Penjual (${product.supplierName}):",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TernivaGreen
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = review.supplierReply,
                                                fontSize = 11.sp,
                                                color = Color(0xFF166534)
                                            )
                                        }
                                    }
                                }

                                if (index < reviews.size - 1) {
                                    Divider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = Color(0xFFF1F5F9)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
