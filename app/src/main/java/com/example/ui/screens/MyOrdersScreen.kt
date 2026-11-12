package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(
    initialStatus: String = "Semua",
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Semua", "Belum Bayar", "Dikemas", "Dikirim", "Selesai", "Dibatalkan")
    var selectedTabIndex by remember {
        val idx = tabs.indexOf(initialStatus)
        mutableStateOf(if (idx >= 0) idx else 0)
    }

    val orders by viewModel.orders.collectAsState()

    val filteredOrders = remember(orders, selectedTabIndex) {
        when (tabs[selectedTabIndex]) {
            "Belum Bayar" -> orders.filter { it.status == OrderStatus.PENDING_PAYMENT }
            "Dikemas" -> orders.filter { it.status == OrderStatus.PAID }
            "Dikirim" -> orders.filter { it.status == OrderStatus.SHIPPED }
            "Selesai" -> orders.filter { it.status == OrderStatus.DELIVERED }
            "Dibatalkan" -> orders.filter { it.status == OrderStatus.CANCELLED }
            else -> orders
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pesanan Saya", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F8F6))
        ) {
            // Shopee style horizontal status tab bar
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = TernivaGreen,
                edgePadding = 12.dp
            ) {
                tabs.forEachIndexed { index, tabTitle ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = tabTitle,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            if (filteredOrders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Belum Ada Pesanan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                        Text(
                            text = "Tidak ada pesanan dalam kategori '${tabs[selectedTabIndex]}'.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.selectTab(0) },
                            colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Belanja Komoditas Tani")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredOrders) { order ->
                        OrderCardItem(order = order, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCardItem(
    order: Order,
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header: Store Name & Order Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Store, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mitra Tani & Supplier",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }

                Surface(
                    color = when (order.status) {
                        OrderStatus.PENDING_PAYMENT -> TernivaHarvestGold.copy(alpha = 0.12f)
                        OrderStatus.PAID, OrderStatus.PROCESSING -> TernivaGreen.copy(alpha = 0.12f)
                        OrderStatus.SHIPPED -> Color(0xFF0284C7).copy(alpha = 0.12f)
                        OrderStatus.DELIVERED, OrderStatus.COMPLETED -> TernivaGreen.copy(alpha = 0.12f)
                        OrderStatus.CANCELLED -> Color.Red.copy(alpha = 0.12f)
                        else -> TernivaGreen.copy(alpha = 0.12f)
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = when (order.status) {
                            OrderStatus.PENDING_PAYMENT -> "Menunggu Pembayaran"
                            OrderStatus.PAID -> "Sedang Dikemas"
                            OrderStatus.PROCESSING -> "Diproses"
                            OrderStatus.SHIPPED -> "Sedang Dikirim"
                            OrderStatus.DELIVERED, OrderStatus.COMPLETED -> "Selesai"
                            OrderStatus.CANCELLED -> "Dibatalkan"
                            else -> order.status.label
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (order.status) {
                            OrderStatus.PENDING_PAYMENT -> TernivaHarvestGold
                            OrderStatus.PAID, OrderStatus.PROCESSING -> TernivaGreen
                            OrderStatus.SHIPPED -> Color(0xFF0284C7)
                            OrderStatus.DELIVERED, OrderStatus.COMPLETED -> TernivaGreen
                            OrderStatus.CANCELLED -> Color.Red
                            else -> TernivaGreen
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))

            // Products list snippet
            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Eco, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(22.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.product.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                        Text(
                            text = "${item.quantity} ${item.product.unit} x Rp ${String.format("%,d", item.product.price)}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total Amount Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "No. Pesanan: ${order.orderNumber}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Total: ", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = "Rp ${String.format("%,d", order.totalAmount)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = TernivaGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons based on status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.navigateTo(CurrentScreen.ChatRoom("Supplier")) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    Text("Hubungi Toko", fontSize = 11.sp)
                }

                when (order.status) {
                    OrderStatus.PENDING_PAYMENT -> {
                        Button(
                            onClick = { viewModel.navigateTo(CurrentScreen.OrderSuccess(order)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            Text("Bayar Sekarang", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    OrderStatus.SHIPPED, OrderStatus.PAID, OrderStatus.PROCESSING -> {
                        Button(
                            onClick = { viewModel.navigateTo(CurrentScreen.OrderTracking(order)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            Text("Lacak Pesanan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    OrderStatus.DELIVERED, OrderStatus.COMPLETED -> {
                        Button(
                            onClick = { viewModel.navigateTo(CurrentScreen.OrderTracking(order)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            Text("Rincian & Bukti", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    else -> {
                        Button(
                            onClick = { viewModel.selectTab(0) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            Text("Beli Lagi", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    order: Order,
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val liveOrder = orders.find { it.id == order.id } ?: order
    var isConfirmedDelivered by remember { mutableStateOf(liveOrder.status == OrderStatus.DELIVERED) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lacak Pengiriman", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (liveOrder.status != OrderStatus.DELIVERED && !isConfirmedDelivered) {
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
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.confirmOrderReceived(liveOrder.id)
                                isConfirmedDelivered = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Konfirmasi Pesanan Diterima", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F8F6)),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Courier & Resi Info
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(TernivaGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.LocalShipping, contentDescription = null, tint = TernivaGreen)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(liveOrder.courierName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("No. Resi: SC-AGRI-${liveOrder.orderNumber.takeLast(6)}", fontSize = 11.sp, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            color = Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Estimasi Tiba: Besok Pagi (Sebelum 12:00)", fontSize = 11.sp, color = Color(0xFF334155))
                                Text("Tepat Waktu", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                            }
                        }
                    }
                }
            }

            // Timeline Steps
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Status Perjalanan Paket", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(14.dp))

                        val checkpoints = listOf(
                            Triple("Dalam Perjalanan ke Alamat Penerima", "Kurir sedang menuju ke alamat tujuan pengantaran.", "Hari ini, 14:20"),
                            Triple("Tiba di Hub Transit Jakarta Timur", "Paket telah disortir di gudang logistik cold-chain.", "Hari ini, 09:15"),
                            Triple("Paket Diserahkan ke Ekspedisi", "Supplier menyerahkan pesanan kepada armada kargo khusus pertanian.", "Kemarin, 17:40"),
                            Triple("Pembayaran Terverifikasi & Dikemas", "Pesanan telah dikemas dengan insulasi pengaman komoditas.", "Kemarin, 11:00")
                        )

                        checkpoints.forEachIndexed { index, (title, desc, time) ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(if (index == 0) TernivaGreen else Color(0xFFCBD5E1))
                                    )
                                    if (index < checkpoints.size - 1) {
                                        Box(
                                            modifier = Modifier
                                                .width(2.dp)
                                                .height(46.dp)
                                                .background(Color(0xFFE2E8F0))
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = title, fontSize = 12.sp, fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Medium, color = if (index == 0) TernivaGreen else Color(0xFF1E293B))
                                    Text(text = desc, fontSize = 11.sp, color = Color.Gray)
                                    Text(text = time, fontSize = 10.sp, color = Color(0xFF94A3B8), modifier = Modifier.padding(top = 2.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Recipient & Address
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = TernivaGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Alamat Pengiriman", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "${liveOrder.recipientName} (${liveOrder.recipientPhone})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = liveOrder.recipientAddress, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
        }
    }
}
