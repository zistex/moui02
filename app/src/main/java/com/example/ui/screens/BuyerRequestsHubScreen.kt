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
import com.example.data.model.BuyerRequest
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerRequestsHubScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val buyerRequests by viewModel.buyerRequests.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var showOfferDialogFor by remember { mutableStateOf<BuyerRequest?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf("Semua") }

    val filteredRequests = remember(buyerRequests, selectedCategoryFilter) {
        if (selectedCategoryFilter == "Semua") buyerRequests
        else buyerRequests.filter { it.category.contains(selectedCategoryFilter, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Permintaan Pasokan Buyer (Horeka)", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { showCreateDialog = true }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Buat Permintaan", tint = TernivaGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = TernivaGreen,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.PostAdd, contentDescription = null) },
                text = { Text("Buat Permintaan Baru", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                modifier = Modifier.testTag("fab_create_buyer_request")
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F8F6))
        ) {
            // Header Info Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RequestQuote, contentDescription = null, tint = TernivaHarvestGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Papan Pasokan Horeka & Industri",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Hotel, Restoran, Katering, dan Pabrik mempublikasikan kebutuhan pasokan rutin. Petani & Supplier lokal dapat langsung mengajukan penawaran harga partai.",
                        fontSize = 11.sp,
                        color = Color(0xFF475569),
                        lineHeight = 15.sp
                    )
                }
            }

            // Category Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Semua", "Pertanian", "Peternakan", "Perikanan").forEach { cat ->
                    val isSelected = selectedCategoryFilter == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryFilter = cat },
                        label = { Text(cat, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TernivaGreen,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredRequests) { request ->
                    BuyerRequestCard(
                        request = request,
                        onOfferClick = { showOfferDialogFor = request }
                    )
                }
            }
        }
    }

    // Modal Dialog: Create New Buyer Request
    if (showCreateDialog) {
        var komoditasTitle by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Pertanian") }
        var buyerName by remember { mutableStateOf("Resto & Katering Anda") }
        var quantity by remember { mutableStateOf("500") }
        var unit by remember { mutableStateOf("Kg") }
        var targetPrice by remember { mutableStateOf("15000") }
        var location by remember { mutableStateOf("Kota Bandung, Jawa Barat") }
        var notes by remember { mutableStateOf("Kualitas segar grade A, pengiriman setiap hari Senin & Kamis pagi.") }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Buat Permintaan Pasokan Baru", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = komoditasTitle,
                        onValueChange = { komoditasTitle = it },
                        label = { Text("Judul Komoditas Dibutuhkan") },
                        placeholder = { Text("Contoh: Tomat Cherry Segar / Beras") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Kebutuhan Volume") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = unit,
                            onValueChange = { unit = it },
                            label = { Text("Satuan (Kg/Ton)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = targetPrice,
                        onValueChange = { targetPrice = it },
                        label = { Text("Target Harga Beli (Rp / Satuan)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Lokasi Pengiriman / Gudang") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Spesifikasi & Standar Mutu") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (komoditasTitle.isNotBlank()) {
                            viewModel.addBuyerRequest(
                                title = komoditasTitle,
                                category = category,
                                buyerName = buyerName,
                                buyerRole = "Horeka / Bisnis Terverifikasi",
                                quantity = quantity,
                                unit = unit,
                                targetPrice = "Rp ${String.format("%,d", targetPrice.toLongOrNull() ?: 15000)} / $unit",
                                location = location,
                                deadline = "30 Hari Kedepan",
                                description = notes
                            )
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                ) {
                    Text("Publikasikan Permintaan", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Modal Dialog: Submit Offer to Buyer Request
    if (showOfferDialogFor != null) {
        val req = showOfferDialogFor!!
        var offerPrice by remember { mutableStateOf("") }
        var offerNotes by remember { mutableStateOf("Kami sanggup memenuhi volume dengan kualitas panen segar langsung dari kebun.") }
        var isSubmitted by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showOfferDialogFor = null },
            title = { Text("Ajukan Penawaran Pasokan", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Permintaan: ${req.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Buyer: ${req.buyerName} • Target: ${req.targetPrice}", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))

                    if (!isSubmitted) {
                        OutlinedTextField(
                            value = offerPrice,
                            onValueChange = { offerPrice = it },
                            label = { Text("Harga Penawaran Anda (Rp / ${req.unit})") },
                            placeholder = { Text("Contoh: 14500") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = offerNotes,
                            onValueChange = { offerNotes = it },
                            label = { Text("Catatan Kapasitas & Jadwal Kirim") },
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✅ Penawaran Anda berhasil dikirim ke ${req.buyerName}. Anda akan menerima notifikasi jika penawaran diterima.",
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
                        Text("Kirim Penawaran")
                    }
                } else {
                    Button(
                        onClick = { showOfferDialogFor = null },
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                    ) {
                        Text("Tutup")
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
fun BuyerRequestCard(
    request: BuyerRequest,
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
                        text = request.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TernivaGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "Batas: ${request.deadline}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = request.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Permintaan oleh: ${request.buyerName} (${request.buyerRole})",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Target Volume:", fontSize = 10.sp, color = Color.Gray)
                    Text("${request.quantity} ${request.unit}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                }
                Column {
                    Text("Target Harga:", fontSize = 10.sp, color = Color.Gray)
                    Text(request.targetPrice, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                }
                Column {
                    Text("Lokasi Kirim:", fontSize = 10.sp, color = Color.Gray)
                    Text(request.location, fontSize = 11.sp, color = Color(0xFF1E293B))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = request.description,
                fontSize = 11.sp,
                color = Color(0xFF475569),
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💼 ${request.offersCount} Penawaran Masuk",
                    fontSize = 10.sp,
                    color = Color.Gray
                )

                Button(
                    onClick = onOfferClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("Ajukan Penawaran Pasokan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
