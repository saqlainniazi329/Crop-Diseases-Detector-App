package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.credentials.CredentialManager
import com.example.R
import com.example.data.model.CropScanEntity
import com.example.model.CropDiseaseKnowledgeBase
import com.example.model.PlantBotanicalProfile
import com.example.ui.CropAppViewModel
import com.example.ui.CropUiState
import com.example.ui.auth.AuthManager
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary
import com.example.ui.theme.AgriHealthyGreen
import com.example.ui.theme.AgriRedDisease
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: CropUiState,
    viewModel: CropAppViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    var tempCameraUri by rememberSaveable { mutableStateOf<String?>(null) }
    var serverInput by remember { mutableStateOf(uiState.serverUrl) }
    var editableFarmerName by remember(uiState.farmerProfileName) { mutableStateOf(uiState.farmerProfileName) }

    val profilePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfilePicture(it) }
    }

    // Photo Gallery Picker Launcher (Google Play Policy Compliant: PickVisualMedia)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onImageSelected(it) }
    }

    // Camera Capture Launcher with [HARDWARE_BRIDGE: SECURE_CAMERA_STREAM]
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        val uri = tempCameraUri?.let { Uri.parse(it) } ?: viewModel.getPendingCameraUri()
        if (success && uri != null) {
            viewModel.onImageSelected(uri)
        } else {
            // [CATCH_ERROR: FALLBACK_TO_GALLERY_PICKER]
            viewModel.onCameraCaptureFailed()
            val fallbackNotice = if (uiState.appCurrentLang == "EN")
                "Camera capture interrupted. [CATCH_ERROR: FALLBACK_TO_GALLERY_PICKER] safely launched."
            else
                "کیمرہ ایرر! [CATCH_ERROR: FALLBACK_TO_GALLERY_PICKER] محفوظ گیلری پکر فعال کر دیا گیا ہے۔"
            Toast.makeText(context, fallbackNotice, Toast.LENGTH_LONG).show()
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    // Camera Permission Launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            try {
                val file = viewModel.createTempPictureFile()
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                tempCameraUri = uri.toString()
                viewModel.setPendingCameraUri(uri)
                cameraLauncher.launch(uri)
            } catch (e: Exception) {
                viewModel.onCameraCaptureFailed()
                val fallbackNotice = if (uiState.appCurrentLang == "EN")
                    "Camera initialization error! Falling back to Gallery."
                else
                    "کیمرہ شروع کرنے میں خرابی! گیلری پکر محفوظ طریقہ سے شروع ہو رہا ہے۔"
                Toast.makeText(context, fallbackNotice, Toast.LENGTH_LONG).show()
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        } else {
            val permDeniedMsg = if (uiState.appCurrentLang == "EN")
                "Camera permission required. Falling back to Gallery picker."
            else
                "پودے کے پتے کی تصویر کے لیے کیمرے کی اجازت ضروری ہے۔ گیلری سے منتخب کریں۔"
            Toast.makeText(context, permDeniedMsg, Toast.LENGTH_LONG).show()
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    fun launchCamera() {
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            try {
                val file = viewModel.createTempPictureFile()
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                tempCameraUri = uri.toString()
                viewModel.setPendingCameraUri(uri)
                cameraLauncher.launch(uri)
            } catch (e: Exception) {
                viewModel.onCameraCaptureFailed()
                val fallbackNotice = if (uiState.appCurrentLang == "EN")
                    "Camera hardware issue! Safely opening Gallery picker."
                else
                    "کیمرہ کھولنے میں خرابی! محفوظ گیلری پکر کھولا جا رہا ہے۔"
                Toast.makeText(context, fallbackNotice, Toast.LENGTH_LONG).show()
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("home_screen_root")
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar Header with Auth Status, Language Switcher & Settings
            HomeTopBar(
                currentUser = uiState.currentUser,
                isPhoneVerified = uiState.isPhoneVerified,
                verifiedPhone = uiState.verifiedPhoneNumber,
                selectedLanguage = uiState.selectedLanguage,
                onLanguageClick = { viewModel.toggleLanguageSelector(true) },
                onSignInClick = { viewModel.openAuthModal(0) },
                onProfileClick = { viewModel.openSettings() },
                onSettingsClick = {
                    serverInput = uiState.serverUrl
                    viewModel.openSettings()
                }
            )

            // Main Scrollable Dashboard Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // LEVEL 1: WELCOME BANNER ("Welcome Kisan / خوش آمدید کسان")
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    HeroWelcomeCard(
                        currentUser = uiState.currentUser,
                        isPhoneVerified = uiState.isPhoneVerified,
                        verifiedPhone = uiState.verifiedPhoneNumber,
                        appCurrentLang = uiState.appCurrentLang
                    )
                }

                // LEVEL 3: LIVE SENSORS & GOOGLE WEATHER RECKONING
                item {
                    LiveSensorsWeatherCard(
                        uiState = uiState,
                        onRefresh = { viewModel.refreshGpsAndWeather() },
                        onExpandWeather = { viewModel.openWeatherDetailsModal() }
                    )
                }

                // LEVEL 4: HIGH-PRECISION LEAF & PLANT DETECTION (CAMERA + GALLERY)
                item {
                    Text(
                        text = "Instant Crop & Tree Leaf Diagnosis",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Text(
                        text = "Take a close-up photo of the infected crop leaf to identify the disease and receive dual-language pesticide remedies.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline),
                        modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                    )

                    // Two prominent responsive action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 1. Camera Button
                        Button(
                            onClick = { launchCamera() },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .testTag("camera_capture_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AgriGreenPrimary,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "📷 Capture via Camera",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        // 2. Gallery Button
                        OutlinedButton(
                            onClick = {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .testTag("gallery_upload_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = AgriGreenPrimary
                            ),
                            border = BorderStroke(1.5.dp, AgriGreenPrimary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Gallery",
                                modifier = Modifier.size(22.dp),
                                tint = AgriGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🖼️ Upload from Gallery",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                // Cloud-Synced History Section (when user has Firestore records)
                if (uiState.currentUser != null && uiState.cloudScans.isNotEmpty()) {
                    item {
                        CloudScansSection(scans = uiState.cloudScans)
                    }
                } else if (uiState.currentUser == null) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.navigateToSignIn() },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(AgriGreenPrimary.copy(alpha = 0.12f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudDone,
                                        contentDescription = null,
                                        tint = AgriGreenPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Cloud Sync Available",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Sign in with Google to backup your scan history to Firebase Firestore.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline)
                                    )
                                }
                            }
                        }
                    }
                }

                // Supported Disease Classes Guide
                item {
                    SupportedDiseasesSection()
                }

                // Botanical Plant & Leaf Anatomy Guide (ہر پودے اور پتے کی ساخت، ساخت اور بیماری کا مقام)
                item {
                    BotanicalPlantLeafGuideSection()
                }

                // AI Model Pipeline Architecture Card
                item {
                    ModelArchitectureInfoCard()
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // MANDATORY VISUAL CONSTRAINT:
            // "On the Home Screen/Front Page, at the very bottom area inside a styled Footer or BottomNavigationBar widget, display clear and elegant text: "Developer: Saqlain Niazi"."
            StyledDeveloperFooter()
        }

        // Analyzing Loading Overlay
        AnimatedVisibility(
            visible = uiState.isAnalyzing,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 12.dp,
                modifier = Modifier
                    .padding(32.dp)
                    .border(1.dp, AgriGreenPrimary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = AgriGreenPrimary,
                        strokeWidth = 3.5.dp,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Analyzing Crop Sample",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = uiState.analysisProgressText,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }

    // LEVEL 1: LANGUAGE SELECTION DIALOG [SYSTEM_ACTION: DISPLAY_LANGUAGE_SELECTION]
    if (uiState.isLanguageSelectorOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleLanguageSelector(false) },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = AgriGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select Language / زبان کا انتخاب", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "براہ کرم اپنی پسندیدہ زبان منتخب کریں:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedButton(
                        onClick = { viewModel.selectLanguage("اردو") },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = if (uiState.selectedLanguage == "اردو") ButtonDefaults.outlinedButtonColors(containerColor = AgriGreenPrimary.copy(alpha = 0.15f)) else ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text("اردو (Urdu) - تجویز کردہ برائے کسان", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { viewModel.selectLanguage("English") },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = if (uiState.selectedLanguage == "English") ButtonDefaults.outlinedButtonColors(containerColor = AgriGreenPrimary.copy(alpha = 0.15f)) else ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text("English (Dual Botanical Mode)", fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { viewModel.toggleLanguageSelector(false) }) {
                    Text("بند کریں / Close")
                }
            }
        )
    }

    // LEVEL 2: ADVANCED AUTHENTICATION MODAL [SYSTEM_ACTION: SHOW_AUTHENTICATION_SCREEN]
    if (uiState.showAuthModal) {
        AlertDialog(
            onDismissRequest = { viewModel.closeAuthModal() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = AgriGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("کسان تصدیق و رجسٹریشن (Onboarding)", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
            },
            text = {
                Column {
                    TabRow(
                        selectedTabIndex = uiState.selectedAuthTab,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = AgriGreenPrimary
                    ) {
                        Tab(
                            selected = uiState.selectedAuthTab == 0,
                            onClick = { viewModel.setAuthTab(0) },
                            text = { Text("📱 فون نمبر / OTP", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                        )
                        Tab(
                            selected = uiState.selectedAuthTab == 1,
                            onClick = { viewModel.setAuthTab(1) },
                            text = { Text("🌐 گوگل اکاؤنٹ", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    if (uiState.selectedAuthTab == 0) {
                        // Path A: Pakistani Mobile Phone Number Verification via SMS OTP
                        Text(
                            text = "پاکستانی کسان بھائی اپنا موبائل نمبر درج کریں تاکہ 4 یا 6 ہندسوں کا OTP ایس ایم ایس بھیجا جا سکے۔",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = uiState.phoneNumberInput,
                            onValueChange = { viewModel.updatePhoneNumber(it) },
                            label = { Text("موبائل نمبر (03001234567 یا 92300...)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = AgriGreenPrimary)
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        if (!uiState.isOtpSent) {
                            Button(
                                onClick = { viewModel.sendOtpSms() },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                                shape = RoundedCornerShape(12.dp),
                                enabled = !uiState.isOtpSending
                            ) {
                                if (uiState.isOtpSending) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                } else {
                                    Text("📩 [SEND_OTP_SMS] کوڈ بھیجیں", fontWeight = FontWeight.Bold)
                                }
                            }
                        } else {
                            Surface(
                                color = AgriHealthyGreen.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "✅ تصدیقی کوڈ ایس ایم ایس کے ذریعے بھیج دیا گیا ہے!",
                                    style = MaterialTheme.typography.labelSmall.copy(color = AgriHealthyGreen, fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = uiState.otpCodeInput,
                                onValueChange = { viewModel.updateOtpCode(it) },
                                label = { Text("4 یا 6 ہندسوں کا OTP کوڈ درج کریں") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { viewModel.verifyOtpCode() },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                                shape = RoundedCornerShape(12.dp),
                                enabled = !uiState.isOtpSending
                            ) {
                                if (uiState.isOtpSending) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                } else {
                                    Text("🔐 تصدیق کریں اور لاگ ان ہوں", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (uiState.otpError != null) {
                            Text(
                                text = uiState.otpError!!,
                                color = AgriRedDisease,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    } else {
                        // Path B: Google Sign In
                        Text(
                            text = "اپنے آفیشل گوگل (Gmail) اکاؤنٹ کے ذریعے فوری لاگ ان کریں تاکہ بیماریوں کا کلاؤڈ ریکارڈ محفوظ رہے۔",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.closeAuthModal()
                                AuthManager.onGoogleSignInClicked(
                                    context = context,
                                    credentialManager = credentialManager,
                                    onAuthSuccess = {
                                        Toast.makeText(context, "گوگل اکاؤنٹ سے کامیابی کے ساتھ لاگ ان ہو گئے!", Toast.LENGTH_SHORT).show()
                                    },
                                    onAuthError = { error ->
                                        Toast.makeText(context, "Sign-in error: $error", Toast.LENGTH_SHORT).show()
                                    },
                                    scope = coroutineScope
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Google Gmail سے لاگ ان کریں", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { viewModel.closeAuthModal() }) {
                    Text("منسوخ کریں / Cancel")
                }
            }
        )
    }

    // LEVEL 2: PERSISTENT ULTRA SETTINGS MENU [SYSTEM_ACTION: LAUNCH_ULTRA_SETTINGS]
    if (uiState.showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.closeSettings() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = AgriGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("سیٹنگز و کسان پروفائل (Ultra Settings)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("[SYSTEM_ACTION: LAUNCH_ULTRA_SETTINGS]", fontSize = 9.sp, color = AgriGreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Farmer Profile & Live Photo Editor
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(2.dp, AgriGreenPrimary, CircleShape)
                                    .clickable {
                                        profilePhotoLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (uiState.farmerProfileImageUri != null) {
                                    AsyncImage(
                                        model = uiState.farmerProfileImageUri,
                                        contentDescription = "Profile Photo",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_pakistani_farmer),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = uiState.farmerProfileName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1B5E20)
                                )
                                TextButton(
                                    onClick = {
                                        profilePhotoLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("📸 تصویر تبدیل کریں (Update Photo)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                                }
                            }
                        }
                    }

                    // Editable Profile Name
                    OutlinedTextField(
                        value = editableFarmerName,
                        onValueChange = { editableFarmerName = it },
                        label = { Text("کسان کا نام (Profile Name)") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = AgriGreenPrimary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // ENTERTAINMENT SHUNT: LUDO KISAN CORE [SYSTEM_ACTION: BOOT_LUDO_KISAN_CORE]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FBE7)),
                        border = BorderStroke(1.5.dp, Color(0xFF33691E))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.SportsEsports, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("انٹرٹینمنٹ شنٹ: لڈو کسان پرو", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = Color(0xFF1B5E20))
                                    Text("[SYSTEM_ACTION: BOOT_LUDO_KISAN_CORE]", fontSize = 8.sp, color = Color(0xFF33691E), fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "3 فعال موڈز: کمپیوٹر AI، 2 کھلاڑی اور 4 کھلاڑی جٹ-جٹ الائنس اتحاد!",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color(0xFF33691E))
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    viewModel.closeSettings()
                                    viewModel.openLudoKisan()
                                },
                                modifier = Modifier.fillMaxWidth().height(42.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Text("🎲 لڈو گیم شروع کریں (Play Ludo)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }

                    // App Telemetry, Diagnostics & Legal Compliance
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("📱 ایپ ٹیلی میٹری و قانونی تصدیق:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            Text("• CropDoc AI ورژن: 2026.4.1 (Production Release)", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                            Text("• پروجیکٹ: IUB BS IT Final Year Project 2026 by Saqlain Niazi", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                            Text("• لیگل کمپلائنس: DPP & PARC پاکستان منظور شدہ ریسرچ انجن", fontSize = 10.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                            Text("• لائیو سینسرز: GPS فعال، 60 منٹ ویدر ڈیمن سٹرنکرونائزڈ", fontSize = 10.sp, color = Color(0xFF0277BD))
                        }
                    }

                    // Language Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ایپ کی زبان (Language):", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium))
                        OutlinedButton(
                            onClick = {
                                val next = if (uiState.selectedLanguage == "اردو") "English" else "اردو"
                                viewModel.selectLanguage(next)
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(uiState.selectedLanguage, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Reset credentials / Logout
                    Button(
                        onClick = { viewModel.resetCredentials() },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgriRedDisease.copy(alpha = 0.9f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ری سیٹ کریڈنشلز و لاگ آؤٹ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    // Backend Endpoint Config
                    Text(
                        text = "Python Flask Server URL:",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.outline)
                    )
                    OutlinedTextField(
                        value = serverInput,
                        onValueChange = { serverInput = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateFarmerProfileName(editableFarmerName)
                        viewModel.updateServerUrl(serverInput)
                        viewModel.closeSettings()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text("محفوظ کریں / Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeSettings() }) {
                    Text("بند کریں")
                }
            }
        )
    }

    // Entertainment Shunt Fullscreen Dialog
    if (uiState.showLudoKisanDialog) {
        LudoKisanGameDialog(
            appCurrentLang = uiState.appCurrentLang,
            onDismiss = { viewModel.closeLudoKisan() }
        )
    }

    // Detailed Weather Modal
    if (uiState.showWeatherDetailsModal) {
        WeatherDetailsModal(
            uiState = uiState,
            onDismiss = { viewModel.closeWeatherDetailsModal() },
            onRefresh = { viewModel.refreshGpsAndWeather() }
        )
    }
}

@Composable
private fun HomeTopBar(
    currentUser: FirebaseUser?,
    isPhoneVerified: Boolean,
    verifiedPhone: String?,
    selectedLanguage: String,
    onLanguageClick: () -> Unit,
    onSignInClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(AgriGreenPrimary.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "CropDoc AI",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AgriGreenPrimary
                        )
                    )
                    Text(
                        text = "IUB BS IT • Final Year Project 2026",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Medium,
                            fontSize = 9.sp
                        )
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Language Switcher Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { onLanguageClick() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedLanguage,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Auth Status indicator or Login Button
                if (currentUser != null || isPhoneVerified) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = AgriHealthyGreen.copy(alpha = 0.15f),
                        modifier = Modifier.clickable { onProfileClick() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active",
                                tint = AgriHealthyGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (currentUser != null) "Gmail Sync" else "OTP Verified",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AgriHealthyGreen,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = onSignInClick,
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            text = "لاگ ان / Auth",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.size(36.dp).testTag("backend_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// LEVEL 1: WELCOME BANNER ("Welcome Kisan / خوش آمدید کسان")
@Composable
private fun HeroWelcomeCard(
    currentUser: FirebaseUser?,
    isPhoneVerified: Boolean,
    verifiedPhone: String?,
    appCurrentLang: String = "UR"
) {
    val isEn = appCurrentLang == "EN"
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0F381E),
                            Color(0xFF1B5E20),
                            Color(0xFF2E7D32)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pakistani Farmer Illustration & Badge
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp,
                    border = BorderStroke(2.dp, Color(0xFFFFD54F)),
                    modifier = Modifier.size(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pakistani_farmer),
                        contentDescription = "Pakistani Farmer",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = Color(0xFFFFD54F).copy(alpha = 0.25f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (isEn) "Govt. of Pakistan DPP Certified 2026" else "حکومت پاکستان DPP تصدیق شدہ 2026",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD54F),
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // STRICT WELCOMING ANCHOR: "Welcome Kisan" IF EN | "خوش آمدید کسان" IF UR
                    Text(
                        text = if (isEn) "Welcome Kisan" else "خوش آمدید کسان",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 17.sp
                        )
                    )

                    Text(
                        text = if (isEn)
                            "Multi-crop botanical diagnosis for Safeda, Guava, Pomegranate, Dates, Mango, Cotton, Wheat, Rice, and Maize."
                        else
                            "سفیدہ، انار، کھجور، مکئی، آم، کنو، امرود، کپاس، گندم، دھان اور کماد کے پودوں اور پتوں کی مستند نباتاتی جانچ۔",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE8F5E9),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

// LEVEL 3: LIVE SENSORS & GOOGLE WEATHER RECKONING
@Composable
private fun LiveSensorsWeatherCard(
    uiState: CropUiState,
    onRefresh: () -> Unit,
    onExpandWeather: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandWeather() }
            .testTag("live_weather_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, AgriGreenPrimary.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: GPS Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "GPS Location",
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "موجودہ لوکیشن (GPS Sync)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary
                        )
                    )
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AgriGreenPrimary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "LIVE SENSORS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary,
                            fontSize = 9.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = uiState.gpsLocation,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Weather Reckoning Parameters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    tint = Color(0xFFE65100),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "موسم / Weather: ${uiState.temperature} (${uiState.weatherCondition})",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    )
                    Text(
                        text = "فضا میں نمی: ${uiState.humidity} • ہوا کی رفتار: 14 کلومیٹر/گھنٹہ",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF2E7D32))
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Precision Irrigation Advice
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE1F5FE), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    tint = Color(0xFF0288D1),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "پانی کا استعمال / Irrigation Guide",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF01579B)
                        )
                    )
                    Text(
                        text = uiState.precisionIrrigationAdvice,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0277BD),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Extreme Weather & News Notifications
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF3E0), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color(0xFFE65100),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "اہم الرٹ اور نئی زرعی معلومات (News & Alerts)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBF360C)
                        )
                    )
                    Text(
                        text = uiState.extremeWeatherWarning,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFD84315),
                            fontWeight = FontWeight.Medium
                        )
                    )
                    uiState.agriculturalAlerts.forEach { alert ->
                        Text(
                            text = "• $alert",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF5D4037)),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                color = Color(0xFFE1F5FE),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandWeather() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color(0xFF0288D1),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "📊 تفصیلی موسمیات، بارش کا امکان (68%) اور طلوع/غروب کے لیے ٹیپ کریں (Click to Expand)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0288D1),
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CloudScansSection(scans: List<CropScanEntity>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Firestore Cloud Records (${scans.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Surface(
                color = AgriGreenPrimary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Live Firestore",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenPrimary
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(scans) { scan ->
                val isHealthy = scan.diseaseName.equals("Healthy", ignoreCase = true)
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, if (isHealthy) AgriHealthyGreen.copy(alpha = 0.4f) else AgriRedDisease.copy(alpha = 0.3f)),
                    modifier = Modifier.width(170.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(if (isHealthy) AgriHealthyGreen else AgriRedDisease, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${(scan.confidence * 100).toInt()}% match",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isHealthy) AgriHealthyGreen else AgriRedDisease
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = scan.diseaseName,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1
                        )
                        Text(
                            text = scan.diseaseNameUrdu,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.outline,
                                fontSize = 11.sp
                            ),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SupportedDiseasesSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "باغات اور فصلیں (Trees & Crops)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Surface(
                color = AgriGreenPrimary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "حکومت پاکستان DPP تصدیق شدہ",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenPrimary
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
        Text(
            text = "پاکستان میں تمام اقسام کے پھل دار درختوں اور فصلوں کی بیماریوں کی حقیقی کمپیوٹر ویژن تشخیص:",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline),
            modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val speciesList = listOf(
                Triple("Mango (آم)", "اینتھراکنوز / کالا دھبہ", "باغ کا درخت"),
                Triple("Citrus (کنو / مالٹا)", "سٹرس کینکر (کوڑھ)", "باغ کا درخت"),
                Triple("Guava (امرود)", "امرود کا سوکڑا (Wilt)", "باغ کا درخت"),
                Triple("Cotton (کپاس)", "پتہ مروڑ وائرس (CLCuV)", "کھیت کی فصل"),
                Triple("Wheat (گندم)", "پیلگی کنگی (Yellow Rust)", "کھیت کی فصل"),
                Triple("Rice (دھان)", "رائس بلاسٹ و بلائٹ", "کھیت کی فصل"),
                Triple("Sugarcane (گنا)", "سرخ گلن (Red Rot)", "کھیت کی فصل"),
                Triple("Apple (سیب)", "سیب کا خارش (Apple Scab)", "پہاڑی درخت")
            )

            items(speciesList) { (species, disease, category) ->
                val isTreeType = category.contains("درخت")
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, if (isTreeType) Color(0xFF2E7D32).copy(alpha = 0.3f) else Color(0xFF1565C0).copy(alpha = 0.3f)),
                    modifier = Modifier.width(165.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isTreeType) Color(0xFFE8F5E9) else Color(0xFFE3F2FD)
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTreeType) Color(0xFF2E7D32) else Color(0xFF1565C0),
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = species,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = disease,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp
                            ),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModelArchitectureInfoCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = AgriGreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "System Architecture Overview",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "• Input Size: 224 × 224 × 3 RGB Tensor\n• CNN Layers: 3× Conv2D (32, 64, 64) + MaxPool + Dense\n• Backend: Python Flask API (0.0.0.0:5000/predict)\n• Database: Firebase Firestore Cloud Synchronization\n• Multilingual: Dual English & Localized Urdu remedies",
                style = MaterialTheme.typography.bodySmall.copy(
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

/**
 * Botanical Plant & Leaf Anatomy Guide
 * (ہر پودے اور پتے کی ساخت، ساخت اور بیماری کا مقام)
 */
@Composable
private fun BotanicalPlantLeafGuideSection() {
    var selectedSpecimenId by remember { mutableStateOf<String?>(null) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "پودوں اور پتوں کی تصویری ساخت",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Surface(
                color = Color(0xFF2E7D32).copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "نباتاتی گائیڈ",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
        Text(
            text = "کسان بھائی کسی غلط فہمی سے بچنے کے لیے یہاں ہر پودے اور پتے کی قدرتی بناوٹ اور بیماری کا مقام دیکھ سکتے ہیں:",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline),
            modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(CropDiseaseKnowledgeBase.BOTANICAL_CATALOG) { item ->
                val isSelected = selectedSpecimenId == item.id
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        1.5.dp,
                        if (isSelected) AgriGreenPrimary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .width(220.dp)
                        .clickable {
                            selectedSpecimenId = if (isSelected) null else item.id
                        }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (item.isTree) Color(0xFFE8F5E9) else Color(0xFFE3F2FD)
                            ) {
                                Text(
                                    text = if (item.isTree) "باغ کا درخت" else "کھیت کی فصل",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.isTree) Color(0xFF2E7D32) else Color(0xFF1565C0),
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = null,
                                tint = if (item.isTree) Color(0xFF2E7D32) else Color(0xFF1565C0),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.nameUrdu,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = item.nameEnglish,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline, fontSize = 11.sp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "پتے کی شکل: ${item.leafTypeUrdu}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "بیماری کہاں لگتی ہے: ${item.whereDiseaseAttacksUrdu}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AgriRedDisease
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * MANDATORY VISUAL CONSTRAINT:
 * On the Home Screen/Front Page, at the very bottom area inside a styled Footer
 * or BottomNavigationBar widget, display clear and elegant text: "Developer: Saqlain Niazi".
 */
@Composable
private fun StyledDeveloperFooter() {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("developer_footer_container")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Distinctive Pill Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = AgriGreenPrimary.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, AgriGreenPrimary.copy(alpha = 0.3f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Developer: Saqlain Niazi",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AgriGreenPrimary,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.testTag("developer_attribution_text")
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "BS Information Technology • Islamia University of Bahawalpur (IUB) 2026",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.outline
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
