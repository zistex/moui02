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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.data.model.Product
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaGreenLight
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    supplierName: String,
    linkedProduct: Product? = null,
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    var inputText by remember { mutableStateOf("") }

    val quickQuestions = listOf(
        "Apakah stok ready?",
        "Bisa kirim kargo hari ini?",
        "Bisa pesan partai besar?",
        "Ada sertifikasi organiknya?"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = supplierName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = "Terverifikasi",
                                tint = TernivaGreen,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(TernivaGreen)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Online 5 menit lalu • Performa Chat 99%",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.navigateTo(CurrentScreen.StoreProfile(supplierName)) }
                    ) {
                        Text("Kunjungi Toko", fontSize = 11.sp, color = TernivaGreen, fontWeight = FontWeight.Bold)
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
                Column(modifier = Modifier.padding(8.dp)) {
                    // Quick Question Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        items(quickQuestions) { q ->
                            Surface(
                                color = TernivaGreenLight,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.clickable {
                                    viewModel.sendChatMessage(q, supplierName, linkedProduct)
                                }
                            ) {
                                Text(
                                    text = q,
                                    fontSize = 11.sp,
                                    color = TernivaGreen,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Input Field & Send Action
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.AddCircleOutline, contentDescription = "Lampirkan", tint = TernivaGreen)
                        }

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Ketik pesan ke supplier...", fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("chat_input_field")
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    viewModel.sendChatMessage(inputText.trim(), supplierName, linkedProduct)
                                    inputText = ""
                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(TernivaGreen)
                                .testTag("chat_send_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Kirim", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF7FAF7)),
            contentPadding = PaddingValues(12.dp)
        ) {
            // Pinned product context snippet
            if (linkedProduct != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(TernivaGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Eco, contentDescription = null, tint = TernivaGreen)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(linkedProduct.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                Text("Rp ${String.format("%,d", linkedProduct.price)} / ${linkedProduct.unit}", fontSize = 12.sp, color = TernivaGreen, fontWeight = FontWeight.Black)
                            }
                            Button(
                                onClick = {
                                    viewModel.sendChatMessage("Saya tertarik memesan produk '${linkedProduct.name}', apakah ada diskon untuk pembelian di atas 5 unit?", supplierName, linkedProduct)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Tanya Produk", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            // Message Stream
            items(messages) { msg ->
                val isMe = msg.isFromMe
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                ) {
                    if (!isMe) {
                        Text(msg.senderName, fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp, bottom = 2.dp))
                    }
                    Surface(
                        color = if (isMe) TernivaGreen else Color.White,
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = if (isMe) 12.dp else 2.dp,
                            bottomEnd = if (isMe) 2.dp else 12.dp
                        ),
                        shadowElevation = 1.dp,
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = msg.text,
                                color = if (isMe) Color.White else Color(0xFF1E293B),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg.timestamp,
                                color = if (isMe) Color.White.copy(alpha = 0.7f) else Color.Gray,
                                fontSize = 9.sp,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}
