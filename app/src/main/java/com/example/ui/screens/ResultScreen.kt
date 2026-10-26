package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.CropDiseaseInfo
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriHealthyGreen
import com.example.ui.theme.AgriRedDisease

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    resultInfo: CropDiseaseInfo,
    imageUri: Uri?,
    sourceText: String,
    isSpeaking: Boolean = false,
    appCurrentLang: String = "UR",
    onReplayVoice: () -> Unit = {},
    onStopVoice: () -> Unit = {},
    onBackToHome: () -> Unit
) {
    BackHandler {
        onStopVoice()
        onBackToHome()
    }

    val isEn = appCurrentLang == "EN"
    var selectedTab by remember { mutableIntStateOf(0) }
    val isHealthy = resultInfo.diseaseName.equals("Healthy", ignoreCase = true)
    val isRejected = resultInfo.isUnknownOrRejected
    val diseaseColor = when {
        isRejected -> Color(0xFFE65100)
        isHealthy -> AgriHealthyGreen
        else -> AgriRedDisease
    }

    // Subtle breathing animation for speaking pulse
    val infiniteTransition = rememberInfiniteTransition(label = "voice_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("result_screen_root")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isRejected) {
                                if (isEn) "Quality Verification & Leaf Check" else "تصدیق و معیار جانچ"
                            } else {
                                if (isEn) "Botanical Pathology & Approved Remedies" else "نباتاتی ساخت و تصدیق شدہ علاج"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "IUB BS IT Final Year Project 2026",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onStopVoice()
                        onBackToHome()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Urdu Voice Assistant Live Status Card
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    VoiceAssistantBanner(
                        isSpeaking = isSpeaking,
                        pulseScale = pulseScale,
                        isRejected = isRejected,
                        onReplayVoice = onReplayVoice,
                        onStopVoice = onStopVoice
                    )
                }

                // 2. Header Image & Leaf Scan Status
                item {
                    CropImagePreviewCard(
                        imageUri = imageUri,
                        confidence = resultInfo.confidence,
                        sourceText = sourceText,
                        isHealthy = isHealthy,
                        isTree = resultInfo.isTree,
                        isRejected = isRejected
                    )
                }

                // 3. IF QUALITY GUARD REJECTED NON-LEAF (ANTI-MISGUIDANCE)
                if (isRejected) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                            border = BorderStroke(1.5.dp, Color(0xFFFF9800))
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFE65100),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "کسان کی حفاظت اور غلط رہنمائی سے بچاؤ",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE65100)
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = resultInfo.rejectionReasonUrdu,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color(0xFF3E2723),
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "رہنمائی: تصویر لیتے وقت پودے یا درخت کے پتے کو کیمرے کے قریب لائیں تاکہ رگیں اور ساخت صاف نظر آئیں۔",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF5D4037))
                                )
                            }
                        }
                    }
                } else {
                    // 4. Tree or Field Crop Species Identity
                    item {
                        TreeCropIdentityCard(
                            cropOrTreeName = resultInfo.cropOrTreeName,
                            cropOrTreeNameUrdu = resultInfo.cropOrTreeNameUrdu,
                            isTree = resultInfo.isTree,
                            severity = resultInfo.severity,
                            severityUrdu = resultInfo.severityUrdu
                        )
                    }

                    // 5. BOTANICAL LEAF MORPHOLOGY & DISEASE LOCATION CARD
                    item {
                        BotanicalMorphologyCard(resultInfo = resultInfo)
                    }

                    // 6. Primary Predicted Disease Header (Dynamically rendered in bold red)
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("disease_card"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isHealthy) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                            ),
                            border = BorderStroke(
                                1.5.dp,
                                diseaseColor.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = diseaseColor.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = if (isHealthy) {
                                            if (isEn) "Healthy Crop / Tree" else "صحت مند درخت / فصل"
                                        } else {
                                            if (isEn) "PATHOLOGY DIAGNOSIS" else "تشخیص شدہ بیماری"
                                        },
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            letterSpacing = 1.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = diseaseColor
                                        ),
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // DYNAMICALLY RENDERED IN BOLD RED ACCORDING TO APP_CURRENT_LANG
                                val primaryTitle = if (isHealthy) {
                                    if (isEn) "Healthy" else "صحت مند"
                                } else {
                                    if (isEn) resultInfo.diseaseName else resultInfo.diseaseNameUrdu
                                }

                                Text(
                                    text = primaryTitle,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = diseaseColor,
                                        fontSize = 26.sp
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.testTag("predicted_disease_text")
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = diseaseColor.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = if (isEn) "Confidence: ${(resultInfo.confidence * 100).toInt()}%" else "اعتماد: ${(resultInfo.confidence * 100).toInt()}%",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = diseaseColor
                                            ),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surface
                                    ) {
                                        Text(
                                            text = "شدت: ${resultInfo.severityUrdu}",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            ),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 7. Pakistani Department of Agriculture Advisory: Monitoring & Irrigation Status Cards
                    item {
                        PakistaniAgronomyGuideSection(
                            monitoringDaysUrdu = resultInfo.monitoringPeriodUrdu,
                            monitoringDaysEnglish = resultInfo.monitoringPeriod,
                            irrigationGuideUrdu = resultInfo.irrigationGuideUrdu,
                            irrigationGuideEnglish = resultInfo.irrigationGuideEnglish
                        )
                    }

                    // 8. Action Tabs: DPP Approved Pesticides, Organic Remedies, Symptoms
                    item {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = AgriGreenPrimary
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = { Text("منظور شدہ ادویات\n(DPP Approved)", fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center) },
                                icon = { Icon(Icons.Default.LocalPharmacy, contentDescription = null, modifier = Modifier.size(20.dp)) }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = { Text("دیسی تدابیر\n(Organic)", fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center) },
                                icon = { Icon(Icons.Default.Eco, contentDescription = null, modifier = Modifier.size(20.dp)) }
                            )
                            Tab(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                text = { Text("علامات\n(Symptoms)", fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center) },
                                icon = { Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(20.dp)) }
                            )
                        }
                    }

                    // 9. Tab Content View
                    item {
                        when (selectedTab) {
                            0 -> PakistanApprovedPesticidesSection(
                                pesticidesUrdu = resultInfo.approvedPesticidesUrdu,
                                pesticidesEnglish = resultInfo.approvedPesticidesEnglish
                            )
                            1 -> RemediesSection(
                                titleEnglish = "Organic & Cultural Management (قدرتی اور دیسی تدابیر)",
                                titleUrdu = "حفاظتی اقدامات اور قدرتی/دیسی تدابیر",
                                englishItems = resultInfo.organicRemediesEnglish,
                                urduItems = resultInfo.organicRemediesUrdu,
                                icon = Icons.Default.Eco,
                                accentColor = AgriGreenPrimary
                            )
                            2 -> RemediesSection(
                                titleEnglish = "Disease Symptoms (بیماری کی ظاہری علامات)",
                                titleUrdu = "بیماری کی اہم ظاہری علامات",
                                englishItems = resultInfo.symptomsEnglish,
                                urduItems = resultInfo.symptomsUrdu,
                                icon = Icons.Default.Visibility,
                                accentColor = Color(0xFFEF6C00)
                            )
                        }
                    }

                    // 10. Long-Term Prevention Strategy Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = AgriGreenPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "طویل مدتی حفاظتی منصوبہ (Long-Term Prevention)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "اردو: ${resultInfo.preventiveMeasuresUrdu}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "English: ${resultInfo.preventiveMeasuresEnglish}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        lineHeight = 18.sp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Bottom Floating Action Button to scan another crop
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        onStopVoice()
                        onBackToHome()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(52.dp)
                        .testTag("scan_another_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRejected) "دوبارہ صاف تصویر کھینچیں (Re-scan Leaf)" else "نیا پودا یا درخت چیک کریں (Scan Another)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

/**
 * Botanical Leaf Morphology & Plant Architecture Card
 */
@Composable
private fun BotanicalMorphologyCard(resultInfo: CropDiseaseInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        border = BorderStroke(1.dp, Color(0xFFAED581))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = AgriGreenPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "نباتاتی پتے کی ساخت اور بیماری کا مقام",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenPrimary
                    )
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = Color(0xFFC5E1A5)
            )

            // Leaf Morphology
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF33691E),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "پتے کی ساخت و خدوخال (Leaf Morphology):",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF33691E)
                        )
                    )
                    Text(
                        text = resultInfo.leafShapeUrdu,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Disease Location on Plant
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = AgriRedDisease,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "بیماری پودے کے کس حصے پر لگتی ہے (Disease Location):",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AgriRedDisease
                        )
                    )
                    Text(
                        text = resultInfo.diseaseLocationUrdu,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Plant Architecture
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = Color(0xFF1B5E20),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "پودے اور تنے کا سٹرکچر (Plant Structure):",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    )
                    Text(
                        text = resultInfo.plantStructureUrdu,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // How Farmer Identifies This Leaf
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFDCEDC8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "پتے کی شناخت کا طریقہ:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary
                        )
                    )
                    Text(
                        text = resultInfo.identificationGuideUrdu,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
    }
}

