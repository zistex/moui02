package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaOrange
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

data class AccountMenuItem(
    val title: String,
    val icon: ImageVector,
    val badge: String? = null,
    val onClick: () -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val currentRole by viewModel.userRole.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val titipOrders by viewModel.titipOrders.collectAsState()

    var showSupplierRegDialog by remember { mutableStateOf(false) }
    var showBuyerRegDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8F6)),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Profile Header with Terniva Green
        item {
            Surface(
                color = TernivaGreen,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(36.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Budi Santoso (Nusantara Agri)", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("ID Anggota: TRN-ID-882190", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = "Peran Aktif: ${currentRole.label}",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Settings, contentDescription = "Pengaturan", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Saldo & Koin Strip (Shopee style wallet bar)
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("TernivaPay", fontSize = 10.sp, color = Color.Gray)
                                Text("Rp 4.250.000", fontSize = 13.sp, fontWeight = FontWeight.Black, color = TernivaGreen)
                            }
                            Divider(
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(1.dp),
                                color = Color(0xFFE2E8F0)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Koin Panen", fontSize = 10.sp, color = Color.Gray)
                                Text("1.450 Koin", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TernivaOrange)
                            }
                            Divider(
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(1.dp),
                                color = Color(0xFFE2E8F0)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Voucher Saya", fontSize = 10.sp, color = Color.Gray)
                                Text("5 Voucher", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 2. Multi-Role Switcher (Section 6C & Section 12)
        item {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Ganti Mode Akun / Multi-Role", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Satu akun dapat bertindak sebagai Pembeli, Petani/Supplier, atau Pengelola Program", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UserRole.values().forEach { role ->
                            val isSelected = currentRole == role
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.switchRole(role) },
                                label = { Text(role.label, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TernivaGreen,
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // 3. Status Pesanan Saya (Shopee order tracking bar)
        item {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(CurrentScreen.MyOrders("Semua")) },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Pesanan Saya", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Lihat Riwayat Pesanan >", fontSize = 11.sp, color = TernivaGreen, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        val pendingCount = orders.count { it.status == com.example.data.model.OrderStatus.PENDING_PAYMENT }
                        val shippedCount = orders.count { it.status == com.example.data.model.OrderStatus.SHIPPED }

                        OrderStatusItem("Belum Bayar", Icons.Default.AccountBalanceWallet, pendingCount) {
                            viewModel.navigateTo(CurrentScreen.MyOrders("Belum Bayar"))
                        }
                        OrderStatusItem("Dikemas", Icons.Default.Inventory2, 0) {
                            viewModel.navigateTo(CurrentScreen.MyOrders("Dikemas"))
                        }
                        OrderStatusItem("Dikirim", Icons.Default.LocalShipping, shippedCount) {
                            viewModel.navigateTo(CurrentScreen.MyOrders("Dikirim"))
                        }
                        OrderStatusItem("Beri Nilai", Icons.Default.RateReview, 0) {
                            viewModel.navigateTo(CurrentScreen.MyOrders("Selesai"))
                        }
                        OrderStatusItem("Pengembalian", Icons.Default.Replay, 0) {
                            viewModel.navigateTo(CurrentScreen.MyOrders("Dibatalkan"))
                        }
                    }
                }
            }
        }

        // 4. Dynamic Menu based on Active Role (Section 12)
        item {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = when (currentRole) {
                            UserRole.SUPPLIER -> "Menu Mitra Supplier & Petani"
                            UserRole.STEWARD -> "Menu Pengelola Pantau Panen"
                            UserRole.BUYER -> "Menu Pembeli & Horeka"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TernivaGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val roleMenus = when (currentRole) {
                        UserRole.SUPPLIER -> listOf(
                            AccountMenuItem("Toko Saya (Kelola Katalog Komoditas)", Icons.Default.Store),
                            AccountMenuItem("Pesanan Masuk dari Pembeli", Icons.Default.ReceiptLong, badge = "1 Baru"),
                            AccountMenuItem("Penawaran Ekspor Terkirim", Icons.Default.Public),
                            AccountMenuItem("Dokumen Legalitas & Mutu Usaha", Icons.Default.VerifiedUser),
                            AccountMenuItem("Kemitraan Petani Terniva", Icons.Default.Handshake)
                        )
                        UserRole.STEWARD -> listOf(
                            AccountMenuItem("Program Pantau Panen Saya", Icons.Default.Agriculture),
                            AccountMenuItem("Data Peserta Titip Tani / Ternak", Icons.Default.Group, badge = "${titipOrders.size} Peserta"),
                            AccountMenuItem("Input Monitoring & Laporan Mingguan", Icons.Default.EditNote),
                            AccountMenuItem("Kamera IoT Lahan & Kandang", Icons.Default.Videocam),
                            AccountMenuItem("Laporan Panen & Serah Terima Hasil", Icons.Default.FactCheck)
                        )
                        UserRole.BUYER -> listOf(
                            AccountMenuItem("Titip Tani & Ternak Saya (Pantau Panen)", Icons.Default.Visibility, badge = "${titipOrders.size} Aktif", onClick = {
                                if (titipOrders.isNotEmpty()) viewModel.navigateTo(CurrentScreen.ActiveMonitoring(titipOrders.first()))
                            }),
                            AccountMenuItem("Permintaan Pasokan Saya (Buyer Request)", Icons.Default.RequestQuote),
                            AccountMenuItem("Konsultasi Dokter Hewan & Tanaman Saya", Icons.Default.MedicalServices),
                            AccountMenuItem("Daftar Alamat Pengiriman", Icons.Default.LocationOn),
                            AccountMenuItem("Pusat Bantuan & Layanan Pelanggan", Icons.Default.HeadsetMic)
                        )
                    }

                    roleMenus.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { item.onClick() }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(item.icon, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(item.title, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            if (item.badge != null) {
                                Surface(
                                    color = TernivaOrange,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.padding(end = 6.dp)
                                ) {
                                    Text(item.badge, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                        Divider(color = Color(0xFFF1F5F9))
                    }
                }
            }
        }

        // 5. Registration CTA for Becoming a Verified Supplier or Corporate Buyer (Section 6)
        item {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Pendaftaran Mitra Resmi Terniva", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { showSupplierRegDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TernivaGreen),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TernivaGreen)
                    ) {
                        Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Daftar Sebagai Supplier / Pemasok Hasil Tani & Ternak", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedButton(
                        onClick = { showBuyerRegDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0284C7)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0284C7))
                    ) {
                        Icon(Icons.Default.Business, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Daftar Akun Buyer Terverifikasi (Horeka / Industri)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 6. Logout / Keluar Akun Button (Shopee Style)
        item {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Button(
                        onClick = { viewModel.logout() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_logout")
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Keluar dari Akun",
                            color = Color(0xFFDC2626),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Terniva v1.0.0 • Ekosistem Agritech Terpercaya",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

    // Modal Form: Pendaftaran Supplier (Section 6A)
    if (showSupplierRegDialog) {
        var sName by remember { mutableStateOf("Budi Santoso") }
        var sBizName by remember { mutableStateOf("Gapoktan Agro Makmur") }
        var sBizType by remember { mutableStateOf("Petani & Distributor Hasil Pertanian") }
        var sCommodity by remember { mutableStateOf("Beras Organik & Cabai Rawit") }
        var sCapacity by remember { mutableStateOf("20 Ton / Bulan") }
        var sLocation by remember { mutableStateOf("Kabupaten Subang, Jawa Barat") }

        AlertDialog(
            onDismissRequest = { showSupplierRegDialog = false },
            title = { Text("Pendaftaran Mitra Supplier", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(value = sName, onValueChange = { sName = it }, label = { Text("Nama Penanggung Jawab") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = sBizName, onValueChange = { sBizName = it }, label = { Text("Nama Usaha / Poktan") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = sBizType, onValueChange = { sBizType = it }, label = { Text("Jenis Usaha") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = sCommodity, onValueChange = { sCommodity = it }, label = { Text("Komoditas Unggulan") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = sCapacity, onValueChange = { sCapacity = it }, label = { Text("Kapasitas Pasokan") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = sLocation, onValueChange = { sLocation = it }, label = { Text("Alamat / Lokasi Kebun / Kandang") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.switchRole(UserRole.SUPPLIER)
                        showSupplierRegDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                ) {
                    Text("Kirim & Mulai Berjualan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSupplierRegDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Modal Form: Pendaftaran Buyer Horeka (Section 6B)
    if (showBuyerRegDialog) {
        var bName by remember { mutableStateOf("Restoran Dapur Sunda Asli") }
        var bCat by remember { mutableStateOf("Restaurant & Cafe") }
        var bContact by remember { mutableStateOf("081199887766") }
        var bLoc by remember { mutableStateOf("Jakarta Barat") }

        AlertDialog(
            onDismissRequest = { showBuyerRegDialog = false },
            title = { Text("Pendaftaran Akun Buyer Terverifikasi", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(value = bName, onValueChange = { bName = it }, label = { Text("Nama Usaha / Perusahaan") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = bCat, onValueChange = { bCat = it }, label = { Text("Kategori (Hotel/Resto/Industri)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = bContact, onValueChange = { bContact = it }, label = { Text("Kontak Pengadaan / Purchasing") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = bLoc, onValueChange = { bLoc = it }, label = { Text("Alamat Pengiriman Utama") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = { showBuyerRegDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                ) {
                    Text("Kirim Verifikasi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBuyerRegDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun OrderStatusItem(
    title: String,
    icon: ImageVector,
    badgeCount: Int = 0,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF475569),
                modifier = Modifier.size(24.dp)
            )
            if (badgeCount > 0) {
                Surface(
                    color = TernivaHarvestGold,
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-4).dp)
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 10.sp, color = Color(0xFF334155))
    }
}
