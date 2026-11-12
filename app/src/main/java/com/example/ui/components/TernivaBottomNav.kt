package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TernivaGreen

@Composable
fun TernivaBottomNav(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    unreadNotificationCount: Int = 2
) {
    NavigationBar(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("bottom_navigation_bar"),
        containerColor = if (selectedTab == 2) Color.Black else Color.White,
        tonalElevation = if (selectedTab == 2) 0.dp else 8.dp
    ) {
        // 1. Untuk Kamu
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == 0) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                    contentDescription = "Untuk Kamu",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Untuk Kamu",
                    fontSize = 10.sp,
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (selectedTab == 2) Color.White else TernivaGreen,
                selectedTextColor = if (selectedTab == 2) Color.White else TernivaGreen,
                unselectedIconColor = if (selectedTab == 2) Color(0xFF94A3B8) else Color(0xFF64748B),
                unselectedTextColor = if (selectedTab == 2) Color(0xFF94A3B8) else Color(0xFF64748B),
                indicatorColor = if (selectedTab == 2) Color.White.copy(alpha = 0.15f) else TernivaGreen.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_tab_home")
        )

        // 2. Peluang (Ekspor & Buyer Request)
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == 1) Icons.Filled.TrendingUp else Icons.Outlined.TrendingUp,
                    contentDescription = "Peluang",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Peluang",
                    fontSize = 10.sp,
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (selectedTab == 2) Color.White else TernivaGreen,
                selectedTextColor = if (selectedTab == 2) Color.White else TernivaGreen,
                unselectedIconColor = if (selectedTab == 2) Color(0xFF94A3B8) else Color(0xFF64748B),
                unselectedTextColor = if (selectedTab == 2) Color(0xFF94A3B8) else Color(0xFF64748B),
                indicatorColor = if (selectedTab == 2) Color.White.copy(alpha = 0.15f) else TernivaGreen.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_tab_peluang")
        )

        // 3. Live & Video
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == 2) Icons.Filled.PlayCircle else Icons.Outlined.PlayCircleOutline,
                    contentDescription = "Live & Video",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Live & Video",
                    fontSize = 10.sp,
                    fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (selectedTab == 2) Color.White else TernivaGreen,
                selectedTextColor = if (selectedTab == 2) Color.White else TernivaGreen,
                unselectedIconColor = if (selectedTab == 2) Color(0xFF94A3B8) else Color(0xFF64748B),
                unselectedTextColor = if (selectedTab == 2) Color(0xFF94A3B8) else Color(0xFF64748B),
                indicatorColor = if (selectedTab == 2) Color.White.copy(alpha = 0.15f) else TernivaGreen.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_tab_live")
        )

        // 4. Notifikasi
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadNotificationCount > 0) {
                            Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                                Text(unreadNotificationCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (selectedTab == 3) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = "Notifikasi",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = {
                Text(
                    text = "Notifikasi",
                    fontSize = 10.sp,
                    fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (selectedTab == 2) Color.White else TernivaGreen,
                selectedTextColor = if (selectedTab == 2) Color.White else TernivaGreen,
                unselectedIconColor = if (selectedTab == 2) Color(0xFF94A3B8) else Color(0xFF64748B),
                unselectedTextColor = if (selectedTab == 2) Color(0xFF94A3B8) else Color(0xFF64748B),
                indicatorColor = if (selectedTab == 2) Color.White.copy(alpha = 0.15f) else TernivaGreen.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_tab_notifications")
        )

        // 5. Akun
        NavigationBarItem(
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == 4) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Akun",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Akun",
                    fontSize = 10.sp,
                    fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (selectedTab == 2) Color.White else TernivaGreen,
                selectedTextColor = if (selectedTab == 2) Color.White else TernivaGreen,
                unselectedIconColor = if (selectedTab == 2) Color(0xFF94A3B8) else Color(0xFF64748B),
                unselectedTextColor = if (selectedTab == 2) Color(0xFF94A3B8) else Color(0xFF64748B),
                indicatorColor = if (selectedTab == 2) Color.White.copy(alpha = 0.15f) else TernivaGreen.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_tab_account")
        )
    }
}
