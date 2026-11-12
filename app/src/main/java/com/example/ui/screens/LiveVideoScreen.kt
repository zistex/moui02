package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.VideoFeedItem
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveVideoScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    val videos by viewModel.videos.collectAsState()
    val products by viewModel.products.collectAsState()
    val pagerState = rememberPagerState { videos.size }

    var selectedTopTab by remember { mutableStateOf(1) } // 0: Live Panen, 1: Untuk Anda (FYP)
    var showCommentSheet by remember { mutableStateOf(false) }
    var activeProductToBuy by remember { mutableStateOf<Product?>(null) }
    var showPurchasedSnackbar by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Vertical Pager: 1 Video Per Screen (True TikTok / Shopee Video experience)
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val video = videos[pageIndex]
            val linkedProduct = products.find { it.id == video.productId } ?: products.first()

            SingleFullScreenVideo(
                video = video,
                linkedProduct = linkedProduct,
                onLike = { viewModel.likeVideo(video.id) },
                onCommentClick = { showCommentSheet = true },
                onBuyClick = { activeProductToBuy = linkedProduct },
                onOpenProductDetail = { viewModel.navigateTo(CurrentScreen.ProductDetail(linkedProduct)) }
            )
        }

        // Top Overlay Header: Live Panen | Untuk Anda
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Live Status Indicator
            Surface(
                color = Color.Red,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("LIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
            }

            // Center Top Switcher (Shopee Video / TikTok style)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Live Panen",
                    color = if (selectedTopTab == 0) Color.White else Color.White.copy(alpha = 0.6f),
                    fontSize = 15.sp,
                    fontWeight = if (selectedTopTab == 0) FontWeight.Black else FontWeight.Normal,
                    modifier = Modifier
                        .clickable { selectedTopTab = 0 }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(" | ", color = Color.White.copy(alpha = 0.4f), fontSize = 14.sp)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Untuk Anda",
                        color = if (selectedTopTab == 1) Color.White else Color.White.copy(alpha = 0.6f),
                        fontSize = 16.sp,
                        fontWeight = if (selectedTopTab == 1) FontWeight.Black else FontWeight.Normal,
                        modifier = Modifier
                            .clickable { selectedTopTab = 1 }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    if (selectedTopTab == 1) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(2.5.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(Color.White)
                        )
                    }
                }
            }

            // Search Icon
            IconButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = "Cari Video", tint = Color.White)
            }
        }

        // Quick Bottom Sheet for Shopping (Tombol Belanja Produk)
        if (activeProductToBuy != null) {
            val product = activeProductToBuy!!
            ModalBottomSheet(
                onDismissRequest = { activeProductToBuy = null },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Eco, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(36.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Rp ${String.format("%,d", product.price)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TernivaGreen
                            )
                            if (product.originalPrice != null) {
                                Text(
                                    text = "Rp ${String.format("%,d", product.originalPrice)}",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            }
                            Text(
                                text = product.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Stok: ${product.stock} ${product.unit} • 📍 ${product.location}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.addToCart(product, 1)
                                activeProductToBuy = null
                                showPurchasedSnackbar = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TernivaGreen),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TernivaGreen)
                        ) {
                            Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Keranjang", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                activeProductToBuy = null
                                viewModel.navigateTo(CurrentScreen.Checkout(listOf(com.example.data.model.CartItem(product, 1))))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_beli_sekarang_video_modal"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
                        ) {
                            Text("Beli Sekarang", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        // Comment Bottom Sheet Modal
        if (showCommentSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCommentSheet = false },
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Komentar Petani & Pembeli (214)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    val sampleComments = listOf(
                        "Kang Asep (Peternak Lembang)" to "Pakan silase ini aromanya wangi tape ya pakde? Sapi saya suka banget!",
                        "Resto Dapur Sunda" to "Kualitas sayur dan beras panen kemarin top banget, pasokan konsisten.",
                        "Mas Tri Widodo" to "Sudah order 2 karung pakan konsentrat lewat keranjang kuning video ini 👍",
                        "drh. Ahmad Kurniawan" to "Komposisi silase rumput odot + ampas tahu sudah pas nutrisinya untuk ruminansia."
                    )

                    sampleComments.forEach { (author, comment) ->
                        Row(modifier = Modifier.padding(vertical = 6.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(TernivaGreen.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(author.take(1), fontWeight = FontWeight.Bold, color = TernivaGreen)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(author, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                Text(comment, fontSize = 12.sp, color = Color(0xFF334155))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun SingleFullScreenVideo(
    video: VideoFeedItem,
    linkedProduct: Product,
    onLike: () -> Unit,
    onCommentClick: () -> Unit,
    onBuyClick: () -> Unit,
    onOpenProductDetail: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var isLiked by remember { mutableStateOf(false) }
    var isFollowed by remember { mutableStateOf(false) }
    var showFloatingHeart by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Rotating music disc animation
    val infiniteTransition = rememberInfiniteTransition(label = "music_rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    // Animated pulse wave for video playback effect
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (!isLiked) {
                            isLiked = true
                            onLike()
                        }
                        showFloatingHeart = true
                        coroutineScope.launch {
                            delay(900)
                            showFloatingHeart = false
                        }
                    },
                    onTap = {
                        isPlaying = !isPlaying
                    }
                )
            }
    ) {
        // 1. Full Screen Video Visual Canvas Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(video.bgPrimaryColor),
                            Color(video.bgSecondaryColor),
                            Color(0xFF020617)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Central Agricultural Dynamic Graphic Motif (Simulating active camera recording / video player)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = if (isPlaying) pulseAlpha else 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(TernivaGreen.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (video.isLive) Icons.Default.LiveTv else Icons.Default.Agriculture,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isPlaying) {
                            Text("▶ Video Sedang Diputar", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        } else {
                            Icon(Icons.Default.Pause, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Video Dijeda (Sentuh untuk Lanjut)", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Big Pause Indicator overlay when user paused
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(48.dp))
                }
            }

            // Floating Heart on Double Tap
            AnimatedVisibility(
                visible = showFloatingHeart,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFFF2D55),
                    modifier = Modifier.size(100.dp)
                )
            }
        }

        // 2. Right Side Action Rail (Shopee Video / TikTok style buttons)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Creator Avatar with "+" Follow toggle
            Box(contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.White, CircleShape)
                        .background(TernivaGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Box(
                    modifier = Modifier
                        .offset(y = 8.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(if (isFollowed) Color.Gray else TernivaHarvestGold)
                        .clickable { isFollowed = !isFollowed },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFollowed) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = "Ikuti",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Like Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = {
                        isLiked = !isLiked
                        onLike()
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Suka",
                        tint = if (isLiked) Color(0xFFFF2D55) else Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "${video.likesCount + if (isLiked) 1 else 0}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Comment Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onCommentClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubble,
                        contentDescription = "Komentar",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text("${video.commentsCount}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            // Share Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Bagikan",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text("${video.sharesCount}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            // Spinning Vinyl Music Disc
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E293B))
                    .border(2.dp, Color(0xFF475569), CircleShape)
                    .rotate(if (isPlaying) rotationAngle else 0f),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(TernivaGreen)
                )
            }
        }

        // 3. Bottom Content & TOMBOL UNTUK BELANJA (Keranjang Kuning Shopee Video)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.82f)
                .padding(start = 14.dp, bottom = 16.dp)
        ) {
            // *** TOMBOL UNTUK BELANJA / KERANJANG KUNING PRODUCT PILL ***
            Surface(
                color = Color(0xFFFFFBEB), // Amber-yellow background like TikTok Shop / Shopee Video
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable { onBuyClick() }
                    .testTag("tombol_belanja_video_${video.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Yellow/Orange Bag Badge
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF08A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Belanja",
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = TernivaHarvestGold,
                                shape = RoundedCornerShape(3.dp),
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Text(
                                    text = video.discountTag,
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                            Text(
                                text = "Beli Produk Ini",
                                color = Color(0xFF78350F),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = video.pinnedProductName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "Rp ${String.format("%,d", video.pinnedProductPrice)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = TernivaGreen
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Prominent Terniva Green Buy Button
                    Button(
                        onClick = onBuyClick,
                        colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("btn_beli_produk_video")
                    ) {
                        Text("Beli", fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            // Creator Tag & Verified Icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = video.creatorHandle,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Terverifikasi",
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Caption & Hashtags
            Text(
                text = video.title,
                color = Color.White.copy(alpha = 0.95f),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                video.tags.forEach { tag ->
                    Text(tag, color = TernivaGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Sound Track marquee simulation
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(video.soundTrack, color = Color.White, fontSize = 10.sp, maxLines = 1)
            }
        }
    }
}
