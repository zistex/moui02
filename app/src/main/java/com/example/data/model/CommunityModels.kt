package com.example.data.model

data class LiveStreamItem(
    val id: String,
    val title: String,
    val hostName: String,
    val hostBadge: String,
    val viewersCount: Int,
    val isLive: Boolean = true,
    val pinnedProductName: String,
    val pinnedProductPrice: Long,
    val productId: String = "PRD-01"
)

data class VideoFeedItem(
    val id: String,
    val title: String,
    val creatorName: String,
    val creatorHandle: String = "@petani_terniva",
    val soundTrack: String = "Suara Asli - Terniva Agritech",
    val durationText: String = "01:00",
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val tags: List<String>,
    val pinnedProductName: String,
    val pinnedProductPrice: Long,
    val productId: String,
    val discountTag: String = "Promo",
    val isLive: Boolean = false,
    val bgPrimaryColor: Long = 0xFF064E3B,
    val bgSecondaryColor: Long = 0xFF022C22
)

data class NotificationItem(
    val id: String,
    val category: String, // "Transaksi", "Pantau Panen", "Konsultasi", "Ekspor", "Sistem"
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false
)

enum class UserRole(val label: String) {
    BUYER("Pembeli / Horeka"),
    SUPPLIER("Petani & Supplier"),
    STEWARD("Pengelola Pantau Panen")
}
