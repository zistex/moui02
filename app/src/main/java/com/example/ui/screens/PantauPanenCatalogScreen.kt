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
import com.example.data.model.PantauPanenProgram
import com.example.data.model.ProgramType
import com.example.ui.components.PantauPanenCard
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantauPanenCatalogScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Katalog Titip Kelola, 1: Pantauan Saya (Monitoring)
    var commodityTypeFilter by remember { mutableStateOf("Semua") } // Semua, Pertanian, Peternakan
    val programs by viewModel.programs.collectAsState()
    val titipOrders by viewModel.titipOrders.collectAsState()

    val filteredPrograms = remember(programs, commodityTypeFilter) {
        when (commodityTypeFilter) {
            "Pertanian" -> programs.filter { it.type == ProgramType.PERTANIAN }
            "Peternakan" -> programs.filter { it.type == ProgramType.PETERNAKAN }
            else -> programs
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pantau Panen (Titip Tani & Ternak)", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
            // Main Tabs: Katalog Titip vs Pantauan Aktif
            Surface(color = Color.White, shadowElevation = 2.dp) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = TernivaGreen
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Katalog Program Titip", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Pantauan Saya", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                if (titipOrders.isNotEmpty()) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Surface(
                                        color = TernivaGreen,
                                        shape = CircleShape
                                    ) {
                                        Text(
                                            text = "${titipOrders.size}",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            if (selectedTab == 0) {
                // Info banner explaining the program transparently
                Card(
                    colors = CardDefaults.cardColors(containerColor = TernivaGreen.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Titipkan perawatan komoditas kepada mitra tani/ternak terverifikasi. Transparan dengan timeline riwayat dan laporan panen berkala.",
                            fontSize = 11.sp,
                            color = Color(0xFF064E3B),
                            lineHeight = 15.sp
                        )
                    }
                }

                // Filter Chips Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Semua", "Pertanian", "Peternakan").forEach { chip ->
                        val isSelected = commodityTypeFilter == chip
                        FilterChip(
                            selected = isSelected,
                            onClick = { commodityTypeFilter = chip },
                            label = { Text(chip, fontSize = 11.sp) },
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
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredPrograms) { program ->
                        PantauPanenCard(
                            program = program,
                            onClick = { viewModel.navigateTo(CurrentScreen.PantauPanenDetail(program)) }
                        )
                    }
                }
            } else {
                // Tab 1: Pantauan Saya (Active Titip Monitoring)
                if (titipOrders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Park, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(60.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Belum Ada Titip Panen Aktif", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                            Text(
                                "Mulai titipkan pengelolaan lahan atau ternak kepada mitra petani Terniva.",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { selectedTab = 0 },
                                colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Jelajahi Program Titip", fontSize = 12.sp)
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(titipOrders) { order ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.navigateTo(CurrentScreen.ActiveMonitoring(order)) }
                                    .testTag("monitoring_card_${order.id}")
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = if (order.type == ProgramType.PERTANIAN) TernivaGreen.copy(alpha = 0.12f) else TernivaHarvestGold.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = if (order.type == ProgramType.PERTANIAN) "🌱 PERTANIAN AKTIF" else "🐂 PETERNAKAN AKTIF",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (order.type == ProgramType.PERTANIAN) TernivaGreen else TernivaHarvestGold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }

                                        Text(
                                            text = order.status,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TernivaGreen
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = order.programTitle,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "Kapasitas Titip: ${order.slots} Slot • Biaya: Rp ${String.format("%,d", order.totalFee)}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF475569)
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Stage Progress bar
                                    LinearProgressIndicator(
                                        progress = { 0.65f },
                                        color = TernivaGreen,
                                        trackColor = Color(0xFFE2E8F0),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Tahap 4: Pemeliharaan & Monitoring", fontSize = 11.sp, color = TernivaGreen, fontWeight = FontWeight.SemiBold)
                                        Text("65%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = { viewModel.navigateTo(CurrentScreen.ActiveMonitoring(order)) },
                                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Buka Dashboard Monitoring & CCTV", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