/**
 * Urdu Voice Assistant Banner
 */
@Composable
private fun VoiceAssistantBanner(
    isSpeaking: Boolean,
    pulseScale: Float,
    isRejected: Boolean = false,
    onReplayVoice: () -> Unit,
    onStopVoice: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isRejected -> Color(0xFFE65100)
                isSpeaking -> Color(0xFF1B5E20)
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            }
        ),
        border = BorderStroke(
            1.dp,
            if (isRejected) Color(0xFFFFB74D) else if (isSpeaking) AgriHealthyGreen else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .scale(if (isSpeaking) pulseScale else 1f)
                        .background(
                            if (isSpeaking || isRejected) Color.White.copy(alpha = 0.25f) else AgriGreenPrimary.copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        tint = if (isSpeaking || isRejected) Color.White else AgriGreenPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (isRejected) "حفاظتی آڈیو پیغام" else if (isSpeaking) "اردو وائس اسسٹنٹ رہنمائی بول رہا ہے..." else "اردو آڈیو رہنمائی",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSpeaking || isRejected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = if (isRejected) "غلط رہنمائی سے بچاؤ کا وضاحتی پیغام سنیں" else if (isSpeaking) "کسان کے نام سے خودکار آواز جاری ہے" else "مکمل علاج اور پانی کی رہنمائی سنیں",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isSpeaking || isRejected) Color(0xFFFFF3E0) else MaterialTheme.colorScheme.outline,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (isSpeaking) {
                    IconButton(
                        onClick = onStopVoice,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeOff,
                            contentDescription = "Stop Voice",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Button(
                    onClick = onReplayVoice,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSpeaking || isRejected) Color.White else AgriGreenPrimary,
                        contentColor = if (isRejected) Color(0xFFE65100) else if (isSpeaking) Color(0xFF1B5E20) else Color.White
                    ),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = if (isSpeaking) "دوبارہ سنیں" else "آواز سنیں",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

/**
 * Tree & Crop Identity Card
 */
@Composable
private fun TreeCropIdentityCard(
    cropOrTreeName: String,
    cropOrTreeNameUrdu: String,
    isTree: Boolean,
    severity: String,
    severityUrdu: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            if (isTree) Color(0xFF2E7D32).copy(alpha = 0.15f) else Color(0xFF1565C0).copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = if (isTree) Color(0xFF2E7D32) else Color(0xFF1565C0),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isTree) Color(0xFFE8F5E9) else Color(0xFFE3F2FD)
                    ) {
                        Text(
                            text = if (isTree) "باغ کا درخت (Fruit Orchard Tree)" else "کھیت کی فصل (Field Crop)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isTree) Color(0xFF2E7D32) else Color(0xFF1565C0),
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = cropOrTreeNameUrdu,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = cropOrTreeName,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AgriRedDisease.copy(alpha = 0.1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "شدت",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AgriRedDisease
                        )
                    )
                    Text(
                        text = severityUrdu,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AgriRedDisease
                        )
                    )
                }
            }
        }
    }
}

