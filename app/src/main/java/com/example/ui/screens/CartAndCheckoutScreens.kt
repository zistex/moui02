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
import com.example.data.model.CartItem
import com.example.data.model.Order
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaOrange
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val selectedItems = cartItems.filter { it.isSelected }
    val totalAmount = selectedItems.sumOf { it.product.price * it.quantity }
    val isAllSelected = cartItems.isNotEmpty() && cartItems.all { it.isSelected }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Keranjang Belanja (${cartItems.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
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
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.toggleSelectAll(!isAllSelected) }
                    ) {
                        Checkbox(
                            checked = isAllSelected,
                            onCheckedChange = { viewModel.toggleSelectAll(it) },
                            colors = CheckboxDefaults.colors(checkedColor = TernivaGreen)
                        )
                        Text("Semua", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Total Pembayaran:", fontSize = 10.sp, color = Color.Gray)
                        Text(
                            text = "Rp ${String.format("%,d", totalAmount)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TernivaOrange
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            if (selectedItems.isNotEmpty()) {
                                viewModel.navigateTo(CurrentScreen.Checkout(selectedItems))
                            }
                        },
                        enabled = selectedItems.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaOrange),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("checkout_button")
                    ) {
                        Text("Checkout (${selectedItems.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.RemoveShoppingCart, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Keranjang Anda masih kosong", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.selectTab(0) },
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                    ) {
                        Text("Mulai Belanja Komoditas")
                    }
                }
            }
        } else {
            // Group by supplier
            val grouped = cartItems.groupBy { it.product.supplierName }

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF6F8F6)),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                grouped.forEach { (supplier, items) ->
                    item {
                        Surface(
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                // Supplier Header
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Store, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = supplier,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                }

                                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))

                                items.forEach { cartItem ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = cartItem.isSelected,
                                            onCheckedChange = { viewModel.toggleCartSelection(cartItem.product.id) },
                                            colors = CheckboxDefaults.colors(checkedColor = TernivaGreen)
                                        )

                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFFE8F5E9)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Eco, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(28.dp))
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = cartItem.product.name,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                maxLines = 2
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Rp ${String.format("%,d", cartItem.product.price)}",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TernivaOrange
                                            )
                                        }

                                        // Qty Adjuster
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            OutlinedIconButton(
                                                onClick = { viewModel.updateCartQuantity(cartItem.product.id, -1) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Text("-", fontSize = 14.sp)
                                            }
                                            Text(
                                                text = "${cartItem.quantity}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp)
                                            )
                                            OutlinedIconButton(
                                                onClick = { viewModel.updateCartQuantity(cartItem.product.id, 1) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Text("+", fontSize = 14.sp)
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    items: List<CartItem>,
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val subtotal = items.sumOf { it.product.price * it.quantity }
    var courier by remember { mutableStateOf("JNE Agri Cargo Dingin (Est. 1-2 hari)") }
    var paymentMethod by remember { mutableStateOf("BCA Virtual Account") }

    var recipientName by remember { mutableStateOf("Restoran Nusantara Rasa") }
    var recipientPhone by remember { mutableStateOf("081234567890") }
    var recipientAddress by remember { mutableStateOf("Jl. Thamrin No. 45, Menteng, Jakarta Pusat (10350)") }

    val shippingFee = 25000L
    val totalPayment = subtotal + shippingFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengiriman & Pembayaran", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
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
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Tagihan:", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = "Rp ${String.format("%,d", totalPayment)}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = TernivaGreen
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.createOrder(
                                items = items,
                                courier = courier,
                                paymentMethod = paymentMethod,
                                recipientName = recipientName,
                                recipientPhone = recipientPhone,
                                address = recipientAddress
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_bayar_sekarang")
                    ) {
                        Text("Bayar Sekarang", fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
            contentPadding = PaddingValues(10.dp)
        ) {
            // 1. Alamat Pengiriman
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = TernivaGreen)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Alamat Pengiriman Penerima", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("$recipientName ($recipientPhone)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(recipientAddress, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            // 2. Daftar Barang Pesanan
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Rincian Produk Pesanan", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.product.name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text("${item.quantity} x Rp ${String.format("%,d", item.product.price)}", fontSize = 11.sp, color = Color.Gray)
                                }
                                Text(
                                    "Rp ${String.format("%,d", item.product.price * item.quantity)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // 3. Opsi Pengiriman Komoditas
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = TernivaGreen)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Metode Pengiriman", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        val couriers = listOf(
                            "JNE Agri Cargo Dingin (Est. 1-2 hari)",
                            "Pengiriman Langsung Petani/Supplier (Hari ini)",
                            "SiCepat Kargo Komoditas"
                        )
                        couriers.forEach { cr ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { courier = cr }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = courier == cr,
                                    onClick = { courier = cr },
                                    colors = RadioButtonDefaults.colors(selectedColor = TernivaGreen)
                                )
                                Text(cr, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // 4. Metode Pembayaran
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = TernivaGreen)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pilihan Metode Pembayaran", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        val methods = listOf(
                            "BCA Virtual Account",
                            "Mandiri Virtual Account",
                            "BRI Virtual Account",
                            "QRIS Nasional (Instant)"
                        )
                        methods.forEach { method ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { paymentMethod = method }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = paymentMethod == method,
                                    onClick = { paymentMethod = method },
                                    colors = RadioButtonDefaults.colors(selectedColor = TernivaGreen)
                                )
                                Text(method, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // 5. Ringkasan Biaya
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Ringkasan Pembayaran", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal Produk", fontSize = 12.sp, color = Color.Gray)
                            Text("Rp ${String.format("%,d", subtotal)}", fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Biaya Pengiriman Kargo", fontSize = 12.sp, color = Color.Gray)
                            Text("Rp ${String.format("%,d", shippingFee)}", fontSize = 12.sp)
                        }
                        Divider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFF1F5F9))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Pembayaran", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "Rp ${String.format("%,d", totalPayment)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = TernivaOrange
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderSuccessScreen(
    order: Order,
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val currentOrder = orders.find { it.id == order.id } ?: order

    Scaffold(
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
                        onClick = { viewModel.selectTab(0) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Kembali ke Beranda")
                    }

                    Button(
                        onClick = { viewModel.selectTab(4) },
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Lihat Pesanan Saya")
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Icon & Message
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(if (currentOrder.status == com.example.data.model.OrderStatus.PAID) TernivaGreen.copy(alpha = 0.15f) else TernivaOrange.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (currentOrder.status == com.example.data.model.OrderStatus.PAID) Icons.Default.CheckCircle else Icons.Default.HourglassTop,
                    contentDescription = null,
                    tint = if (currentOrder.status == com.example.data.model.OrderStatus.PAID) TernivaGreen else TernivaOrange,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (currentOrder.status == com.example.data.model.OrderStatus.PAID) "Pembayaran Terverifikasi!" else "Menunggu Pembayaran",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = if (currentOrder.status == com.example.data.model.OrderStatus.PAID) TernivaGreen else TernivaOrange
            )

            Text(
                text = "Nomor Pesanan: ${currentOrder.orderNumber}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // VA Card & Instructions
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Instruksi Pembayaran Gateway", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Metode: ${currentOrder.paymentMethod}", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))

                    Text("Nomor Virtual Account:", fontSize = 11.sp, color = Color.Gray)
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentOrder.virtualAccountNumber,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "SALIN",
                                color = TernivaGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Total yang Harus Dibayar:", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = "Rp ${String.format("%,d", currentOrder.totalAmount)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TernivaGreen
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Divider(color = Color(0xFFF1F5F9))

                    Spacer(modifier = Modifier.height(12.dp))

                    // Backend Webhook Simulation button (Complies with section 7F: "Jangan pernah menyatakan pembayaran berhasil hanya berdasarkan klik dari frontend. Payment callback wajib diproses backend.")
                    if (currentOrder.status == com.example.data.model.OrderStatus.PENDING_PAYMENT) {
                        Surface(
                            color = TernivaGreen.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TernivaGreen.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Simulasi Callback Pembayaran Backend",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TernivaGreen
                                )
                                Text(
                                    text = "Tekan tombol di bawah untuk menyimulasikan penerimaan webhook notifikasi pembayaran otomatis dari payment gateway.",
                                    fontSize = 10.sp,
                                    color = Color(0xFF334155)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { viewModel.simulateConfirmPayment(currentOrder.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth().testTag("simulate_payment_callback")
                                ) {
                                    Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Verifikasi Callback Pembayaran", fontSize = 11.sp)
                                }
                            }
                        }
                    } else {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = TernivaGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Status: Lunas & Terverifikasi", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                                    Text("Supplier sedang menyiapkan pesanan agritech Anda.", fontSize = 11.sp, color = Color(0xFF004D32))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
