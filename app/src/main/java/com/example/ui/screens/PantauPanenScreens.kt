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
import com.example.data.model.PantauPanenProgram
import com.example.data.model.ProgramType
import com.example.data.model.TitipOrder
import com.example.ui.components.PantauPanenCard
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaOrange
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantauPanenProgramDetailScreen(
    program: PantauPanenProgram,
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSlots by remember { mutableStateOf(1) }
    val totalCost = program.feePerUnit * selectedSlots

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(program.title, fontSize = 15.sp, maxLines = 1) },
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
                        Text("Total Titip ($selectedSlots Slot):", fontSize = 10.sp, color = Color.Gray)
                        Text(
                            text = "Rp ${String.format("%,d", totalCost)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = TernivaGreen
                        )
                    }

                    Button(
                        onClick = { viewModel.navigateTo(CurrentScreen.TitipBookingForm(program)) },
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_titip_sekarang")
                    ) {
                        Text("Titip Kelola Sekarang", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Header Card
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Surface(
                            color = if (program.type == ProgramType.PERTANIAN) TernivaGreen.copy(alpha = 0.12f) else TernivaOrange.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (program.type == ProgramType.PERTANIAN) "🌱 PROGRAM PANTAU PERTANIAN" else "🐂 PROGRAM PANTAU PETERNAKAN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (program.type == ProgramType.PERTANIAN) TernivaGreen else TernivaOrange,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = program.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                            Text(program.location, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Divider(color = Color(0xFFF1F5F9))

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stats Summary Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Durasi", fontSize = 11.sp, color = Color.Gray)
                                Text("${program.durationMonths} Bulan", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Est. Panen", fontSize = 11.sp, color = Color.Gray)
                                Text(program.harvestEstimate, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Biaya / Unit", fontSize = 11.sp, color = Color.Gray)
                                Text("Rp ${String.format("%,d", program.feePerUnit)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                            }
                        }
                    }
                }
            }

            // Manager Profile Card
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Profil Pengelola & Penanggung Jawab", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(TernivaGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = TernivaGreen)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(program.managerName, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(program.managerRole, fontSize = 11.sp, color = Color.Gray)
                                Text("Terverifikasi & Bersertifikat Terniva Mitra", fontSize = 10.sp, color = TernivaGreen, fontWeight = FontWeight.SemiBold)
                            }
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
                        Text("Deskripsi & Aktivitas Program", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(program.description, fontSize = 12.sp, lineHeight = 18.sp, color = Color(0xFF334155))
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = Color(0xFFF0FDF4),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Target Hasil: ${program.expectedYield}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                            }
                        }
                    }
                }
            }

            // Budget Breakdown (Transparansi Penggunaan Dana)
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Rincian Transparansi Penggunaan Dana", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        program.budgetBreakdown.forEach { item ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(item.title, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text("Rp ${String.format("%,d", item.amount)}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Text(item.description, fontSize = 10.sp, color = Color.Gray)
                            }
                            Divider(color = Color(0xFFF1F5F9))
                        }
                    }
                }
            }

            // Risks & Terms (Bukan Investasi Bodong)
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WarningAmber, contentDescription = null, tint = TernivaOrange, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Transparansi Risiko & Ketentuan Hasil", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Bukan investasi dengan keuntungan pasti / fixed income. Hasil panen ditentukan oleh kondisi pertumbuhan aktual dengan mitigasi risiko profesional.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        program.risks.forEach { risk ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                Text("• ", color = TernivaOrange, fontWeight = FontWeight.Bold)
                                Text(risk, fontSize = 11.sp, color = Color(0xFF334155))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = Color(0xFFFFFBEB),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Ketentuan Pembagian: ${program.termsAndConditions}",
                                fontSize = 11.sp,
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitipBookingFormScreen(
    program: PantauPanenProgram,
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    var slots by remember { mutableStateOf(1) }
    var investorName by remember { mutableStateOf("Budi Santoso") }
    var investorPhone by remember { mutableStateOf("081234567890") }
    var paymentMethod by remember { mutableStateOf("BCA Virtual Account") }

    val totalFee = program.feePerUnit * slots

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulir Akad Penitipan", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
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
                        Text("Total Biaya Penitipan:", fontSize = 10.sp, color = Color.Gray)
                        Text("Rp ${String.format("%,d", totalFee)}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TernivaGreen)
                    }

                    Button(
                        onClick = {
                            viewModel.joinPantauPanen(
                                program = program,
                                slots = slots,
                                userName = investorName,
                                userPhone = investorPhone,
                                paymentMethod = paymentMethod
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_konfirmasi_penitipan")
                    ) {
                        Text("Konfirmasi & Bayar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
            contentPadding = PaddingValues(12.dp)
        ) {
            // Program summary card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(program.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Pengelola: ${program.managerName}", fontSize = 11.sp, color = Color.Gray)
                        Text("Lokasi: ${program.location}", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }

            // Slot Capacity Selector
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Tentukan Kapasitas Penitipan", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Satuan: ${program.unitLabel}", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Jumlah Slot / Unit:", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedIconButton(
                                    onClick = { if (slots > 1) slots-- },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = "$slots",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 14.dp)
                                )
                                OutlinedIconButton(
                                    onClick = { if (slots < program.availableSlots) slots++ },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Investor Identity Form
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Data Identitas Penitip", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = investorName,
                            onValueChange = { investorName = it },
                            label = { Text("Nama Lengkap Sesuai KTP") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = investorPhone,
                            onValueChange = { investorPhone = it },
                            label = { Text("Nomor WhatsApp Aktif") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Payment Selection
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Pilih Metode Pembayaran", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))

                        listOf("BCA Virtual Account", "Mandiri Virtual Account", "QRIS").forEach { method ->
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveMonitoringDashboardScreen(
    titipOrder: TitipOrder,
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val cropLogs = viewModel.getCropLogs()
    val animal = viewModel.getAnimalRecord()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard Pantau Panen", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F8F6)),
            contentPadding = PaddingValues(12.dp)
        ) {
            // Summary Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = TernivaGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PROGRAM AKTIF",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = titipOrder.status,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = titipOrder.programTitle,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Penitip: ${titipOrder.investorName} (${titipOrder.slots} Slot)",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Timeline Monitoring Pertanian (Section 4C)
            if (titipOrder.type == ProgramType.PERTANIAN) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Timeline Fase Pertumbuhan Padi", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))

                            val stages = listOf(
                                "Persiapan Lahan" to true,
                                "Penanaman" to true,
                                "Pertumbuhan" to true,
                                "Perawatan Tajuk" to true,
                                "Pembungaan" to false,
                                "Pembuahan" to false,
                                "Panen Raya" to false,
                                "Serah Terima" to false
                            )

                            stages.forEachIndexed { idx, (stage, isDone) ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 3.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(if (isDone) TernivaGreen else Color.LightGray),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isDone) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stage,
                                        fontSize = 12.sp,
                                        fontWeight = if (stage == "Perawatan Tajuk") FontWeight.Bold else FontWeight.Normal,
                                        color = if (stage == "Perawatan Tajuk") TernivaGreen else if (isDone) Color.Black else Color.Gray
                                    )
                                    if (stage == "Perawatan Tajuk") {
                                        Surface(
                                            color = TernivaGreen.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.padding(start = 6.dp)
                                        ) {
                                            Text("Sedang Berjalan", fontSize = 9.sp, color = TernivaGreen, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Riwayat Log Aktivitas Pertanian
                item {
                    Text("Riwayat Aktivitas & Perawatan Lahan", fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
                }

                items(cropLogs) { log ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(log.activity, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                                Text(log.date, fontSize = 10.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(log.notes, fontSize = 11.sp, color = Color(0xFF334155))
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                                    Text("💧 Kelembapan: ${log.moisturePercent}%", fontSize = 9.sp, color = TernivaGreen, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                                Surface(color = Color(0xFFFFF3E0), shape = RoundedCornerShape(4.dp)) {
                                    Text("🌡️ Suhu Lahan: ${log.temperatureCelsius}°C", fontSize = 9.sp, color = TernivaOrange, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }
                }
            } else {
                // Section 4D: Monitoring Peternakan (ID masing-masing hewan & kesehatan)
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Sell, contentDescription = null, tint = TernivaOrange, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Eartag IoT: ${animal.tagId}", fontSize = 14.sp, fontWeight = FontWeight.Black)
                                }
                                Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                                    Text(animal.healthStatus, color = TernivaGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Ras: ${animal.breed} • Usia: ${animal.ageMonths} Bulan", fontSize = 12.sp, color = Color.Gray)
                            Text("Lokasi Kandang: ${animal.penLocation}", fontSize = 11.sp, color = Color.Gray)

                            Spacer(modifier = Modifier.height(10.dp))

                            // Bobot Metric
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Bobot Awal", fontSize = 10.sp, color = Color.Gray)
                                    Text("${animal.initialWeightKg} Kg", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text("Bobot Terkini", fontSize = 10.sp, color = Color.Gray)
                                    Text("${animal.weightKg} Kg", fontSize = 15.sp, fontWeight = FontWeight.Black, color = TernivaOrange)
                                }
                                Column {
                                    Text("Pertambahan Bobot (ADG)", fontSize = 10.sp, color = Color.Gray)
                                    Text("+1.35 Kg / hari", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = Color(0xFFF1F5F9))
                            Spacer(modifier = Modifier.height(8.dp))

                            // Riwayat Pakan & Nutrisi
                            Text("Riwayat Asupan Pakan Harian", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            animal.feedLogs.forEach { feed ->
                                Text("• $feed", fontSize = 11.sp, color = Color(0xFF334155), modifier = Modifier.padding(vertical = 2.dp))
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Riwayat Vaksin & Medis
                            Text("Riwayat Vaksinasi & Sertifikasi Hewan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            animal.vaccineLogs.forEach { vac ->
                                Text("✓ $vac", fontSize = 11.sp, color = TernivaGreen, modifier = Modifier.padding(vertical = 2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
