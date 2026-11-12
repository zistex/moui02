package com.example.data.model

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Long,
    val originalPrice: Long? = null,
    val unit: String, // "Kg", "Ton", "Karung", "Ekor", "Liter", "Peti"
    val minOrder: Int = 1,
    val stock: Int,
    val location: String,
    val rating: Float = 4.8f,
    val soldCount: Int = 120,
    val supplierName: String,
    val isSupplierVerified: Boolean = true,
    val imageUrl: String = "",
    val description: String,
    val specifications: Map<String, String> = emptyMap(),
    val isFlashSale: Boolean = false,
    val freeShippingBadge: Boolean = true
) {
    val discountPercent: Int
        get() = if (originalPrice != null && originalPrice > price) {
            (((originalPrice - price).toDouble() / originalPrice) * 100).toInt()
        } else 0
}

data class CartItem(
    val product: Product,
    val quantity: Int,
    val isSelected: Boolean = true
)

data class Order(
    val id: String,
    val orderNumber: String,
    val date: String,
    val items: List<CartItem>,
    val subtotal: Long,
    val shippingCost: Long,
    val totalAmount: Long,
    val status: OrderStatus,
    val paymentMethod: String,
    val virtualAccountNumber: String,
    val courierName: String,
    val recipientName: String,
    val recipientPhone: String,
    val recipientAddress: String
)

enum class OrderStatus(val label: String) {
    PENDING_PAYMENT("Menunggu Pembayaran"),
    PAID("Dibayar"),
    PROCESSING("Diproses"),
    SHIPPED("Dalam Pengiriman"),
    DELIVERED("Terkirim"),
    COMPLETED("Selesai"),
    CANCELLED("Dibatalkan")
}

data class BuyerRequest(
    val id: String,
    val title: String,
    val category: String,
    val buyerName: String,
    val buyerRole: String, // "Hotel Bintang 5", "Restoran Seafood", "Pasar Induk", "Pabrik Saus"
    val quantity: String,
    val unit: String,
    val targetPrice: String,
    val location: String,
    val deadline: String,
    val description: String,
    val offersCount: Int = 0,
    val status: String = "Terbuka"
)

data class ExportListing(
    val id: String,
    val commodityName: String,
    val destinationCountry: String,
    val flagEmoji: String,
    val volume: String,
    val unit: String,
    val qualityGrade: String,
    val certification: String,
    val packaging: String,
    val incoterms: String, // "FOB", "CIF", "CFR"
    val portDestination: String,
    val deliverySchedule: String,
    val paymentTerms: String,
    val deadline: String,
    val status: String = "Menerima Penawaran"
)

data class ExportOffer(
    val id: String,
    val exportListingId: String,
    val commodityName: String,
    val supplierName: String,
    val offeredVolume: String,
    val price: String,
    val currency: String = "USD",
    val originPort: String,
    val status: String = "Dalam Seleksi",
    val date: String
)

data class ProductReview(
    val id: String,
    val userName: String,
    val userRole: String, // "Executive Chef Hotel", "Peternak Mandiri", "Pedagang Pasar"
    val rating: Int = 5,
    val date: String,
    val comment: String,
    val purchasedVariant: String = "5 Kg",
    val supplierReply: String? = null
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val linkedProduct: Product? = null
)

data class SupplierStore(
    val name: String,
    val isVerified: Boolean = true,
    val location: String,
    val lastOnline: String = "Online 5 menit lalu",
    val rating: Float = 4.9f,
    val ratingCount: Int = 1840,
    val chatResponseRate: String = "99% (< 10 menit)",
    val deliverySpeed: String = "Cepat (< 24 jam)",
    val followersCount: String = "14.2rb",
    val productsCount: Int = 36,
    val joinedSince: String = "2 tahun lalu",
    val description: String
)
