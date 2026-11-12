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
import com.example.data.model.BuyerRequest
import com.example.data.model.ExportListing
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaOrange
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeluangScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Peluang Ekspor, 1: Permintaan Buyer Horeka
    val exportListings by viewModel.exportListings.collectAsState()
    val exportOffers by viewModel.exportOffers.collectAsState()
    val buyerRequests by viewModel.buyerRequests.collectAsState()

    var showOfferDialogFor by remember { mutableStateOf<ExportListing?>(null) }
    var showNewRequestDialog by remember { mutableStateOf(false) }
    var showDocCenterDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8F6))
    ) {
        // Tab Header
        Surface(color = Color.White, shadowElevation = 2.dp) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = TernivaGreen
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Peluang Ekspor 🌐", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Permintaan Buyer 📋", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (selectedTab == 0) {
                // Section 9: Ekspor
                item {
                    // Export Quick Actions Banner
                    Card(
                        colors = CardDefaults.cardColors(containerColor = TernivaGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Public, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "International Trade & Export Hub",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Menghubungkan supplier agritech Indonesia langsung dengan importir & buyer mancanegara.",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = { showDocCenterDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("btn_export_doc_center")
                            ) {
                                Icon(Icons.Default.FolderSpecial, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pusat Dokumen Ekspor (COO, Phyto, BL)", fontSize = 11.sp, color = TernivaGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Listing Permintaan Ekspor Aktif",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                items(exportListings) { listing ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Country destination & commodity
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(listing.flagEmoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = listing.destinationCountry,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF334155)
                                    )
                                }
                                Surface(
                                    color = TernivaGreen.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = listing.status,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TernivaGreen,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = listing.commodityName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Specs Grid
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8FAFC), RoundedCornerShape(6.dp))
                                    .padding(8.dp)
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Volume Kebutuhan:", fontSize = 11.sp, color = Color.Gray)
                                    Text("${listing.volume} ${listing.unit}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Incoterms & Port:", fontSize = 11.sp, color = Color.Gray)
                                    Text(listing.incoterms, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Sertifikasi:", fontSize = 11.sp, color = Color.Gray)
                                    Text(listing.certification, fontSize = 10.sp, color = TernivaGreen, fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Jadwal Pengiriman:", fontSize = 11.sp, color = Color.Gray)
                                    Text(listing.deliverySchedule, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Deadline: ${listing.deadline}",
                                    fontSize = 10.sp,
                                    color = Color(0xFFE11D48),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Button(
                                    onClick = { showOfferDialogFor = listing },
                                    colors = ButtonDefaults.buttonColors(containerColor = TernivaOrange),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("btn_ajukan_penawaran_ekspor_${listing.id}")
                                ) {
                                    Text("Ajukan Pasokan Ekspor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            } else {
                // Section 8: Buyer Requests
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Permintaan Pasokan Komoditas", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Dari Hotel, Restoran, Kafe, dan Industri Pangan", fontSize = 11.sp, color = Color.Gray)
                        }

                        Button(
                            onClick = { showNewRequestDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("btn_buat_permintaan_buyer")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Buat Permintaan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                items(buyerRequests) { request ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = Color(0xFFE0F2FE),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = request.buyerRole,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0369A1),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text("Tenggat: ${request.deadline}", fontSize = 10.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(request.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Pemesan: ${request.buyerName} • 📍 ${request.location}", fontSize = 11.sp, color = Color.Gray)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(request.description, fontSize = 11.sp, color = Color(0xFF334155))

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = Color(0xFFF1F5F9))
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Volume: ${request.quantity} ${request.unit}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Target Harga: ${request.targetPrice}", fontSize = 11.sp, color = TernivaOrange, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { },
                                    colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("Kirim Penawaran (${request.offersCount} masuk)", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialog: Ajukan Pasokan Ekspor (Section 9B)
    if (showOfferDialogFor != null) {
        val listing = showOfferDialogFor!!
        var offerVolume by remember { mutableStateOf(listing.volume) }
        var offerPrice by remember { mutableStateOf("7.50") }
        var originPort by remember { mutableStateOf("Pelabuhan Tanjung Perak Surabaya") }
        var supplierName by remember { mutableStateOf("Koperasi Petani Nusantara Mandiri") }

        AlertDialog(
            onDismissRequest = { showOfferDialogFor = null },
            title = { Text("Ajukan Penawaran Ekspor", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Komoditas: ${listing.commodityName}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Tujuan: ${listing.destinationCountry}", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = supplierName,
                        onValueChange = { supplierName = it },
                        label = { Text("Nama Usaha / Koperasi Supplier") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = offerVolume,
                        onValueChange = { offerVolume = it },
                        label = { Text("Kapasitas Pasokan (${listing.unit})") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = offerPrice,
                        onValueChange = { offerPrice = it },
                        label = { Text("Harga Penawaran (USD / Kg)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = originPort,
                        onValueChange = { originPort = it },
                        label = { Text("Pelabuhan Asal Muat") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitExportOffer(
                            listingId = listing.id,
                            commodity = listing.commodityName,
                            supplierName = supplierName,
                            volume = "$offerVolume ${listing.unit}",
                            priceUsd = "$offerPrice USD/Kg",
                            originPort = originPort
                        )
                        showOfferDialogFor = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TernivaOrange)
                ) {
                    Text("Kirim Penawaran Ekspor")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOfferDialogFor = null }) {
                    Text("Batal")
                }
            }
        )
    }

    // Modal Dialog: Buat Permintaan Buyer Baru (Section 8)
    if (showNewRequestDialog) {
        var reqTitle by remember { mutableStateOf("Pasokan Rutin Sayur Organik Harian") }
        var reqCategory by remember { mutableStateOf("Buah dan Sayuran") }
        var reqBuyerName by remember { mutableStateOf("Cafe & Resto Hijau Daun") }
        var reqBuyerRole by remember { mutableStateOf("Restaurant & Cafe") }
        var reqQuantity by remember { mutableStateOf("150") }
        var reqUnit by remember { mutableStateOf("Kg / Minggu") }
        var reqTargetPrice by remember { mutableStateOf("Rp 28.000 / Kg") }
        var reqLocation by remember { mutableStateOf("Bandung") }
        var reqDeadline by remember { mutableStateOf("30 Nov 2026") }
        var reqDesc by remember { mutableStateOf("Dibutuhkan selada keriting, timun jepang dan tomat hidroponik.") }

        AlertDialog(
            onDismissRequest = { showNewRequestDialog = false },
            title = { Text("Buat Permintaan Barang Baru", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = reqTitle,
                        onValueChange = { reqTitle = it },
                        label = { Text("Judul Kebutuhan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = reqBuyerName,
                        onValueChange = { reqBuyerName = it },
                        label = { Text("Nama Bisnis / Hotel / Resto") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = reqQuantity,
                            onValueChange = { reqQuantity = it },
                            label = { Text("Kuantitas") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = reqUnit,
                            onValueChange = { reqUnit = it },
                            label = { Text("Satuan") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = reqTargetPrice,
                        onValueChange = { reqTargetPrice = it },
                        label = { Text("Target Harga") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = reqDesc,
                        onValueChange = { reqDesc = it },
                        label = { Text("Spesifikasi Kualitas") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createBuyerRequest(
                            title = reqTitle,
                            category = reqCategory,
                            buyerName = reqBuyerName,
                            buyerRole = reqBuyerRole,
                            quantity = reqQuantity,
                            unit = reqUnit,
                            targetPrice = reqTargetPrice,
                            location = reqLocation,
                            deadline = reqDeadline,
                            description = reqDesc
                        )
                        showNewRequestDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                ) {
                    Text("Publikasikan Permintaan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewRequestDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Modal Dialog: Pusat Dokumen Ekspor (Section 9E)
    if (showDocCenterDialog) {
        val docs = listOf(
            "Proforma Invoice & Sales Contract" to "Terverifikasi",
            "Packing List & Commercial Invoice" to "Siap Cetak",
            "Certificate of Origin (SKA / Form D/E)" to "Dalam Proses",
            "Phytosanitary Certificate (Karantina Pertanian)" to "Terbit",
            "Bill of Lading / Air Waybill" to "Menunggu Vessel Loading",
            "Pemberitahuan Ekspor Barang (PEB Bea Cukai)" to "Telah Diajukan"
        )

        AlertDialog(
            onDismissRequest = { showDocCenterDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = TernivaGreen)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Checklist Dokumen Ekspor", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Dokumen resmi disesuaikan komoditas ekspor dan regulasi kepabeanan negara tujuan.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    docs.forEach { (doc, status) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(doc, fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            Surface(
                                color = if (status == "Terbit" || status == "Terverifikasi") Color(0xFFE8F5E9) else Color(0xFFFFF7ED),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = status,
                                    fontSize = 9.sp,
                                    color = if (status == "Terbit" || status == "Terverifikasi") TernivaGreen else TernivaOrange,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Divider(color = Color(0xFFF1F5F9))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDocCenterDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                ) {
                    Text("Tutup Dokumen")
                }
            }
        )
    }
}
