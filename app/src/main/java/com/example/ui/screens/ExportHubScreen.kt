package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExportListing
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportHubScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Peluang Ekspor, 1: Pusat Dokumen Ekspor
    val exportListings by viewModel.exportListings.collectAsState()
    var showOfferDialogFor by remember { mutableStateOf<ExportListing?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pusat Perdagangan Ekspor Agritech", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
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
            Surface(color = Color.White, shadowElevation = 2.dp) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = TernivaGreen
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Peluang Ekspor 🌐", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Dokumen & Karantina 📑", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            if (selectedTab == 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = TernivaGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Public, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "International Trade & Direct Export",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Menghubungkan kelompok tani, koperasi komoditas, dan eksportir Indonesia dengan buyer mancanegara.",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }

                    items(exportListings) { listing ->
                        ExportListingCard(
                            listing = listing,
                            onOfferClick = { showOfferDialogFor = listing }
                        )
                    }
                }
            } else {
                // Tab 1: Export Documentation Center
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Pendampingan Dokumen Legalitas Ekspor", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                                    Text("Terniva menyediakan fasilitas kurasi dokumen karantina, fumigasi, dan sertifikasi negara tujuan.", fontSize = 11.sp, color = Color(0xFF166534))
                                }
                            }
                        }
                    }

                    val exportDocs = listOf(
                        Triple("1. Phytosanitary Certificate (PC)", "Dikeluarkan Badan Karantina Pertanian RI. Wajib untuk seluruh produk nabati segar/kering bebas dari OPTK (Organisme Pengganggu Tumbuhan Karantina).", "Karantina RI"),
                        Triple("2. Certificate of Origin (COO / SKA)", "Surat Keterangan Asal Form D/E/AK dari Kementerian Perdagangan RI untuk mendapatkan fasilitas tarif bea masuk 0% di negara tujuan.", "Kemendag"),
                        Triple("3. Sertifikasi Halal BPJPH", "Wajib untuk komoditas ekspor tujuan Timur Tengah (Arab Saudi, UAE, Qatar) dan pasar Muslim global.", "BPJPH RI"),
                        Triple("4. Certificate of Analysis (CoA) & HACCP", "Hasil uji laboratorium independen (residu pestisida, aflatoksin, kadar air, Salmonella) dan standar Hazard Analysis Critical Control Point.", "Lab Terakreditasi"),
                        Triple("5. Fumigasi & Pest-Control Container", "Perlakuan Phosphine (PH3) atau Methyl Bromide pada kontainer pengapalan berstandar AFAS (Australian Fumigation Accreditation Scheme).", "Balai Fumigasi"),
                        Triple("6. Bill of Lading (B/L) & Packing List", "Dokumen bukti muat kapal/pesawat logistik internasional beserta rincian berat kotor/bersih tiap palet komoditas.", "Shipping Line")
                    )

                    items(exportDocs) { (title, desc, authority) ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                    Surface(
                                        color = TernivaGreen.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(text = authority, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TernivaGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = desc, fontSize = 11.sp, lineHeight = 15.sp, color = Color(0xFF475569))
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal: Submit Export Offer
    if (showOfferDialogFor != null) {
        val listing = showOfferDialogFor!!
        var offerKg by remember { mutableStateOf("5000") }
        var offerPriceUsd by remember { mutableStateOf("4.5") }
        var incoterm by remember { mutableStateOf("FOB Port Tanjung Priok") }
        var isSubmitted by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showOfferDialogFor = null },
            title = { Text("Ajukan Penawaran Ekspor", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Peluang: ${listing.commodityName}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Tujuan: ${listing.destinationCountry} • Port: ${listing.portDestination}", fontSize = 11.sp, color = Color.Gray)

                    if (!isSubmitted) {
                        OutlinedTextField(
                            value = offerKg,
                            onValueChange = { offerKg = it },
                            label = { Text("Kapasitas Volume Siap Ekspor (Kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = offerPriceUsd,
                            onValueChange = { offerPriceUsd = it },
                            label = { Text("Harga Penawaran (USD / Kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = incoterm,
                            onValueChange = { incoterm = it },
                            label = { Text("Syarat Pengiriman (Incoterm)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✅ Penawaran ekspor berhasil diajukan kepada perwakilan buyer di ${listing.destinationCountry}. Tim Terniva Trade Desk akan memfasilitasi negosiasi & LC.",
                                fontSize = 12.sp,
                                color = Color(0xFF166534),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (!isSubmitted) {
                    Button(
                        onClick = { isSubmitted = true },
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                    ) {
                        Text("Kirim Penawaran Ekspor")
                    }
                } else {
                    Button(
                        onClick = { showOfferDialogFor = null },
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                    ) {
                        Text("Selesai")
                    }
                }
            },
            dismissButton = {
                if (!isSubmitted) {
                    TextButton(onClick = { showOfferDialogFor = null }) {
                        Text("Batal")
                    }
                }
            }
        )
    }
}

@Composable
fun ExportListingCard(
    listing: ExportListing,
    onOfferClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = TernivaGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "🌏 TUJUAN: ${listing.destinationCountry.uppercase()}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TernivaGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(text = "Incoterms: ${listing.incoterms}", fontSize = 10.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${listing.flagEmoji} ${listing.commodityName}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Kebutuhan: ${listing.volume} ${listing.unit} • Port: ${listing.portDestination}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TernivaGreen
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Grade: ${listing.qualityGrade} • Sertifikasi: ${listing.certification}",
                fontSize = 10.sp,
                color = Color(0xFF475569)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onOfferClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ajukan Penawaran Ekspor", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
