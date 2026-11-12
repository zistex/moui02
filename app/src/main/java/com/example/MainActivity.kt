package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.TernivaBottomNav
import com.example.ui.components.TernivaTopBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TernivaApp()
            }
        }
    }
}

@Composable
fun TernivaApp(
    viewModel: TernivaViewModel = viewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    val unreadCount = remember(notifications) { notifications.count { !it.isRead } }

    // Intercept back button when deep in nested screen or tabs
    BackHandler(enabled = currentScreen !is CurrentScreen.MainNavigation || selectedTab != 0) {
        if (currentScreen !is CurrentScreen.MainNavigation) {
            viewModel.navigateBack()
        } else if (selectedTab != 0) {
            viewModel.selectTab(0)
        }
    }

    val isMainNavigation = currentScreen is CurrentScreen.MainNavigation

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isMainNavigation && selectedTab == 0) {
                TernivaTopBar(
                    searchQuery = searchQuery,
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    cartCount = cartItems.size,
                    onCartClick = { viewModel.navigateTo(CurrentScreen.Cart) },
                    onChatClick = { viewModel.selectTab(3) }
                )
            }
        },
        bottomBar = {
            if (isMainNavigation) {
                TernivaBottomNav(
                    selectedTab = selectedTab,
                    onTabSelected = { viewModel.selectTab(it) },
                    unreadNotificationCount = unreadCount
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is CurrentScreen.Splash -> {
                    SplashScreen(viewModel = viewModel)
                }
                is CurrentScreen.Login -> {
                    LoginScreen(viewModel = viewModel)
                }
                is CurrentScreen.Register -> {
                    RegisterScreen(viewModel = viewModel)
                }
                is CurrentScreen.OtpVerification -> {
                    OtpVerificationScreen(
                        phoneOrEmail = screen.phoneOrEmail,
                        isRegister = screen.isRegister,
                        registeredName = screen.registeredName,
                        registeredRole = screen.registeredRole,
                        viewModel = viewModel
                    )
                }
                is CurrentScreen.MainNavigation -> {
                    when (selectedTab) {
                        0 -> HomeScreen(viewModel = viewModel)
                        1 -> PeluangScreen(viewModel = viewModel)
                        2 -> LiveVideoScreen(viewModel = viewModel)
                        3 -> NotificationScreen(viewModel = viewModel)
                        4 -> AccountScreen(viewModel = viewModel)
                    }
                }
                is CurrentScreen.ProductDetail -> {
                    ProductDetailScreen(product = screen.product, viewModel = viewModel)
                }
                is CurrentScreen.Cart -> {
                    CartScreen(viewModel = viewModel)
                }
                is CurrentScreen.Checkout -> {
                    CheckoutScreen(items = screen.items, viewModel = viewModel)
                }
                is CurrentScreen.OrderSuccess -> {
                    OrderSuccessScreen(order = screen.order, viewModel = viewModel)
                }
                is CurrentScreen.OrderTracking -> {
                    OrderTrackingScreen(order = screen.order, viewModel = viewModel)
                }
                is CurrentScreen.MyOrders -> {
                    MyOrdersScreen(initialStatus = screen.initialStatus, viewModel = viewModel)
                }
                is CurrentScreen.PantauPanenCatalog -> {
                    PantauPanenCatalogScreen(viewModel = viewModel)
                }
                is CurrentScreen.PantauPanenDetail -> {
                    PantauPanenProgramDetailScreen(program = screen.program, viewModel = viewModel)
                }
                is CurrentScreen.TitipBookingForm -> {
                    TitipBookingFormScreen(program = screen.program, viewModel = viewModel)
                }
                is CurrentScreen.ActiveMonitoring -> {
                    ActiveMonitoringDashboardScreen(titipOrder = screen.titipOrder, viewModel = viewModel)
                }
                is CurrentScreen.ProductSearch -> {
                    ProductSearchScreen(
                        initialQuery = screen.initialQuery,
                        initialCategory = screen.initialCategory,
                        viewModel = viewModel
                    )
                }
                is CurrentScreen.SupplierDirectory -> {
                    SupplierDirectoryScreen(viewModel = viewModel)
                }
                is CurrentScreen.BuyerRequestsHub -> {
                    BuyerRequestsHubScreen(viewModel = viewModel)
                }
                is CurrentScreen.ExportHub, is CurrentScreen.ExportListingDetail -> {
                    ExportHubScreen(viewModel = viewModel)
                }
                is CurrentScreen.AiConsultationHub -> {
                    AiConsultationHubScreen(viewModel = viewModel)
                }
                is CurrentScreen.ChatRoom -> {
                    ChatScreen(supplierName = screen.supplierName, linkedProduct = screen.product, viewModel = viewModel)
                }
                is CurrentScreen.StoreProfile -> {
                    StoreProfileScreen(supplierName = screen.supplierName, location = screen.location, viewModel = viewModel)
                }
            }
        }
    }
}
