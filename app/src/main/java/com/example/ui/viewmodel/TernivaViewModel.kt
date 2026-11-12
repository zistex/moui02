package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.TernivaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class CurrentScreen {
    object MainNavigation : CurrentScreen()
    data class ProductDetail(val product: Product) : CurrentScreen()
    object Cart : CurrentScreen()
    data class Checkout(val items: List<CartItem>) : CurrentScreen()
    data class OrderSuccess(val order: Order) : CurrentScreen()
    data class OrderTracking(val order: Order) : CurrentScreen()
    data class PantauPanenDetail(val program: PantauPanenProgram) : CurrentScreen()
    data class TitipBookingForm(val program: PantauPanenProgram) : CurrentScreen()
    data class ActiveMonitoring(val titipOrder: TitipOrder) : CurrentScreen()
    object AiConsultationHub : CurrentScreen()
    data class ExportListingDetail(val listing: ExportListing) : CurrentScreen()
    data class ChatRoom(val supplierName: String, val product: Product? = null) : CurrentScreen()
    data class StoreProfile(val supplierName: String, val location: String = "Kab. Cianjur", val isVerified: Boolean = true) : CurrentScreen()
    
    // Dedicated Screens for 7 Layanan Unggulan
    object PantauPanenCatalog : CurrentScreen()
    data class ProductSearch(val initialQuery: String = "", val initialCategory: String = "Semua") : CurrentScreen()
    object SupplierDirectory : CurrentScreen()
    object BuyerRequestsHub : CurrentScreen()
    object ExportHub : CurrentScreen()
    data class MyOrders(val initialStatus: String = "Semua") : CurrentScreen()

    // Auth & Onboarding Screens
    object Splash : CurrentScreen()
    object Login : CurrentScreen()
    object Register : CurrentScreen()
    data class OtpVerification(
        val phoneOrEmail: String,
        val isRegister: Boolean,
        val registeredName: String? = null,
        val registeredRole: UserRole? = null
    ) : CurrentScreen()
}

