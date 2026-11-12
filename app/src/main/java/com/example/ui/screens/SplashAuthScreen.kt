package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.TernivaGreen
import com.example.ui.theme.TernivaGreenDeep
import com.example.ui.theme.TernivaHarvestGold
import com.example.ui.viewmodel.CurrentScreen
import com.example.ui.viewmodel.TernivaViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(1800) // 1.8s splash presentation
        if (viewModel.isUserLoggedIn.value) {
            viewModel.navigateTo(CurrentScreen.MainNavigation)
        } else {
            viewModel.navigateTo(CurrentScreen.Login)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        TernivaGreen,
                        TernivaGreenDeep
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            // Shopee-style glowing icon badge
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Agriculture,
                    contentDescription = "Terniva Logo",
                    tint = TernivaGreen,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "TERNIVA",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp,
                color = Color.White
            )

            Text(
                text = "Smart Agritech & Direct Farm Marketplace",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.88f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            CircularProgressIndicator(
                color = TernivaHarvestGold,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Menghubungkan Petani, Peternak, dan Pembeli Langsung",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )
        }

        // Bottom Footer
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Aman & Terenkripsi 256-Bit SSL",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Text(
                text = "v1.0.0 • Kementerian Pertanian & Mitra RI",
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    var phoneOrEmail by remember { mutableStateOf("081234567890") }
    var password by remember { mutableStateOf("Terniva2026!") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var rememberMe by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Masuk Akun", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(CurrentScreen.MainNavigation) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tutup / Eksplorasi")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.navigateTo(CurrentScreen.Register) }) {
                        Text("Daftar", color = TernivaGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo & Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TernivaGreen.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Eco, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Masuk ke Terniva", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E293B))
                    Text("Akses marketplace agritech, titip panen & ekspor", fontSize = 11.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Phone or Email Input
            OutlinedTextField(
                value = phoneOrEmail,
                onValueChange = {
                    phoneOrEmail = it
                    errorMessage = null
                },
                label = { Text("No. Handphone / Email") },
                placeholder = { Text("Contoh: 08123456789 atau nama@email.com") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TernivaGreen)
                },
                trailingIcon = {
                    if (phoneOrEmail.isNotEmpty()) {
                        IconButton(onClick = { phoneOrEmail = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Hapus", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_login_username"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TernivaGreen,
                    focusedLabelColor = TernivaGreen
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Kata Sandi") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = TernivaGreen)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Sembunyikan sandi" else "Tampilkan sandi"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_login_password"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TernivaGreen,
                    focusedLabelColor = TernivaGreen
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Options: Remember Me & Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = TernivaGreen)
                    )
                    Text("Ingat saya", fontSize = 12.sp, color = Color(0xFF334155))
                }
                Text(
                    text = "Lupa Sandi?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TernivaGreen,
                    modifier = Modifier.clickable {
                        errorMessage = "Link reset sandi aman telah dikirim via SMS/WhatsApp terdaftar."
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Login Button (Shopee Style Full Width Button)
            Button(
                onClick = {
                    if (phoneOrEmail.isBlank() || password.isBlank()) {
                        errorMessage = "Silakan masukkan nomor HP/email dan kata sandi."
                    } else {
                        viewModel.login(phoneOrEmail, "Budi Santoso", UserRole.BUYER)
                        viewModel.navigateTo(CurrentScreen.MainNavigation)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_submit_login"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
            ) {
                Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Masuk", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Login via WhatsApp OTP
            OutlinedButton(
                onClick = {
                    viewModel.navigateTo(CurrentScreen.OtpVerification(phoneOrEmail, isRegister = false))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("btn_login_whatsapp_otp"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF25D366)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF25D366))
            ) {
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Masuk dengan Kode OTP WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divider or Social login
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                Text("  ATAU  ", fontSize = 11.sp, color = Color.Gray)
                Divider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Demo 1-Tap Logins for Testing Roles
            Text(
                text = "⚡ Akses Cepat Demo Peran:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.login("081299990001", "Pak Budi (Pembeli)", UserRole.BUYER)
                        viewModel.navigateTo(CurrentScreen.MainNavigation)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text("🛒 Pembeli", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TernivaGreen)
                }

                OutlinedButton(
                    onClick = {
                        viewModel.login("081299990002", "Koperasi Cianjur (Supplier)", UserRole.SUPPLIER)
                        viewModel.navigateTo(CurrentScreen.MainNavigation)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text("🌾 Supplier", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TernivaHarvestGold)
                }

                OutlinedButton(
                    onClick = {
                        viewModel.login("081299990003", "Kelompok Panen (Pengelola)", UserRole.STEWARD)
                        viewModel.navigateTo(CurrentScreen.MainNavigation)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text("🐂 Pengelola", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F766E))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer info
            Text(
                text = "Dengan masuk, Anda menyetujui Ketentuan Layanan & Kebijakan Privasi Terniva Agritech.",
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("") }
    var phoneOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.BUYER) }
    var agreeToTerms by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Akun Baru", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(CurrentScreen.Login) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali ke Login")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.navigateTo(CurrentScreen.Login) }) {
                        Text("Masuk", color = TernivaGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Buat Akun Terniva",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1E293B),
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Satu akun untuk belanja hasil tani, titip kelola panen, dan bermitra.",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Full Name Input
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nama Lengkap / Nama Bisnis") },
                placeholder = { Text("Contoh: Budi Santoso / Resto Alam Segar") },
                leadingIcon = {
                    Icon(Icons.Default.Badge, contentDescription = null, tint = TernivaGreen)
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_register_name"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TernivaGreen,
                    focusedLabelColor = TernivaGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Phone/Email
            OutlinedTextField(
                value = phoneOrEmail,
                onValueChange = { phoneOrEmail = it },
                label = { Text("No. Handphone (WhatsApp Aktif)") },
                placeholder = { Text("08xxxxxxxxxx") },
                leadingIcon = {
                    Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = TernivaGreen)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_register_phone"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TernivaGreen,
                    focusedLabelColor = TernivaGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Kata Sandi Baru (Min. 8 Karakter)") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = TernivaGreen)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Sembunyikan" else "Tampilkan"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_register_password"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TernivaGreen,
                    focusedLabelColor = TernivaGreen
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Role Selection (Peran Akun Utama)
            Text(
                text = "Pilih Peran Utama Anda di Terniva:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UserRole.values().forEach { role ->
                    val isSelected = selectedRole == role
                    Surface(
                        color = if (isSelected) TernivaGreen.copy(alpha = 0.12f) else Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) TernivaGreen else Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedRole = role }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = when (role) {
                                    UserRole.BUYER -> Icons.Default.ShoppingCart
                                    UserRole.SUPPLIER -> Icons.Default.Agriculture
                                    UserRole.STEWARD -> Icons.Default.Pets
                                },
                                contentDescription = null,
                                tint = if (isSelected) TernivaGreen else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = role.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) TernivaGreen else Color(0xFF334155),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreeToTerms,
                    onCheckedChange = { agreeToTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = TernivaGreen)
                )
                Text(
                    text = "Saya menyetujui Ketentuan Penggunaan, Kebijakan Privasi, dan Standar Mutu Komoditas Terniva.",
                    fontSize = 10.sp,
                    lineHeight = 13.sp,
                    color = Color(0xFF475569)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Register Submit -> Goes to OTP Verification
            Button(
                onClick = {
                    if (fullName.isBlank() || phoneOrEmail.isBlank() || password.isBlank()) {
                        errorMessage = "Mohon lengkapi seluruh formulir pendaftaran."
                    } else if (password.length < 6) {
                        errorMessage = "Kata sandi minimal 6 karakter."
                    } else if (!agreeToTerms) {
                        errorMessage = "Anda harus menyetujui ketentuan layanan."
                    } else {
                        viewModel.navigateTo(
                            CurrentScreen.OtpVerification(
                                phoneOrEmail = phoneOrEmail,
                                isRegister = true,
                                registeredName = fullName,
                                registeredRole = selectedRole
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_submit_register"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
            ) {
                Text("Daftar Sekarang & Verifikasi OTP", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    phoneOrEmail: String,
    isRegister: Boolean,
    registeredName: String? = null,
    registeredRole: UserRole? = null,
    viewModel: TernivaViewModel,
    modifier: Modifier = Modifier
) {
    var otpCode by remember { mutableStateOf("8812") }
    var countdown by remember { mutableStateOf(59) }
    var isVerifying by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verifikasi Keamanan OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(if (isRegister) CurrentScreen.Register else CurrentScreen.Login) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(TernivaGreen.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = TernivaGreen, modifier = Modifier.size(36.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Masukkan Kode OTP", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Kode 4-digit verifikasi telah dikirimkan secara aman ke:\n$phoneOrEmail",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // OTP Box Inputs
            OutlinedTextField(
                value = otpCode,
                onValueChange = { if (it.length <= 4) otpCode = it },
                label = { Text("Kode OTP 4 Digit") },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 8.sp,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(220.dp)
                    .testTag("input_otp_code"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TernivaGreen,
                    focusedLabelColor = TernivaGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = TernivaHarvestGold.copy(alpha = 0.12f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "🔒 Demo Kode Otomatis: 8812",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TernivaHarvestGold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    isVerifying = true
                    val name = registeredName ?: "Budi Santoso"
                    val role = registeredRole ?: UserRole.BUYER
                    viewModel.login(phoneOrEmail, name, role)
                    viewModel.navigateTo(CurrentScreen.MainNavigation)
                },
                enabled = otpCode.length >= 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_verify_otp"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TernivaGreen)
            ) {
                Text("Verifikasi & Masuk", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (countdown > 0) {
                Text(
                    text = "Kirim ulang kode dalam $countdown detik",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            } else {
                Text(
                    text = "Kirim Ulang Kode OTP via WhatsApp",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TernivaGreen,
                    modifier = Modifier.clickable {
                        countdown = 59
                    }
                )
            }
        }
    }
}
