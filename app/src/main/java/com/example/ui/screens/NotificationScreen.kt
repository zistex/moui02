package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaOrange
import com.example.ui.viewmodel.TernivaViewModel

@Composable
fun NotificationScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsState()
    var selectedCategory by remember { mutableStateOf("Semua") }

    val categories = listOf("Semua", "Transaksi", "Pantau Panen", "Konsultasi", "Ekspor", "Sistem")

    val filteredNotifications = remember(notifications, selectedCategory) {
        if (selectedCategory == "Semua") notifications
        else notifications.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8F6))
    ) {
        // Header
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text("Pusat Notifikasi Terniva", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TernivaGreen,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp)
        ) {
            if (filteredNotifications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Tidak ada notifikasi dalam kategori ini", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }

            items(filteredNotifications) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    when (item.category) {
                                        "Transaksi" -> TernivaOrange.copy(alpha = 0.15f)
                                        "Pantau Panen" -> TernivaGreen.copy(alpha = 0.15f)
                                        "Ekspor" -> Color(0xFF0284C7).copy(alpha = 0.15f)
                                        else -> Color(0xFF8B5CF6).copy(alpha = 0.15f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (item.category) {
                                    "Transaksi" -> Icons.Default.ReceiptLong
                                    "Pantau Panen" -> Icons.Default.Agriculture
                                    "Ekspor" -> Icons.Default.Public
                                    "Konsultasi" -> Icons.Default.MedicalServices
                                    else -> Icons.Default.Notifications
                                },
                                contentDescription = null,
                                tint = when (item.category) {
                                    "Transaksi" -> TernivaOrange
                                    "Pantau Panen" -> TernivaGreen
                                    "Ekspor" -> Color(0xFF0284C7)
                                    else -> Color(0xFF8B5CF6)
                                },
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                Text(item.timeAgo, fontSize = 10.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(item.message, fontSize = 11.sp, color = Color(0xFF475569), lineHeight = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