class TernivaViewModel(
    private val repository: TernivaRepository = TernivaRepository()
) : ViewModel() {

    // Auth State
    val isUserLoggedIn = MutableStateFlow(true)
    val loggedInUserName = MutableStateFlow("Budi Santoso")
    val loggedInUserPhone = MutableStateFlow("081234567890")

    fun login(phoneOrEmail: String, name: String, role: UserRole) {
        isUserLoggedIn.value = true
        loggedInUserName.value = name
        loggedInUserPhone.value = phoneOrEmail
        switchRole(role)
    }

    fun logout() {
        isUserLoggedIn.value = false
        _currentScreen.value = CurrentScreen.Login
    }

    // Bottom Navigation Index (0: Untuk Kamu, 1: Peluang, 2: Live & Video, 3: Notifikasi, 4: Akun)
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // Subscreen Navigation Stack (Starts at Splash or MainNavigation)
    private val _currentScreen = MutableStateFlow<CurrentScreen>(CurrentScreen.Splash)
    val currentScreen: StateFlow<CurrentScreen> = _currentScreen.asStateFlow()

    // Active Category Filter for Marketplace
    private val _selectedCategory = MutableStateFlow("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // User Role
    val userRole: StateFlow<UserRole> = repository.currentUserRole

    // Data streams from repository
    val products = repository.products
    val cartItems = repository.cartItems
    val orders = repository.orders
    val programs = repository.programs
    val titipOrders = repository.titipOrders
    val buyerRequests = repository.buyerRequests
    val exportListings = repository.exportListings
    val exportOffers = repository.exportOffers
    val consultationHistory = repository.consultationHistory
    val doctorProfiles = repository.doctorProfiles
    val videos = repository.videos
    val liveStreams = repository.liveStreams
    val notifications = repository.notifications

    // Selected Program for Pantau Panen Filter (Pertanian / Peternakan)
    private val _pantauFilter = MutableStateFlow(ProgramType.PERTANIAN)
    val pantauFilter: StateFlow<ProgramType> = _pantauFilter.asStateFlow()

    // Dialog state controllers
    val showSupplierRegisterDialog = MutableStateFlow(false)
    val showBuyerRegisterDialog = MutableStateFlow(false)
    val showExportOfferDialog = MutableStateFlow<ExportListing?>(null)
    val showCreateBuyerRequestDialog = MutableStateFlow(false)
    val showExportDocCenterDialog = MutableStateFlow(false)

    // AI Form States
    val aiAnimalFormState = MutableStateFlow(false)
    val aiPlantFormState = MutableStateFlow(false)
    val latestAiResult = MutableStateFlow<AiConsultationRecord?>(null)
    val isAiAnalyzing = MutableStateFlow(false)

    fun selectTab(index: Int) {
        _selectedTab.value = index
        _currentScreen.value = CurrentScreen.MainNavigation
    }

    fun navigateTo(screen: CurrentScreen) {
        _currentScreen.value = screen
    }

    fun navigateBack() {
        _currentScreen.value = CurrentScreen.MainNavigation
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setPantauFilter(type: ProgramType) {
        _pantauFilter.value = type
    }

    fun switchRole(role: UserRole) {
        repository.switchRole(role)
    }

    // Cart actions
    fun addToCart(product: Product, quantity: Int = 1) {
        repository.addToCart(product, quantity)
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        repository.updateCartQuantity(productId, delta)
    }

    fun toggleCartSelection(productId: String) {
        repository.toggleCartSelection(productId)
    }

    fun toggleSelectAll(selectAll: Boolean) {
        repository.toggleSelectAll(selectAll)
    }

    // Checkout
    fun createOrder(
        items: List<CartItem>,
        courier: String,
        paymentMethod: String,
        recipientName: String,
        recipientPhone: String,
        address: String
    ) {
        val order = repository.createOrder(items, courier, paymentMethod, recipientName, recipientPhone, address)
        _currentScreen.value = CurrentScreen.OrderSuccess(order)
    }

    fun simulateConfirmPayment(orderId: String) {
        repository.simulateConfirmPayment(orderId)
    }

    // Pantau Panen Booking
    fun joinPantauPanen(
        program: PantauPanenProgram,
        slots: Int,
        userName: String,
        userPhone: String,
        paymentMethod: String
    ) {
        val titipOrder = repository.joinPantauPanen(program, slots, userName, userPhone, paymentMethod)
        _currentScreen.value = CurrentScreen.ActiveMonitoring(titipOrder)
    }

    // Buyer Request
    fun createBuyerRequest(
        title: String,
        category: String,
        buyerName: String,
        buyerRole: String,
        quantity: String,
        unit: String,
        targetPrice: String,
        location: String,
        deadline: String,
        description: String
    ) {
        repository.createBuyerRequest(
            title, category, buyerName, buyerRole, quantity, unit, targetPrice, location, deadline, description
        )
        showCreateBuyerRequestDialog.value = false
    }

    // Export Offer
    fun submitExportOffer(
        listingId: String,
        commodity: String,
        supplierName: String,
        volume: String,
        priceUsd: String,
        originPort: String
    ) {
        repository.submitExportOffer(listingId, commodity, supplierName, volume, priceUsd, originPort)
        showExportOfferDialog.value = null
    }

    // AI Diagnosis
    fun runAiDiagnosis(
        category: String,
        subjectName: String,
        age: String,
        weight: String,
        symptoms: String
    ) {
        viewModelScope.launch {
            isAiAnalyzing.value = true
            kotlinx.coroutines.delay(1200) // realistic triage latency
            val record = repository.runAiDiagnosis(category, subjectName, age, weight, symptoms)
            latestAiResult.value = record
            isAiAnalyzing.value = false
        }
    }

    fun likeVideo(videoId: String) {
        repository.likeVideo(videoId)
    }

    // Chat State with Supplier
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage(
            id = "MSG-01",
            senderName = "Sistem Terniva",
            text = "Selamat datang di Chat Resmi Terniva. Transaksi Anda dilindungi garansi keamanan panen.",
            timestamp = "10:00",
            isFromMe = false
        ),
        ChatMessage(
            id = "MSG-02",
            senderName = "Gapoktan Agro Cianjur Berkah",
            text = "Halo! Terima kasih telah mengunjungi toko kami. Semua komoditas dipanen langsung saat pesanan masuk.",
            timestamp = "10:05",
            isFromMe = false
        )
    ))
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    fun sendChatMessage(text: String, supplierName: String, linkedProduct: Product? = null) {
        val userMsg = ChatMessage(
            id = java.util.UUID.randomUUID().toString(),
            senderName = "Saya",
            text = text,
            timestamp = "Baru saja",
            isFromMe = true,
            linkedProduct = linkedProduct
        )
        _chatMessages.value = _chatMessages.value + userMsg

        // Auto reply from supplier simulation
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            val reply = ChatMessage(
                id = java.util.UUID.randomUUID().toString(),
                senderName = supplierName,
                text = "Baik kak! Untuk ${linkedProduct?.name ?: "komoditas ini"}, stok di gudang/kebun kami dalam kondisi prima dan siap kirim via kargo hari ini. Ada yang bisa kami bantu lagi?",
                timestamp = "Baru saja",
                isFromMe = false
            )
            _chatMessages.value = _chatMessages.value + reply
        }
    }

    fun getReviewsForProduct(productId: String): List<ProductReview> = listOf(
        ProductReview(
            id = "REV-01",
            userName = "Chef Handoko (Executive Chef)",
            userRole = "Hotel Bintang 5 Yogyakarta",
            rating = 5,
            date = "09 Nov 2026",
            comment = "Kualitas komoditas sangat konsisten! Segar, bersih, dan sesuai standar dapur bintang lima. Tamu kami sangat puas. Akan langganan rutin mingguan.",
            purchasedVariant = "Kemasan 5 Kg",
            supplierReply = "Terima kasih banyak Chef Handoko! Kami pastikan pasokan panen organik selalu terjaga mutunya."
        ),
        ProductReview(
            id = "REV-02",
            userName = "Pak Haji Ridwan",
            userRole = "Peternak Mandiri Boyolali",
            rating = 5,
            date = "07 Nov 2026",
            comment = "Barang sampai dengan aman memakai kargo pendingin. Masih segar dan sesuai deskripsi. Pengiriman super cepat cuma 1 hari.",
            purchasedVariant = "Paket Standar",
            supplierReply = "Alhamdulillah, terima kasih Pak Haji. Semoga berkah untuk usaha peternakannya!"
        ),
        ProductReview(
            id = "REV-03",
            userName = "Ibu Dewi Lestari",
            userRole = "Pedagang Pasar Induk Kramat Jati",
            rating = 5,
            date = "03 Nov 2026",
            comment = "Sudah 3 kali repeat order di toko ini. Harga partai sangat bersaing dan timbangannya pas tidak ada susut.",
            purchasedVariant = "Karung Grosir",
            supplierReply = null
        )
    )

    fun getStoreProfile(supplierName: String, location: String = "Kab. Cianjur"): SupplierStore = SupplierStore(
        name = supplierName,
        isVerified = true,
        location = location,
        lastOnline = "Online 5 menit lalu",
        rating = 4.9f,
        ratingCount = 2840,
        chatResponseRate = "99% (< 5 menit)",
        deliverySpeed = "Sangat Cepat (< 24 jam)",
        followersCount = "14.8rb",
        productsCount = 28,
        joinedSince = "3 tahun lalu",
        description = "Kelompok Tani & Produsen Komoditas Unggulan Terverifikasi Terniva. Berkomitmen menyediakan hasil bumi dan ternak berkualitas tinggi langsung dari petani binaan tanpa perantara tengkulak."
    )

    fun getCropLogs() = repository.getCropLogs()
    fun getAnimalRecord() = repository.getAnimalRecord()

    fun confirmOrderReceived(orderId: String) {
        repository.updateOrderStatus(orderId, OrderStatus.DELIVERED)
        repository.addNotification(
            category = "Transaksi",
            title = "Pesanan Diterima",
            message = "Terima kasih! Pesanan telah selesai. Jangan lupa berikan penilaian & ulasan kepada supplier."
        )
    }

    fun cancelOrder(orderId: String) {
        repository.updateOrderStatus(orderId, OrderStatus.CANCELLED)
        repository.addNotification(
            category = "Transaksi",
            title = "Pesanan Dibatalkan",
            message = "Pesanan telah berhasil dibatalkan sesuai permintaan Anda."
        )
    }

    fun addBuyerRequest(
        title: String,
        category: String,
        buyerName: String,
        buyerRole: String,
        quantity: String,
        unit: String,
        targetPrice: String,
        location: String,
        deadline: String,
        description: String
    ) {
        repository.createBuyerRequest(
            title = title,
            category = category,
            buyerName = buyerName,
            buyerRole = buyerRole,
            quantity = quantity,
            unit = unit,
            targetPrice = targetPrice,
            location = location,
            deadline = deadline,
            description = description
        )
    }

    fun getAllSuppliers(): List<SupplierStore> = listOf(
        SupplierStore(
            name = "Koperasi Tani Makmur Cianjur",
            isVerified = true,
            location = "Kab. Cianjur, Jawa Barat",
            lastOnline = "Online 5 menit lalu",
            rating = 4.9f,
            ratingCount = 2840,
            chatResponseRate = "99% (< 5 menit)",
            deliverySpeed = "Sangat Cepat (< 24 jam)",
            followersCount = "14.8rb",
            productsCount = 28,
            joinedSince = "3 tahun lalu",
            description = "Pusat produsen Beras Pandanwangi Organik, Cabai Rawit Merah, dan Sayuran Dataran Tinggi bersertifikat GAP (Good Agricultural Practices)."
        ),
        SupplierStore(
            name = "Peternakan Barokah Pasuruan",
            isVerified = true,
            location = "Kab. Pasuruan, Jawa Timur",
            lastOnline = "Online 12 menit lalu",
            rating = 4.9f,
            ratingCount = 1420,
            chatResponseRate = "97% (< 10 menit)",
            deliverySpeed = "Armada Khusus Ternak",
            followersCount = "8.6rb",
            productsCount = 15,
            joinedSince = "2 tahun lalu",
            description = "Peternakan Sapi Potong Limousin, Domba Garut, dan Kambing Etawa unggulan dengan standar bio-security ketat dan pakan silase fermentasi."
        ),
        SupplierStore(
            name = "CV Agro Benih Nusantara",
            isVerified = true,
            location = "Kab. Sleman, D.I. Yogyakarta",
            lastOnline = "Online 2 menit lalu",
            rating = 4.8f,
            ratingCount = 3120,
            chatResponseRate = "100% (< 3 menit)",
            deliverySpeed = "Cepat (< 24 jam)",
            followersCount = "21.3rb",
            productsCount = 42,
            joinedSince = "4 tahun lalu",
            description = "Produsen benih hibrida unggul, pupuk hayati mikoriza, dan hormon pertumbuhan organik bersertifikasi Balai Pengawasan dan Sertifikasi Benih."
        ),
        SupplierStore(
            name = "Koperasi Nelayan Bahari Nusantara",
            isVerified = true,
            location = "Kab. Indramayu, Jawa Barat",
            lastOnline = "Online 25 menit lalu",
            rating = 4.9f,
            ratingCount = 980,
            chatResponseRate = "95% (< 15 menit)",
            deliverySpeed = "Cold Chain Box Beku",
            followersCount = "6.1rb",
            productsCount = 12,
            joinedSince = "1 tahun lalu",
            description = "Penyedia ikan nila segar, udang vaname tambak intensif, dan ikan laut tangkapan segar dengan rantai pendingin cold-chain terstandar ekspor."
        ),
        SupplierStore(
            name = "AgriPakan Mandiri Prima",
            isVerified = true,
            location = "Kab. Boyolali, Jawa Tengah",
            lastOnline = "Online 1 jam lalu",
            rating = 4.8f,
            ratingCount = 2210,
            chatResponseRate = "96% (< 10 menit)",
            deliverySpeed = "Kargo Tonase Besar",
            followersCount = "11.5rb",
            productsCount = 19,
            joinedSince = "3 tahun lalu",
            description = "Pabrik formulasi konsentrat pakan sapi perah & potong, pelet ayam buras, dan probiotik pencernaan ruminansia tinggi protein."
        )
    )
}
