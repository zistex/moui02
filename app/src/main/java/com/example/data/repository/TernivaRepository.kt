package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class TernivaRepository {

    // --- Current Active User Role ---
    private val _currentUserRole = MutableStateFlow(UserRole.BUYER)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    fun switchRole(newRole: UserRole) {
        _currentUserRole.value = newRole
    }

    // --- Products Catalog ---
    private val _products = MutableStateFlow<List<Product>>(getInitialProducts())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // --- Cart ---
    private val _cartItems = MutableStateFlow<List<CartItem>>(getInitialCart())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // --- Orders ---
    private val _orders = MutableStateFlow<List<Order>>(getInitialOrders())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // --- Pantau Panen Programs ---
    private val _programs = MutableStateFlow<List<PantauPanenProgram>>(getInitialPrograms())
    val programs: StateFlow<List<PantauPanenProgram>> = _programs.asStateFlow()

    // --- Active Titip Orders ---
    private val _titipOrders = MutableStateFlow<List<TitipOrder>>(getInitialTitipOrders())
    val titipOrders: StateFlow<List<TitipOrder>> = _titipOrders.asStateFlow()

    // --- Buyer Requests ---
    private val _buyerRequests = MutableStateFlow<List<BuyerRequest>>(getInitialBuyerRequests())
    val buyerRequests: StateFlow<List<BuyerRequest>> = _buyerRequests.asStateFlow()

    // --- Export Listings ---
    private val _exportListings = MutableStateFlow<List<ExportListing>>(getInitialExportListings())
    val exportListings: StateFlow<List<ExportListing>> = _exportListings.asStateFlow()

    private val _exportOffers = MutableStateFlow<List<ExportOffer>>(getInitialExportOffers())
    val exportOffers: StateFlow<List<ExportOffer>> = _exportOffers.asStateFlow()

    // --- AI & Consultation Records ---
    private val _consultationHistory = MutableStateFlow<List<AiConsultationRecord>>(getInitialConsultations())
    val consultationHistory: StateFlow<List<AiConsultationRecord>> = _consultationHistory.asStateFlow()

    val doctorProfiles = getInitialDoctors()

    // --- Live & Video ---
    private val _videos = MutableStateFlow<List<VideoFeedItem>>(getInitialVideos())
    val videos: StateFlow<List<VideoFeedItem>> = _videos.asStateFlow()

    val liveStreams = getInitialLiveStreams()

    // --- Notifications ---
    private val _notifications = MutableStateFlow<List<NotificationItem>>(getInitialNotifications())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // --- Cart Operations ---
    fun addToCart(product: Product, quantity: Int = 1) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val existing = current[index]
            current[index] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            current.add(CartItem(product = product, quantity = quantity, isSelected = true))
        }
        _cartItems.value = current
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        val current = _cartItems.value.mapNotNull { item ->
            if (item.product.id == productId) {
                val newQty = item.quantity + delta
                if (newQty > 0) item.copy(quantity = newQty) else null
            } else item
        }
        _cartItems.value = current
    }

    fun toggleCartSelection(productId: String) {
        _cartItems.value = _cartItems.value.map {
            if (it.product.id == productId) it.copy(isSelected = !it.isSelected) else it
        }
    }

    fun toggleSelectAll(selectAll: Boolean) {
        _cartItems.value = _cartItems.value.map { it.copy(isSelected = selectAll) }
    }

    // --- Checkout & Orders ---
    fun createOrder(
        items: List<CartItem>,
        courier: String,
        paymentMethod: String,
        recipientName: String,
        recipientPhone: String,
        address: String
    ): Order {
        val subtotal = items.sumOf { it.product.price * it.quantity }
        val shipping = 25000L
        val total = subtotal + shipping
        val vaNum = "8809" + (1000000000L..9999999999L).random()
        val order = Order(
            id = UUID.randomUUID().toString(),
            orderNumber = "TRN-${System.currentTimeMillis().toString().takeLast(8)}",
            date = "Hari ini, 15:30",
            items = items,
            subtotal = subtotal,
            shippingCost = shipping,
            totalAmount = total,
            status = OrderStatus.PENDING_PAYMENT,
            paymentMethod = paymentMethod,
            virtualAccountNumber = vaNum,
            courierName = courier,
            recipientName = recipientName,
            recipientPhone = recipientPhone,
            recipientAddress = address
        )

        // Add to orders list
        _orders.value = listOf(order) + _orders.value

        // Remove bought items from cart
        val boughtIds = items.map { it.product.id }.toSet()
        _cartItems.value = _cartItems.value.filterNot { boughtIds.contains(it.product.id) }

        // Add notification
        addNotification(
            category = "Transaksi",
            title = "Pesanan Dibuat #${order.orderNumber}",
            message = "Selesaikan pembayaran sebesar Rp ${String.format("%,d", total)} via $paymentMethod."
        )

        return order
    }

    fun simulateConfirmPayment(orderId: String) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(status = OrderStatus.PAID)
            } else order
        }
        addNotification(
            category = "Transaksi",
            title = "Pembayaran Berhasil Dikonfirmasi!",
            message = "Supplier segera memproses dan mengemas pesanan agritech Anda."
        )
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) order.copy(status = newStatus) else order
        }
    }

    // --- Titip Tani & Ternak (Pantau Panen) Booking ---
    fun joinPantauPanen(
        program: PantauPanenProgram,
        slots: Int,
        userName: String,
        userPhone: String,
        paymentMethod: String
    ): TitipOrder {
        val totalFee = program.feePerUnit * slots
        val vaNum = "8812" + (1000000000L..9999999999L).random()
        val titipOrder = TitipOrder(
            id = UUID.randomUUID().toString(),
            programId = program.id,
            programTitle = program.title,
            slots = slots,
            totalFee = totalFee,
            investorName = userName,
            investorPhone = userPhone,
            status = "Aktif Berjalan",
            paymentMethod = paymentMethod,
            vaNumber = vaNum,
            joinedDate = "Hari ini",
            type = program.type
        )

        // Update capacity in program
        _programs.value = _programs.value.map {
            if (it.id == program.id) it.copy(capacityBooked = it.capacityBooked + slots) else it
        }

        _titipOrders.value = listOf(titipOrder) + _titipOrders.value

        addNotification(
            category = "Pantau Panen",
            title = "Penitipan ${program.title} Berhasil!",
            message = "Anda dapat memantau perkembangan harian di Dashboard Pantau Panen."
        )

        return titipOrder
    }

    // --- Buyer Request Operations ---
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
        val req = BuyerRequest(
            id = UUID.randomUUID().toString(),
            title = title,
            category = category,
            buyerName = buyerName,
            buyerRole = buyerRole,
            quantity = quantity,
            unit = unit,
            targetPrice = targetPrice,
            location = location,
            deadline = deadline,
            description = description,
            offersCount = 0,
            status = "Terbuka"
        )
        _buyerRequests.value = listOf(req) + _buyerRequests.value
        addNotification(
            category = "Peluang Buyer",
            title = "Permintaan Komoditas Dipublikasikan",
            message = "Permintaan '$title' siap menerima penawaran dari petani & supplier."
        )
    }

    // --- Export Offer Submission ---
    fun submitExportOffer(
        listingId: String,
        commodity: String,
        supplierName: String,
        volume: String,
        priceUsd: String,
        originPort: String
    ) {
        val offer = ExportOffer(
            id = UUID.randomUUID().toString(),
            exportListingId = listingId,
            commodityName = commodity,
            supplierName = supplierName,
            offeredVolume = volume,
            price = priceUsd,
            currency = "USD",
            originPort = originPort,
            status = "Dalam Seleksi",
            date = "Hari ini"
        )
        _exportOffers.value = listOf(offer) + _exportOffers.value
        addNotification(
            category = "Ekspor",
            title = "Penawaran Ekspor Terkirim",
            message = "Penawaran untuk $commodity telah disampaikan ke International Trade Desk."
        )
    }

    // --- AI Doctor Diagnostic Logic ---
    fun runAiDiagnosis(
        category: String,
        subjectName: String,
        age: String,
        weight: String,
        symptoms: String
    ): AiConsultationRecord {
        val isAnimal = category.equals("Hewan", ignoreCase = true)
        val diagnosisText: String
        val confidence: Int
        val urgency: String
        val actions: List<String>
        val products: List<String>

        if (isAnimal) {
            when {
                symptoms.contains("perut", ignoreCase = true) || symptoms.contains("kembung", ignoreCase = true) -> {
                    diagnosisText = "Tympanites / Bloat Akut Ruminansia (Penumpukan Gas Lambung)"
                    confidence = 94
                    urgency = "Darurat / Hubungi Dokter"
                    actions = listOf(
                        "Posisikan ternak dengan bagian kepala lebih tinggi dari tubuh belakang",
                        "Berikan minyak nabati/mineral 250-500 ml secara oral untuk meredakan busa gas",
                        "Segera hubungi dokter hewan lapangan jika ternak mulai sesak napas"
                    )
                    products = listOf("Vitamin B-Kompleks Ternak Injeksi", "Bio-Probiotik Rumen Ternak")
                }
                symptoms.contains("batuk", ignoreCase = true) || symptoms.contains("ingus", ignoreCase = true) || symptoms.contains("napas", ignoreCase = true) -> {
                    diagnosisText = "Bovine Respiratory Disease (BRD) / Infeksi Saluran Pernapasan"
                    confidence = 88
                    urgency = "Sedang"
                    actions = listOf(
                        "Isolasi hewan ke kandang karantina berpenetrasi sirkulasi udara baik",
                        "Jaga lantai kandang tetap kering dan bebas dari amonia berlebih",
                        "Berikan antibiotik spektrum luas sesuai resep dokter hewan berizin"
                    )
                    products = listOf("Antibiotik Broad-Spectrum Ternak", "Multivitamin & Elektrolit")
                }
                else -> {
                    diagnosisText = "Gangguan Metabolisme & Penurunan Imunitas Ternak"
                    confidence = 82
                    urgency = "Rendah"
                    actions = listOf(
                        "Tingkatkan rasio hijauan berkualitas dan pakan konsentrat berenergi",
                        "Berikan suplemen vitamin dan mineral premix harian",
                        "Pastikan akses air bersih tak terbatas (ad libitum)"
                    )
                    products = listOf("Konsentrat Sapi Penggemukan Super", "Vitamin B-Kompleks Ternak Injeksi")
                }
            }
        } else {
            // Tanaman
            when {
                symptoms.contains("bercak", ignoreCase = true) || symptoms.contains("busuk", ignoreCase = true) || symptoms.contains("hitam", ignoreCase = true) -> {
                    diagnosisText = "Antraknosa (Colletotrichum capsici) / Busuk Buah & Daun"
                    confidence = 96
                    urgency = "Sedang"
                    actions = listOf(
                        "Petik dan musnahkan bagian buah atau daun yang telah terinfeksi agar tidak menular",
                        "Semprot fungisida berbahan aktif tembaga hidroksida atau azoksistrobin di pagi hari",
                        "Kurangi kelembapan tajuk dengan perempelan tunas air secara teratur"
                    )
                    products = listOf("Fungisida Antraknosa Hayati", "Alat Semprot Pertanian Elektrik 16L")
                }
                symptoms.contains("kuning", ignoreCase = true) || symptoms.contains("keriting", ignoreCase = true) -> {
                    diagnosisText = "Gemini Virus / Pepper Yellow Leaf Curl Virus (Vektor Kutu Kebul)"
                    confidence = 91
                    urgency = "Sedang"
                    actions = listOf(
                        "Kendalikan hama vektor (kutu kebul/Bemisia tabaci) menggunakan insektisida nabati atau kuning perekat",
                        "Aplikasi pupuk silika dan kalium tinggi untuk memperkuat dinding sel tanaman",
                        "Cabut tanaman yang terinfeksi parah untuk melindungi populasi lahan"
                    )
                    products = listOf("Insektisida Nabati Pengendali Hama", "Pupuk Kalium & Silika Cair")
                }
                else -> {
                    diagnosisText = "Defisiensi Unsur Hara Makro Sekunder (Kalsium & Magnesium)"
                    confidence = 85
                    urgency = "Rendah"
                    actions = listOf(
                        "Koreksi pH tanah dengan dolomit atau kalsium karbonat",
                        "Semprotkan pupuk kalsium cair pada daun di pagi hari sebelum stomata menutup",
                        "Jaga konsistensi kelembapan tanah agar penyerapan hara optimal"
                    )
                    products = listOf("Pupuk Organik Hayati Cair Bio-Agritech", "Kalsium Nitrat Plus Boron")
                }
            }
        }

        val record = AiConsultationRecord(
            id = UUID.randomUUID().toString(),
            category = category,
            subjectName = subjectName,
            symptoms = symptoms,
            diagnosis = diagnosisText,
            confidence = confidence,
            urgencyLevel = urgency,
            recommendations = actions,
            recommendedProducts = products,
            timestamp = "Hari ini, 15:45"
        )

        _consultationHistory.value = listOf(record) + _consultationHistory.value

        addNotification(
            category = "Konsultasi",
            title = "Analisis AI Selesai: $diagnosisText",
            message = "Tingkat keyakinan $confidence%. Rekomendasi penanganan telah tersedia."
        )

        return record
    }

    fun likeVideo(videoId: String) {
        _videos.value = _videos.value.map {
            if (it.id == videoId) it.copy(likesCount = it.likesCount + 1) else it
        }
    }

    fun addNotification(category: String, title: String, message: String) {
        val item = NotificationItem(
            id = UUID.randomUUID().toString(),
            category = category,
            title = title,
            message = message,
            timeAgo = "Baru saja",
            isRead = false
        )
        _notifications.value = listOf(item) + _notifications.value
    }

    // --- Mock Data Generators ---
    private fun getInitialProducts(): List<Product> = listOf(
        Product(
            id = "PRD-01",
            name = "Beras Organik Ciherang Premium (Vakum 5 Kg)",
            category = "Pertanian",
            price = 85000,
            originalPrice = 98000,
            unit = "Pcs",
            stock = 450,
            location = "Kab. Cianjur",
            rating = 4.9f,
            soldCount = 2840,
            supplierName = "Gapoktan Agro Cianjur Berkah",
            description = "Beras organik asli Cianjur varietas Ciherang, ditanam di sawah beririgasi pegunungan tanpa pestisida kimia sintetis. Bersertifikasi organik SNI.",
            specifications = mapOf("Berat Bersih" to "5 Kg", "Kadar Air" to "13%", "Sertifikasi" to "SNI Organik 6729:2016", "Kemasan" to "Vakum Food Grade"),
            isFlashSale = true
        ),
        Product(
            id = "PRD-02",
            name = "Cabai Rawit Merah Petik Segar Berkualitas",
            category = "Buah dan Sayuran",
            price = 42000,
            originalPrice = 55000,
            unit = "Kg",
            stock = 1200,
            location = "Kab. Malang",
            rating = 4.8f,
            soldCount = 4120,
            supplierName = "Tani Makmur Bumiaji",
            description = "Cabai rawit merah petik langsung dari lereng Gunung Panderman. Pedas menyengat, kesegaran terjamin, daya tahan simpan tinggi.",
            specifications = mapOf("Kondisi" to "Segar Petik Hari Ini", "Tingkat Kematangan" to "95% Merah", "Minimal Order" to "5 Kg"),
            isFlashSale = true
        ),
        Product(
            id = "PRD-03",
            name = "Sapi Limousin Jantan Bobot 450-500 Kg Siap Qurban/Potong",
            category = "Peternakan",
            price = 28500000,
            originalPrice = 31000000,
            unit = "Ekor",
            stock = 18,
            location = "Kab. Boyolali",
            rating = 5.0f,
            soldCount = 42,
            supplierName = "CV Boyolali Beef Livestock",
            description = "Sapi jantan Limousin kondisi prima, bebas PMK & LSD (Surat Keterangan Kesehatan Hewan / SKKH lengkap). Gratis perawatan dan antar sampai H-1.",
            specifications = mapOf("Jenis" to "Limousin Cross", "Bobot" to "480 Kg", "Status Kesehatan" to "SKKH Dinas Peternakan Lengkap", "Vaksinasi" to "Lengkap 3 Dosis")
        ),
        Product(
            id = "PRD-04",
            name = "Pupuk Hayati Cair Bio-Agritech 1000 ml",
            category = "Pupuk",
            price = 68000,
            originalPrice = 85000,
            unit = "Liter",
            stock = 850,
            location = "Kota Bandung",
            rating = 4.9f,
            soldCount = 6320,
            supplierName = "Terniva Bio Science Lab",
            description = "Konsorsium mikroba penambat nitrogen, pelarut fosfat, dan penghasil fitohormon alami untuk meningkatkan bobot panen hingga 35%.",
            specifications = mapOf("Volume" to "1.000 ml", "Bahan Aktif" to "Trichoderma, Pseudomonas, Azotobacter", "Izin Kementan" to "03.02.2023.112"),
            isFlashSale = true
        ),
        Product(
            id = "PRD-05",
            name = "Udang Vaname Segar Grade A (Size 30-40) Ekspor Quality",
            category = "Perikanan",
            price = 92000,
            originalPrice = 110000,
            unit = "Kg",
            stock = 600,
            location = "Kab. Situbondo",
            rating = 4.9f,
            soldCount = 1890,
            supplierName = "Tambak Bahari Nusantara",
            description = "Udang Vaname tambak intensif Situbondo, segar dingin langsung dibekukan dengan standar cold-chain HACCP internasional.",
            specifications = mapOf("Size" to "30-40 ekor/Kg", "Kemasan" to "Ice Box 10 Kg", "Sertifikasi" to "HACCP & IndoGAP")
        ),
        Product(
            id = "PRD-06",
            name = "Pakan Konsentrat Sapi Penggemukan Super 50 Kg",
            category = "Pakan",
            price = 240000,
            originalPrice = 270000,
            unit = "Karung",
            stock = 320,
            location = "Kab. Kediri",
            rating = 4.8f,
            soldCount = 980,
            supplierName = "Koperasi Peternak Mandiri Kediri",
            description = "Pakan konsentrat berprotein kasar 16%, TDN 72%. Mempercepat pertambahan bobot badan (ADG) hingga 1.3 - 1.6 Kg/hari.",
            specifications = mapOf("Protein Kasar" to "Min 16%", "Kadar Air" to "Maks 12%", "Berat" to "50 Kg", "Bentuk" to "Mash / Butiran Halus")
        ),
        Product(
            id = "PRD-07",
            name = "Alat Semprot Pertanian Elektrik 16 Liter 2in1",
            category = "Alat Pertanian",
            price = 485000,
            originalPrice = 580000,
            unit = "Unit",
            stock = 140,
            location = "Kota Semarang",
            rating = 4.8f,
            soldCount = 740,
            supplierName = "Agrindo Jaya Machinery",
            description = "Sprayer tangki elektrik otomatis 16L baterai lithium tahan 8 jam kerja. Lengkap dengan 4 jenis nozzle spuyer kuningan anti karat.",
            specifications = mapOf("Kapasitas Tangki" to "16 Liter", "Baterai" to "Lithium 12V 8Ah", "Tekanan" to "0.4 - 0.45 Mpa")
        ),
        Product(
            id = "PRD-08",
            name = "Benih Jagung Hibrida F1 Manis Madu 1 Kg",
            category = "Bibit dan Benih",
            price = 115000,
            originalPrice = 135000,
            unit = "Bungkus",
            stock = 500,
            location = "Kab. Jember",
            rating = 4.9f,
            soldCount = 1530,
            supplierName = "CV Benih Pertiwi Nusantara",
            description = "Benih jagung manis dengan tingkat kemanisan brix 14-16%. Daya simpan tongkol prima dan tahan bulai.",
            specifications = mapOf("Daya Tumbuh" to "90%", "Kemurnian" to "99%", "Potensi Hasil" to "18 Ton/Ha")
        )
    )

    private fun getInitialCart(): List<CartItem> = listOf(
        CartItem(
            product = getInitialProducts()[0], // Beras Ciherang
            quantity = 2,
            isSelected = true
        ),
        CartItem(
            product = getInitialProducts()[3], // Pupuk Hayati
            quantity = 3,
            isSelected = true
        )
    )

    private fun getInitialOrders(): List<Order> = listOf(
        Order(
            id = "ORD-001",
            orderNumber = "TRN-98432101",
            date = "11 Nov 2026, 10:15",
            items = listOf(
                CartItem(product = getInitialProducts()[1], quantity = 10, isSelected = true)
            ),
            subtotal = 420000,
            shippingCost = 35000,
            totalAmount = 455000,
            status = OrderStatus.SHIPPED,
            paymentMethod = "BCA Virtual Account",
            virtualAccountNumber = "8809123894729102",
            courierName = "JNE Agri Cargo Dingin (AWB: TRN991204)",
            recipientName = "Hotel Grand Sahid Jaya (Executive Chef)",
            recipientPhone = "081288991122",
            recipientAddress = "Jl. Jend. Sudirman Kav. 86, Tanah Abang, Jakarta Pusat"
        )
    )

    private fun getInitialPrograms(): List<PantauPanenProgram> = listOf(
        PantauPanenProgram(
            id = "PRG-01",
            title = "Titip Tani Padi Organik Ciherang Subang 10 Hektar",
            type = ProgramType.PERTANIAN,
            commodity = "Padi Ciherang SNI Organik",
            location = "Pagaden Barat, Kab. Subang, Jawa Barat",
            managerName = "Ki Ageng Suparman (Ketua Gapoktan Berkah Tani)",
            managerRole = "Agronom & Praktisi Organik 22 Tahun",
            capacityTotal = 50,
            capacityBooked = 38,
            feePerUnit = 2500000,
            unitLabel = "Lot (0.2 Hektar)",
            durationMonths = 4,
            harvestEstimate = "Maret 2027",
            description = "Program penitipan budidaya padi organik terpadu. Pengelola bertanggung jawab atas olah tanah, pupuk kandang matang, bibit bersertifikat, dan pengendalian hama ramah lingkungan. Bukan investasi fixed income, hasil panen dibagi sesuai kesepakatan transparan.",
            budgetBreakdown = listOf(
                BudgetItem("Pengolahan Lahan & Traktor", 500000, "Sewa traktor, pematang sawah, olah pupuk kandang"),
                BudgetItem("Bibit & Pupuk Hayati Organik", 750000, "Benih bersertifikat BPSB dan pupuk kompos matang"),
                BudgetItem("Biaya Perawatan & Pengendalian Hama", 800000, "Upah tenaga kerja tani harian dan pestisida nabati"),
                BudgetItem("Panen, Penggilingan & Karung", 450000, "Mesin combine harvester dan kemasan vakum beras")
            ),
            risks = listOf(
                "Anomali cuaca ekstrem (kemarau panjang atau banjir bandang)",
                "Fluktuasi harga gabah kering panen di tingkat pasar regional",
                "Risiko serangan organisme pengganggu tumbuhan (telah diproteksi sistem musuh alami)"
            ),
            termsAndConditions = "Penitip memperoleh hak hasil 65% gabah/beras bersih setelah panen atau dikonversikan ke nilai penjualan langsung ke mitra buyer Terniva. Laporan mingguan via aplikasi.",
            expectedYield = "1.2 - 1.5 Ton Beras Organik per Lot"
        ),
        PantauPanenProgram(
            id = "PRG-02",
            title = "Titip Ternak Sapi Limousin Penggemukan Idul Adha 2027",
            type = ProgramType.PETERNAKAN,
            commodity = "Sapi Limousin Jantan Bobot Awal 280 Kg",
            location = "Ampel, Kab. Boyolali, Jawa Tengah",
            managerName = "drh. Hendra Wicaksono & Tim Kandang Komunal",
            managerRole = "Dokter Hewan & Ahli Nutrisi Ruminansia",
            capacityTotal = 40,
            capacityBooked = 31,
            feePerUnit = 18500000,
            unitLabel = "Ekor Sapi Ber-Eartag IoT",
            durationMonths = 8,
            harvestEstimate = "Juni 2027 (Idul Adha)",
            description = "Penitipan pemeliharaan dan penggemukan intensif sapi Limousin. Setiap sapi dipasangi smart collar IoT pengukur denyut jantung, suhu, dan aktivitas rumput. Target ADG harian 1.2 Kg.",
            budgetBreakdown = listOf(
                BudgetItem("Pengadaan Bakalan Sapi Sehat", 12500000, "Bakalan jantan 280 Kg lolos uji SKKH bebas penyakit"),
                BudgetItem("Pakan Konsentrat & Silase Hijauan", 4200000, "Nutrisi presisi TDN 74% dan rumput odot fermentasi"),
                BudgetItem("Vaksinasi, Mineral & Tenaga Kandang", 1300000, "Vaksin PMK, deworming berkala, tenaga abrik kandang"),
                BudgetItem("Asuransi Ternak Jiwa", 500000, "Proteksi risiko kematian hewan akibat sakit/kecelakaan")
            ),
            risks = listOf(
                "Risiko morbiditas sakit ternak (ditangani tim dokter standby kandang 24 jam)",
                "Perubahan harga pakan konsentrat (sudah dikunci dengan kontrak suplier 1 tahun)"
            ),
            termsAndConditions = "Sapi dapat diambil langsung saat panen, atau dijualkan lewat ekosistem Pasar Hewan Terniva dengan margin keuntungan transparan.",
            expectedYield = "Target Bobot Akhir 520 - 580 Kg"
        ),
        PantauPanenProgram(
            id = "PRG-03",
            title = "Kemitraan Kambing Boer Pedaging Unggul",
            type = ProgramType.PETERNAKAN,
            commodity = "Kambing Boer F3 Pejantan",
            location = "Cijeruk, Kab. Bogor, Jawa Barat",
            managerName = "Ust. Mulyadi (Santri Tani Mandiri)",
            managerRole = "Praktisi Peternakan Terpadu",
            capacityTotal = 60,
            capacityBooked = 45,
            feePerUnit = 2800000,
            unitLabel = "Ekor Kambing",
            durationMonths = 6,
            harvestEstimate = "Mei 2027",
            description = "Pemeliharaan kambing Boer ras pedaging di kandang panggung modern higienis. Bebas bau dengan aplikasi biosanitasi bakteri fotosintetik.",
            budgetBreakdown = listOf(
                BudgetItem("Bibit Bakalan Kambing", 1800000, "Cempe lepas sapih bobot 18-20 Kg"),
                BudgetItem("Pakan Komplit Fermentasi", 700000, "Konsentrat ampas tahu, jagung, premix"),
                BudgetItem("Perawatan Kesehatan & Obat", 300000, "Obat cacing, vitamin dan vaksin")
            ),
            risks = listOf("Fluktuasi suhu dingin musim hujan Bogor (kandang tertutup tirai malam)"),
            termsAndConditions = "Bagi hasil 70% untuk penitip dari selisih nilai kenaikan bobot akhir dikurangi biaya pokok.",
            expectedYield = "Kenaikan bobot 20 Kg menjadi 42-45 Kg"
        )
    )

    private fun getInitialTitipOrders(): List<TitipOrder> = listOf(
        TitipOrder(
            id = "TTP-001",
            programId = "PRG-02",
            programTitle = "Titip Ternak Sapi Limousin Penggemukan Idul Adha 2027",
            slots = 1,
            totalFee = 18500000,
            investorName = "Budi Hartono",
            investorPhone = "081122334455",
            status = "Aktif Berjalan",
            paymentMethod = "Mandiri Virtual Account",
            vaNumber = "8812903810294812",
            joinedDate = "01 Nov 2026",
            type = ProgramType.PETERNAKAN
        )
    )

    private fun getInitialBuyerRequests(): List<BuyerRequest> = listOf(
        BuyerRequest(
            id = "REQ-01",
            title = "Kebutuhan Sayuran Hidroponik Campur untuk Hotel Bintang 5",
            category = "Buah dan Sayuran",
            buyerName = "Hotel Tentrem Yogyakarta",
            buyerRole = "Hotel & Resort",
            quantity = "500",
            unit = "Kg / Minggu",
            targetPrice = "Rp 35.000 / Kg",
            location = "Kota Yogyakarta",
            deadline = "20 Nov 2026",
            description = "Mencari supplier tetap selada romaine, pakcoy, butterhead, dan kale hidroponik bersertifikat GAP untuk suplai rutin breakfast buffet.",
            offersCount = 4
        ),
        BuyerRequest(
            id = "REQ-02",
            title = "Pasokan Daging Sapi Sirloin & Tenderloin Segar Harian",
            category = "Peternakan",
            buyerName = "Holycow Steakhouse Group",
            buyerRole = "Restaurant Chain",
            quantity = "2",
            unit = "Ton / Bulan",
            targetPrice = "Rp 125.000 / Kg",
            location = "Jakarta Selatan",
            deadline = "25 Nov 2026",
            description = "Dibutuhkan Rumah Potong Hewan (RPH) bersertifikat Halal MUI dengan fasilitas blast freezer dan armada truk pendingin.",
            offersCount = 7
        ),
        BuyerRequest(
            id = "REQ-03",
            title = "Pengadaan Jagung Pipil Kering Pakan Ternak Kadar Air 14%",
            category = "Pakan",
            buyerName = "PT Charoen Pokphand Unit Feedmill",
            buyerRole = "Industri Pakan",
            quantity = "50",
            unit = "Ton",
            targetPrice = "Rp 5.200 / Kg",
            location = "Kab. Sidoarjo, Jatim",
            deadline = "30 Nov 2026",
            description = "Bebas aflatoksin >20ppb, bersih dari kotoran batu/tongkol. Pembayaran tempo 14 hari kerja setelah timbang pabrik.",
            offersCount = 12
        )
    )

    private fun getInitialExportListings(): List<ExportListing> = listOf(
        ExportListing(
            id = "EXP-01",
            commodityName = "Kopi Arabika Gayo Green Beans Specialty",
            destinationCountry = "Hamburg, Jerman",
            flagEmoji = "🇩🇪",
            volume = "20",
            unit = "Ton (1 FCL)",
            qualityGrade = "Grade 1, Cupping Score 84+, Defect <5%",
            certification = "Rainforest Alliance / Fairtrade Certified",
            packaging = "Jute Bag 60 Kg dengan GrainPro Liner",
            incoterms = "FOB Tanjung Priok, Jakarta",
            portDestination = "Port of Hamburg",
            deliverySchedule = "Januari 2027",
            paymentTerms = "Letter of Credit (L/C at sight)",
            deadline = "15 Des 2026"
        ),
        ExportListing(
            id = "EXP-02",
            commodityName = "Kelapa Bulat Semi-Husked Mature Coconut",
            destinationCountry = "Ningbo, Tiongkok",
            flagEmoji = "🇨🇳",
            volume = "100",
            unit = "Ton (5 Kontainer)",
            qualityGrade = "Bobot 800-1100 gram/butir, air penuh",
            certification = "Phytosanitary Certificate Badan Karantina",
            packaging = "Mesh Bag 50 Kg",
            incoterms = "CIF Ningbo Port",
            portDestination = "Ningbo-Zhoushan Port",
            deliverySchedule = "Desember 2026",
            paymentTerms = "TT 30% DP, 70% against BL scan",
            deadline = "28 Nov 2026"
        ),
        ExportListing(
            id = "EXP-03",
            commodityName = "Vanili Kering Gourmet Pods (Planifolia)",
            destinationCountry = "California, Amerika Serikat",
            flagEmoji = "🇺🇸",
            volume = "500",
            unit = "Kg",
            qualityGrade = "Panjang 18-20 cm, Kadar Air 30-35%, Vanilin >2.2%",
            certification = "USDA Organic & FDA Prior Notice",
            packaging = "Vakum Bag 1 Kg dalam Master Box Karton",
            incoterms = "Air Freight CFR Los Angeles (LAX)",
            portDestination = "Los Angeles International Airport",
            deliverySchedule = "Februari 2027",
            paymentTerms = "Escrow via Terniva Trade Gateway",
            deadline = "10 Jan 2027"
        )
    )

    private fun getInitialExportOffers(): List<ExportOffer> = listOf(
        ExportOffer(
            id = "EOF-01",
            exportListingId = "EXP-01",
            commodityName = "Kopi Arabika Gayo Green Beans",
            supplierName = "Koperasi Produsen Kopi Gayo Mandiri",
            offeredVolume = "20 Ton",
            price = "USD 7.40 / Kg",
            currency = "USD",
            originPort = "Pelabuhan Belawan Medan",
            status = "Negosiasi",
            date = "08 Nov 2026"
        )
    )

    private fun getInitialDoctors(): List<DoctorProfile> = listOf(
        DoctorProfile(
            id = "DOC-01",
            name = "Dr. drh. Ahmad Kurniawan, Sp.MP",
            title = "Spesialis Reproduksi & Kesehatan Ruminansia Besar",
            specialization = "Sapi Potong, Sapi Perah & Kambing",
            category = "Dokter Hewan",
            rating = 4.9f,
            experienceYears = 14,
            consultFee = 45000,
            strNumber = "STRV: 3174/PBPDHI/2012",
            availableSchedule = "Hari ini, 08:00 - 21:00 (Online)",
            bio = "Praktisi kesehatan ternak dan konsultan kandang komunal berlisensi. Telah menangani lebih dari 4.000 kasus penyakit peternakan rakyat."
        ),
        DoctorProfile(
            id = "DOC-02",
            name = "drh. Siti Rahmawati, M.Si",
            title = "Dokter Unggas & Sanitasi Kandang Modern",
            specialization = "Ayam Broiler, Petelur & Bebek",
            category = "Dokter Hewan",
            rating = 4.9f,
            experienceYears = 9,
            consultFee = 35000,
            strNumber = "STRV: 3201/PBPDHI/2017",
            availableSchedule = "Hari ini, 09:00 - 18:00 (Online)",
            bio = "Fokus pada biosekuriti kandang, manajemen vaksinasi unggas dan penanganan stres lingkungan ayam petelur."
        ),
        DoctorProfile(
            id = "DOC-03",
            name = "Ir. Bambang Sujarwo, MP",
            title = "Agronom Senior Tanah & Hama Tanaman Hortikultura",
            specialization = "Padi, Cabai, Bawang & Buah Hidroponik",
            category = "Agronom Tanaman",
            rating = 4.8f,
            experienceYears = 18,
            consultFee = 40000,
            strNumber = "SKA Agronom: 12.09.8821.11",
            availableSchedule = "Hari ini, 08:00 - 20:00 (Online)",
            bio = "Pakar perlindungan tanaman IPB. Berpengalaman menyelesaikan epidemi antraknosa, gemini virus, dan pengkondisian pH tanah masam."
        )
    )

    private fun getInitialConsultations(): List<AiConsultationRecord> = listOf(
        AiConsultationRecord(
            id = "CNS-01",
            category = "Tanaman",
            subjectName = "Tanaman Cabai Rawit Merah (Fase Berbunga)",
            symptoms = "Daun muda menggulung keriting ke atas, warna kekuningan belang-belang, bunga rontok.",
            diagnosis = "Pepper Yellow Leaf Curl Virus (Gemini Virus)",
            confidence = 92,
            urgencyLevel = "Sedang",
            recommendations = listOf(
                "Kendalikan serangga vektor Bemisia tabaci (kutu kebul) menggunakan yellow sticky trap",
                "Semprotkan ekstrak daun nimba / insektisida nabati secara berkala 3 hari sekali",
                "Berikan pupuk daun berunsur mikro seng (Zn) dan boron untuk memperkuat ketahanan pucuk"
            ),
            recommendedProducts = listOf("Insektisida Nabati Pengendali Hama", "Pupuk Hayati Cair Bio-Agritech 1000 ml"),
            timestamp = "Kemarin, 14:20"
        )
    )

    private fun getInitialLiveStreams(): List<LiveStreamItem> = listOf(
        LiveStreamItem(
            id = "LIV-01",
            title = "🔴 LIVE: Panen Melon Sultan Hidroponik Greenhouse Subang",
            hostName = "Agro Smart Farm Subang",
            hostBadge = "Star Seller Petani",
            viewersCount = 1420,
            isLive = true,
            pinnedProductName = "Melon Inthanon Hidroponik Grade A (Manis Brix 15)",
            pinnedProductPrice = 55000
        ),
        LiveStreamItem(
            id = "LIV-02",
            title = "🔴 LIVE: Review Bibit Sapi Limousin Bakalan Super Boyolali",
            hostName = "Kandang Lembu Boyolali",
            hostBadge = "Official Store",
            viewersCount = 890,
            isLive = true,
            pinnedProductName = "Sapi Limousin Jantan Bobot 450-500 Kg",
            pinnedProductPrice = 28500000
        )
    )

    private fun getInitialVideos(): List<VideoFeedItem> = listOf(
        VideoFeedItem(
            id = "VID-01",
            title = "Cara Bikin Pakan Silase Fermentasi Rumput Odot Awet 6 Bulan Tanpa Busuk! Ternak makan lahap.",
            creatorName = "Pakde Peternak Mandiri",
            creatorHandle = "@pakde_peternak_mandiri",
            soundTrack = "Suara Asli - Tips Silase Terniva",
            durationText = "01:15",
            likesCount = 3840,
            commentsCount = 214,
            sharesCount = 680,
            tags = listOf("#PakanFermentasi", "#TernakSapi", "#Silase"),
            pinnedProductName = "Pakan Konsentrat Sapi Penggemukan Super 50 Kg",
            pinnedProductPrice = 240000,
            productId = "PRD-06",
            discountTag = "-11%",
            bgPrimaryColor = 0xFF14532D,
            bgSecondaryColor = 0xFF052E16
        ),
        VideoFeedItem(
            id = "VID-02",
            title = "Rahasia Cabai Rawit Berbuah Lebat anti Rontok Musim Hujan Pakai Mikroba Hayati Cair!",
            creatorName = "Dokter Tani Nusantara",
            creatorHandle = "@dokter_tani_nusantara",
            soundTrack = "Suara Asli - Edukasi Petani Modern",
            durationText = "00:58",
            likesCount = 5120,
            commentsCount = 432,
            sharesCount = 1200,
            tags = listOf("#CabaiRawit", "#PupukHayati", "#PetaniModern"),
            pinnedProductName = "Pupuk Hayati Cair Bio-Agritech 1000 ml",
            pinnedProductPrice = 68000,
            productId = "PRD-04",
            discountTag = "-20%",
            bgPrimaryColor = 0xFF065F46,
            bgSecondaryColor = 0xFF022C22
        ),
        VideoFeedItem(
            id = "VID-03",
            title = "🔴 LIVE PANEN: Beras Organik Ciherang Panen Raya di Sawah Subang Siap Kirim Hari Ini!",
            creatorName = "Gapoktan Agro Cianjur",
            creatorHandle = "@gapoktan_cianjur",
            soundTrack = "Suara Asli - Panen Raya Padi",
            durationText = "LIVE",
            likesCount = 7420,
            commentsCount = 589,
            sharesCount = 1430,
            tags = listOf("#BerasOrganik", "#PanenRaya", "#Subang"),
            pinnedProductName = "Beras Organik Ciherang Premium (Vakum 5 Kg)",
            pinnedProductPrice = 85000,
            productId = "PRD-01",
            discountTag = "-13%",
            isLive = true,
            bgPrimaryColor = 0xFF1E3A8A,
            bgSecondaryColor = 0xFF0F172A
        ),
        VideoFeedItem(
            id = "VID-04",
            title = "Review Bakalan Sapi Limousin Boyolali Sehat Siap Qurban! Sertifikat SKKH Lengkap.",
            creatorName = "Boyolali Beef Livestock",
            creatorHandle = "@boyolali_beef",
            soundTrack = "Suara Asli - Kandang Lembu Modern",
            durationText = "01:20",
            likesCount = 2890,
            commentsCount = 175,
            sharesCount = 420,
            tags = listOf("#SapiLimousin", "#Qurban2027", "#PeternakBoyolali"),
            pinnedProductName = "Sapi Limousin Jantan Bobot 450-500 Kg Siap Qurban",
            pinnedProductPrice = 28500000,
            productId = "PRD-03",
            discountTag = "-8%",
            bgPrimaryColor = 0xFF78350F,
            bgSecondaryColor = 0xFF451A03
        ),
        VideoFeedItem(
            id = "VID-05",
            title = "Review Praktis Sprayer Elektrik 16L Otomatis Baterai Tahan Seharian di Lahan",
            creatorName = "Agrindo Jaya Machinery",
            creatorHandle = "@agrindo_machinery",
            soundTrack = "Suara Asli - Review Alat Tani",
            durationText = "00:45",
            likesCount = 1940,
            commentsCount = 98,
            sharesCount = 230,
            tags = listOf("#SprayerElektrik", "#AlatPertanian", "#Terniva"),
            pinnedProductName = "Alat Semprot Pertanian Elektrik 16 Liter 2in1",
            pinnedProductPrice = 485000,
            productId = "PRD-07",
            discountTag = "-16%",
            bgPrimaryColor = 0xFF334155,
            bgSecondaryColor = 0xFF0F172A
        )
    )

    private fun getInitialNotifications(): List<NotificationItem> = listOf(
        NotificationItem(
            id = "NOTIF-01",
            category = "Transaksi",
            title = "Pesanan Dibuat #TRN-98432101",
            message = "Pesanan Anda sedang dikirim oleh kurir JNE Agri Cargo Dingin.",
            timeAgo = "1 jam lalu",
            isRead = false
        ),
        NotificationItem(
            id = "NOTIF-02",
            category = "Pantau Panen",
            title = "Laporan Mingguan: Sapi Limousin #TRN-SP-042",
            message = "Bobot badan naik menjadi 312.4 Kg (ADG 1.35 Kg/hari). Kondisi sangat sehat.",
            timeAgo = "3 jam lalu",
            isRead = false
        ),
        NotificationItem(
            id = "NOTIF-03",
            category = "Ekspor",
            title = "Peluang Baru: Permintaan Kopi Arabika ke Jerman",
            message = "Buyer Hamburg mencari pasokan 20 Ton green beans specialty grade 1.",
            timeAgo = "1 hari lalu",
            isRead = true
        )
    )

    // Detailed mock logs for Pantau Panen Crop Monitoring
    fun getCropLogs(): List<CropLog> = listOf(
        CropLog("Hari ini, 07:00", "Perawatan", "Pemberian pupuk hayati cair mikrobial penguat anakan", "Dosis 5 ml/liter air pada petak A dan B. Kelembapan tanah stabil.", 82, 25),
        CropLog("3 Hari lalu", "Perawatan", "Pengendalian gulma & penyiangan mekanis", "Pembersihan rumput teki dan aplikasi perangkap hama kepik.", 79, 27),
        CropLog("1 Minggu lalu", "Pertumbuhan", "Pengairan berkala & monitoring tinggi tanaman", "Tinggi tanaman rata-rata 38 cm, anakan produktif 24 batang per rumpun.", 85, 26),
        CropLog("3 Minggu lalu", "Penanaman", "Pindah tanam bibit umur 15 hari dari persemaian", "Metode tanam jajar legowo 2:1 dengan jarak tanam 25x12.5 cm.", 88, 28)
    )

    // Detailed mock records for Livestock Individual Tag Tracking
    fun getAnimalRecord(): AnimalIndividualRecord = AnimalIndividualRecord(
        id = "ANM-001",
        tagId = "TRN-SP-042",
        breed = "Limousin Cross Jantan",
        ageMonths = 22,
        weightKg = 312.4f,
        initialWeightKg = 280.0f,
        healthStatus = "Sangat Sehat",
        temperature = 38.5f,
        farmerName = "Mas Joko Susilo",
        penLocation = "Kandang Komunal Blok B-04, Ampel Boyolali",
        feedLogs = listOf(
            "Hari ini 06:30 - Silase Jagung Fermentasi 15 Kg + Konsentrat 5 Kg",
            "Hari ini 14:00 - Rumput Gajah Segar Chopper 12 Kg + Suplemen Mineral",
            "Kemarin 07:00 - Silase Jagung 14 Kg + Konsentrat TDN 74% 5 Kg"
        ),
        vaccineLogs = listOf(
            "10 Nov 2026 - Vaksinasi Booster PMK Dosis II (drh. Hendra)",
            "02 Nov 2026 - Pemberian Obat Cacing Albendazole & Vitamin ADE",
            "20 Okt 2026 - Pemasangan Eartag IoT & Uji Serologi Negatif Antraks"
        ),
        checkupSchedule = "Pemeriksaan USG & Timbang Berkala: 18 Nov 2026"
    )
}