/**
 * Pakistani Agronomy Guide (Monitoring Days & Irrigation Status)
 */
@Composable
private fun PakistaniAgronomyGuideSection(
    monitoringDaysUrdu: String,
    monitoringDaysEnglish: String,
    irrigationGuideUrdu: String,
    irrigationGuideEnglish: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Monitoring Days Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
            border = BorderStroke(1.dp, Color(0xFFFFE082))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFFFA000).copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFFE65100),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "کتنے دن تک نگرانی کرنی چاہیے (Field Monitoring Period)",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                    )
                    Text(
                        text = monitoringDaysUrdu,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242)
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = monitoringDaysEnglish,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF757575)
                        )
                    )
                }
            }
        }

        // Irrigation Situation Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
            border = BorderStroke(1.dp, Color(0xFF81D4FA))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF0288D1).copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color(0xFF01579B),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "پانی اور آبپاشی کی صورتحال (Irrigation & Moisture Guide)",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF01579B)
                        )
                    )
                    Text(
                        text = irrigationGuideUrdu,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121),
                            lineHeight = 20.sp
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = irrigationGuideEnglish,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF616161)
                        )
                    )
                }
            }
        }
    }
}

/**
 * Government of Pakistan Department of Plant Protection Approved Agrochemicals
 */
@Composable
private fun PakistanApprovedPesticidesSection(
    pesticidesUrdu: List<String>,
    pesticidesEnglish: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, AgriGreenPrimary.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(AgriGreenPrimary.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalPharmacy,
                        contentDescription = null,
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "حکومتِ پاکستان منظور شدہ زرعی ادویات",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary
                        )
                    )
                    Text(
                        text = "DPP & PARC Registered Pesticides & Precise Dosages",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            pesticidesUrdu.forEachIndexed { index, urduAdvice ->
                val englishAdvice = pesticidesEnglish.getOrNull(index) ?: ""

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF9FBE7),
                    border = BorderStroke(1.dp, Color(0xFFDCE775)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = AgriGreenPrimary,
                                modifier = Modifier.size(22.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${index + 1}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = urduAdvice,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32),
                                    lineHeight = 22.sp
                                )
                            )
                        }

                        if (englishAdvice.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = englishAdvice,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF558B2F),
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.padding(start = 30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CropImagePreviewCard(
    imageUri: Uri?,
    confidence: Float,
    sourceText: String,
    isHealthy: Boolean,
    isTree: Boolean,
    isRejected: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Analyzed Crop Leaf",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AgriGreenPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            // Top Status Source Pill
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.72f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = sourceText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            // Bottom Confidence Status Pill
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when {
                    isRejected -> Color(0xFFE65100).copy(alpha = 0.95f)
                    isHealthy -> Color(0xFF2E7D32).copy(alpha = 0.9f)
                    else -> Color(0xFFC62828).copy(alpha = 0.9f)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = when {
                            isRejected -> Icons.Default.Warning
                            isHealthy -> Icons.Default.CheckCircle
                            else -> Icons.Default.Warning
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when {
                            isRejected -> "Unrecognized Sample"
                            isHealthy -> "Healthy Condition"
                            else -> "Infection Detected"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun RemediesSection(
    titleEnglish: String,
    titleUrdu: String,
    englishItems: List<String>,
    urduItems: List<String>,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(accentColor.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = titleUrdu,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    )
                    Text(
                        text = titleEnglish,
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.outline)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            urduItems.forEachIndexed { index, urduText ->
                val englishText = englishItems.getOrNull(index) ?: ""

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = accentColor.copy(alpha = 0.15f),
                            modifier = Modifier.size(22.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}",
                                    color = accentColor,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = urduText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 22.sp
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (englishText.isNotBlank()) {
                        Text(
                            text = englishText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.outline,
                                lineHeight = 18.sp
                            ),
                            modifier = Modifier.padding(start = 32.dp, top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
