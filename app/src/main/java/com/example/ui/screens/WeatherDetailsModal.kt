package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CropUiState
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriHealthyGreen
import com.example.ui.theme.AgriRedDisease

data class HourlyForecast(
    val timeLabel: String,
    val temp: String,
    val rainProb: Int,
    val conditionUrdu: String,
    val icon: ImageVector
)

@Composable
fun WeatherDetailsModal(
    uiState: CropUiState,
    onDismiss: () -> Unit,
    onRefresh: () -> Unit
) {
    val hourlyList = listOf(
        HourlyForecast("01:00 PM", "34°C", 35, "ابر آلود", Icons.Default.WbSunny),
        HourlyForecast("02:00 PM", "35°C", 65, "تیز گرج چمک", Icons.Default.Warning),
        HourlyForecast("03:00 PM", "31°C", 82, "شدید بارش", Icons.Default.WaterDrop),
        HourlyForecast("04:00 PM", "29°C", 70, "بارش کا سلسلہ", Icons.Default.WaterDrop),
        HourlyForecast("05:00 PM", "28°C", 45, "ہلکی بوندا باندی", Icons.Default.WaterDrop),
        HourlyForecast("06:00 PM", "27°C", 20, "جزوی صاف", Icons.Default.WbSunny)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("detailed_weather_modal"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFF0288D1).copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFF0288D1), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "تفصیلی زرعی موسمیات (Hourly Weather)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "[SYSTEM_ACTION: RUN_GPS_WEATHER_DAEMON]",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0288D1), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(18.dp))
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Real-time GPS Location & Sync Daemon Badge
                Surface(
                    color = Color(0xFFE1F5FE),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Place, contentDescription = null, tint = Color(0xFF0288D1), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "مقام: پنجاب زرعی بیلٹ (بہاولپور و ملتان - پاکستان)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF01579B))
                            )
                            Text(
                                text = "60 منٹ پر خودکار اپ ڈیٹ شدہ لائیو موسمی رڈار",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0288D1), fontSize = 10.sp)
                            )
                        }
                    }
                }

                // Rain Probability Percentage Highlight
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                    border = BorderStroke(1.dp, Color(0xFF81C784))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFF0288D1), modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "بارش کا امکان (Rain Probability):",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Text(
                                text = "68%",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color(0xFF0D47A1)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { 0.68f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = Color(0xFF0288D1),
                            trackColor = Color.LightGray.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "🌧️ شدید بارش متوقع ہے - فصلوں کی نہری و ٹیوب ویل آبپاشی فوراً بند کریں!",
                            style = MaterialTheme.typography.labelSmall.copy(color = AgriRedDisease, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        )
                    }
                }

                // Sunrise & Sunset Timetables
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFF8E1),
                        border = BorderStroke(1.dp, Color(0xFFFFD54F))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color(0xFFF57F17), modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("طلوعِ آفتاب (Sunrise)", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color(0xFFE65100)))
                                Text("05:42 AM", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFEDE7F6),
                        border = BorderStroke(1.dp, Color(0xFFB39DDB))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFF512DA8), modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("غروبِ آفتاب (Sunset)", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color(0xFF4A148C)))
                                Text("06:48 PM", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                // 6-Hour Dynamic Hourly Forecast Strip
                Text(
                    text = "آئندہ گھنٹوں کی پیشن گوئی (Hourly Breakdown):",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(hourlyList) { item ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f)),
                            modifier = Modifier.width(92.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(item.timeLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Icon(item.icon, contentDescription = null, tint = Color(0xFF0288D1), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(item.temp, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                Text("${item.rainProb}% بارش", fontSize = 10.sp, color = Color(0xFF0277BD), fontWeight = FontWeight.Bold)
                                Text(item.conditionUrdu, fontSize = 9.sp, maxLines = 1)
                            }
                        }
                    }
                }

                // Dynamic Hourly Irrigation Guidance
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "💧 زرعی موسمیاتی آبپاشی ہدایت (Irrigation Command):",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "آئندہ 48 گھنٹوں میں 68% بارش اور 80% نمی کی پیش گوئی ہے۔ لہٰذا کپاس، دھان، مکئی اور باغات میں نیا پانی ہرگز نہ لگائیں۔ بارش کا پانی کھڑا نہ ہونے دیں تاکہ پھپھوندی کا پھیلاؤ روکا جا سکے۔",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF2E7D32), lineHeight = 16.sp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onRefresh,
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("تازہ ترین ڈیٹا ریفریش")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("بند کریں (Close)")
            }
        }
    )
}
