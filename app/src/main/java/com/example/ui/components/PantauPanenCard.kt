package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PantauPanenProgram
import com.example.data.model.ProgramType
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaOrange

@Composable
fun PantauPanenCard(
    program: PantauPanenProgram,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("pantau_program_card_${program.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Type Tag & Duration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (program.type == ProgramType.PERTANIAN)
                            TernivaGreen.copy(alpha = 0.12f)
                        else
                            TernivaOrange.copy(alpha = 0.12f)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (program.type == ProgramType.PERTANIAN) Icons.Default.Agriculture else Icons.Default.Pets,
                            contentDescription = null,
                            tint = if (program.type == ProgramType.PERTANIAN) TernivaGreen else TernivaOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (program.type == ProgramType.PERTANIAN) "PANTAU PERTANIAN" else "PANTAU PETERNAKAN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (program.type == ProgramType.PERTANIAN) TernivaGreen else TernivaOrange
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ) {
                        Text(
                            text = "Durasi: ${program.durationMonths} Bulan",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Body
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = program.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Location & Manager
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Lokasi",
                        tint = Color.Gray,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = program.location,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonPin,
                        contentDescription = "Pengelola",
                        tint = TernivaGreen,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = program.managerName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF334155),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Capacity Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Kapasitas Terisi: ${program.capacityBooked}/${program.capacityTotal} ${program.unitLabel}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "Sisa ${program.availableSlots} Slot",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (program.availableSlots > 5) TernivaGreen else TernivaOrange
                    )
                }

                LinearProgressIndicator(
                    progress = { program.progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .padding(top = 2.dp),
                    color = TernivaGreen,
                    trackColor = Color(0xFFE2E8F0)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Divider(color = Color(0xFFF1F5F9))

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom row: Fee & Action Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Biaya Penitipan:",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Rp ${String.format("%,d", program.feePerUnit)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TernivaGreen
                        )
                        Text(
                            text = "Est. Panen: ${program.harvestEstimate}",
                            fontSize = 10.sp,
                            color = Color(0xFF0284C7),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_detail_program_${program.id}")
                    ) {
                        Text("Lihat Detail", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
