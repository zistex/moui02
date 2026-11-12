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
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaOrange
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiConsultationHubScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    var selectedConsultTab by remember { mutableStateOf(0) } // 0: AI Hewan, 1: AI Tanaman, 2: Dokter Profesional
    val isAnalyzing by viewModel.isAiAnalyzing.collectAsState()
    val latestResult by viewModel.latestAiResult.collectAsState()
    val doctors = viewModel.doctorProfiles
    val products by viewModel.products.collectAsState()

    // Form fields for AI Hewan
    var animalType by remember { mutableStateOf("Sapi") }
    var animalAge by remember { mutableStateOf("18 Bulan") }
    var animalWeight by remember { mutableStateOf("380 Kg") }
    var animalSymptoms by remember { mutableStateOf("Perut kembung membesar di sisi kiri, gelisah dan malas makan rumput") }

    // Form fields for AI Tanaman
    var cropType by remember { mutableStateOf("Cabai Rawit") }
    var cropAge by remember { mutableStateOf("50 Hari Setelah Tanam (HST)") }
    var cropStage by remember { mutableStateOf("Fase Berbunga & Pembuahan") }
    var cropSymptoms by remember { mutableStateOf("Muncul bercak hitam melingkar pada buah cabai dan daun mengering busuk") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Doctor & Konsultasi Agritech", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
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
            // Three Tabs: AI Hewan, AI Tanaman, Dokter Ahli
            TabRow(
                selectedTabIndex = selectedConsultTab,
                containerColor = Color.White,
                contentColor = TernivaGreen
            ) {
                Tab(
                    selected = selectedConsultTab == 0,
                    onClick = { selectedConsultTab = 0 },
                    text = { Text("AI Hewan", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Pets, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedConsultTab == 1,
                    onClick = { selectedConsultTab = 1 },
                    text = { Text("AI Tanaman", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Spa, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedConsultTab == 2,
                    onClick = { selectedConsultTab = 2 },
                    text = { Text("Dokter Ahli", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.MedicalServices, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                if (selectedConsultTab == 0 || selectedConsultTab == 1) {
                    val isAnimal = selectedConsultTab == 0

                    // Disclaimer Header Card
                    item {
                        Surface(
                            color = Color(0xFFFEF3C7),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Dokter AI memberikan analisis awal dan rekomendasi tindakan berdasar gejala klinis. AI tidak menggantikan diagnosis definitif dokter hewan/agronom lapangan. Untuk kasus darurat, hubungi dokter terdekat.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF92400E),
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    // Interactive Input Form
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isAnimal) "Identifikasi Kesehatan Ternak" else "Identifikasi Hama & Penyakit Tanaman",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                if (isAnimal) {
                                    OutlinedTextField(
                                        value = animalType,
                                        onValueChange = { animalType = it },
                                        label = { Text("Jenis Hewan (misal: Sapi, Kambing, Domba, Ayam)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedTextField(
                                            value = animalAge,
                                            onValueChange = { animalAge = it },
                                            label = { Text("Usia Hewan") },
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedTextField(
                                            value = animalWeight,
                                            onValueChange = { animalWeight = it },
                                            label = { Text("Estimasi Berat") },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = animalSymptoms,
                                        onValueChange = { animalSymptoms = it },
                                        label = { Text("Gejala yang Terlihat (batuk, nafsu makan, kembung, dll)") },
                                        minLines = 3,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    OutlinedTextField(
                                        value = cropType,
                                        onValueChange = { cropType = it },
                                        label = { Text("Jenis Komoditas (Cabai, Padi, Bawang, Tomat)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedTextField(
                                            value = cropAge,
                                            onValueChange = { cropAge = it },
                                            label = { Text("Usia Tanaman (HST)") },
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedTextField(
                                            value = cropStage,
                                            onValueChange = { cropStage = it },
                                            label = { Text("Fase Pertumbuhan") },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = cropSymptoms,
                                        onValueChange = { cropSymptoms = it },
                                        label = { Text("Gejala Daun, Batang atau Buah") },
                                        minLines = 3,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Photo Upload Placeholder
                                Surface(
                                    color = Color(0xFFF8FAFC),
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = TernivaGreen)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Unggah Foto Gejala / Daun / Ternak (Opsional)", fontSize = 11.sp, color = TernivaGreen, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        if (isAnimal) {
                                            viewModel.runAiDiagnosis("Hewan", animalType, animalAge, animalWeight, animalSymptoms)
                                        } else {
                                            viewModel.runAiDiagnosis("Tanaman", cropType, cropAge, cropStage, cropSymptoms)
                                        }
                                    },
                                    enabled = !isAnalyzing,
                                    colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_jalankan_analisis_ai")
                                ) {
                                    if (isAnalyzing) {
                                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("AI Sedang Menganalisis Gejala...")
                                    } else {
                                        Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Analisis Diagnosa AI Sekarang", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // AI Diagnosis Result Section
                    if (latestResult != null) {
                        val result = latestResult!!
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(10.dp),
                                elevation = CardDefaults.cardElevation(3.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Hasil Triage AI Terniva", fontSize = 14.sp, fontWeight = FontWeight.Black)
                                        Surface(
                                            color = if (result.urgencyLevel.contains("Darurat")) Color(0xFFFEE2E2) else Color(0xFFE0F2FE),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "Urgensi: ${result.urgencyLevel}",
                                                color = if (result.urgencyLevel.contains("Darurat")) Color(0xFFB91C1C) else Color(0xFF0369A1),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = result.diagnosis,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TernivaGreen
                                    )

                                    Text(
                                        text = "Tingkat Keyakinan Analisis: ${result.confidence}%",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Divider(color = Color(0xFFF1F5F9))
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("Langkah Tindakan Segera Direkomendasikan:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    result.recommendations.forEach { step ->
                                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                            Text("✓ ", color = TernivaGreen, fontWeight = FontWeight.Bold)
                                            Text(step, fontSize = 11.sp, color = Color(0xFF334155))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Recommended Products Section (Section 5D)
                                    Text("Obat & Nutrisi yang Direkomendasikan:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(6.dp))

                                    products.take(2).forEach { prod ->
                                        Surface(
                                            color = Color(0xFFF8FAFC),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 3.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.Medication, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(20.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(prod.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                                    Text("Rp ${String.format("%,d", prod.price)} • ${prod.supplierName}", fontSize = 10.sp, color = TernivaOrange)
                                                }
                                                Button(
                                                    onClick = { viewModel.navigateTo(CurrentScreen.ProductDetail(prod)) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = TernivaOrange),
                                                    shape = RoundedCornerShape(6.dp),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                                ) {
                                                    Text("Beli", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Tab 2: Dokter Profesional Berbayar (Section 5C)
                    item {
                        Text(
                            text = "Konsultasi Dokter & Agronom Berlisensi",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Terverifikasi STRV Dinas Peternakan & SKA Agronomi. Konsultasi langsung via chat & video.",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }

                    items(doctors) { doctor ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(TernivaGreen.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.MedicalServices, contentDescription = null, tint = TernivaGreen)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(doctor.name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(doctor.title, fontSize = 11.sp, color = Color.Gray, maxLines = 1)
                                        Text(doctor.strNumber, fontSize = 10.sp, color = TernivaGreen, fontWeight = FontWeight.SemiBold)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFA000), modifier = Modifier.size(12.dp))
                                            Text(" ${doctor.rating}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Text("${doctor.experienceYears} thn exp", fontSize = 10.sp, color = Color.Gray)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(doctor.bio, fontSize = 11.sp, color = Color(0xFF475569))

                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = Color(0xFFF1F5F9))
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Tarif Konsultasi:", fontSize = 10.sp, color = Color.Gray)
                                        Text("Rp ${String.format("%,d", doctor.consultFee)}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = TernivaOrange)
                                    }

                                    Button(
                                        onClick = { },
                                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Icon(Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Konsultasi Sekarang", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
