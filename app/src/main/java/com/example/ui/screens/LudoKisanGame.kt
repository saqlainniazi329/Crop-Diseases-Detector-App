package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class LudoGameMode(val titleUrdu: String, val titleEnglish: String) {
    VS_COMPUTER("کمپیوٹر کے خلاف", "VS Computer"),
    TWO_PLAYER("دو کھلاڑی لوکل", "2-Player Local"),
    FOUR_PLAYER_TEAM("چار کھلاڑی جٹ-جٹ اتحاد", "4-Player Team Mode")
}

data class LudoToken(
    val id: Int,
    val playerIndex: Int, // 0: Green, 1: Red, 2: Yellow, 3: Blue
    val stepPosition: Int = 0, // 0 = Yard, 1..56 = Track/Home
    val isHome: Boolean = false
)

@Composable
fun LudoKisanGameDialog(
    appCurrentLang: String = "UR",
    onDismiss: () -> Unit
) {
    val isEn = appCurrentLang == "EN"

    var selectedMode by remember { mutableStateOf(LudoGameMode.FOUR_PLAYER_TEAM) }
    var currentTurn by remember { mutableIntStateOf(0) } // 0: Green, 1: Red, 2: Yellow, 3: Blue
    var diceValue by remember { mutableIntStateOf(6) }
    var isRolling by remember { mutableStateOf(false) }
    var gameStatusText by remember {
        mutableStateOf(if (isEn) "Roll dice to advance your tokens!" else "ڈائس گھمائیں اور اپنی گوٹی آگے بڑھائیں!")
    }
    var winningTeamMessage by remember { mutableStateOf<String?>(null) }
    val diceRotation by animateFloatAsState(
        targetValue = if (isRolling) 360f else 0f,
        animationSpec = tween(400),
        label = "dice_rotation"
    )

    val coroutineScope = rememberCoroutineScope()

    // 4 tokens per player
    var tokens by remember {
        mutableStateOf(
            listOf(
                // Player 0 (Green - Jatt Team A)
                LudoToken(id = 1, playerIndex = 0, stepPosition = 12),
                LudoToken(id = 2, playerIndex = 0, stepPosition = 24),
                // Player 1 (Red - Kisan Team B)
                LudoToken(id = 3, playerIndex = 1, stepPosition = 15),
                LudoToken(id = 4, playerIndex = 1, stepPosition = 30),
                // Player 2 (Yellow - Jatt Team A)
                LudoToken(id = 5, playerIndex = 2, stepPosition = 18),
                LudoToken(id = 6, playerIndex = 2, stepPosition = 36),
                // Player 3 (Blue - Kisan Team B)
                LudoToken(id = 7, playerIndex = 3, stepPosition = 9),
                LudoToken(id = 8, playerIndex = 3, stepPosition = 42)
            )
        )
    }

    val teamScores = remember(tokens) {
        val teamAScore = tokens.filter { it.playerIndex == 0 || it.playerIndex == 2 }.sumOf { it.stepPosition * 10 }
        val teamBScore = tokens.filter { it.playerIndex == 1 || it.playerIndex == 3 }.sumOf { it.stepPosition * 10 }
        Pair(teamAScore, teamBScore)
    }

    // AI Turn Auto-mover for VS Computer Mode
    LaunchedEffect(currentTurn, selectedMode) {
        if (selectedMode == LudoGameMode.VS_COMPUTER && currentTurn == 1 && winningTeamMessage == null) {
            delay(700)
            isRolling = true
            delay(400)
            val aiRoll = Random.nextInt(1, 7) // [GAME_ENGINE: LUDO_KING_CORE] pseudo-random physics
            diceValue = aiRoll
            isRolling = false
            gameStatusText = if (isEn) "Computer AI rolled $aiRoll!" else "کمپیوٹر نے $aiRoll ڈائس پھینکا!"
            delay(500)

            // Move AI token
            tokens = tokens.map { token ->
                if (token.playerIndex == 1 && token.id == 3) {
                    val nextStep = (token.stepPosition + aiRoll).coerceAtMost(56)
                    token.copy(stepPosition = nextStep, isHome = nextStep >= 56)
                } else token
            }

            // Check win
            val aiWon = tokens.any { it.playerIndex == 1 && it.isHome }
            if (aiWon) {
                winningTeamMessage = if (isEn)
                    "Winning Team: Computer AI! | Losing Team: Farmer"
                else
                    "ہارنے والی ٹیم: کسان بھائی | فاتح ٹیم: کمپیوٹر AI!"
            } else {
                currentTurn = 0
                gameStatusText = if (isEn) "Farmer's turn! Roll the dice." else "کسان بھائی کی باری! ڈائس رول کریں۔"
            }
        }
    }

    fun handleDiceRoll() {
        if (isRolling || winningTeamMessage != null) return
        coroutineScope.launch {
            isRolling = true
            delay(350)
            val roll = Random.nextInt(1, 7) // [GAME_ENGINE: LUDO_KING_CORE] pseudo-random physics
            diceValue = roll
            isRolling = false

            // Move the active player's primary token
            val playerTokens = tokens.filter { it.playerIndex == currentTurn }
            val chosenToken = playerTokens.minByOrNull { it.stepPosition } ?: playerTokens.first()

            val nextPos = (chosenToken.stepPosition + roll).coerceAtMost(56)
            val isSafeZone = nextPos % 8 == 0 // Safe star zones at 8, 16, 24, 32, 40, 48

            // Rule-based token cutting on non-safe zone
            var cutHappened = false
            tokens = tokens.map { other ->
                if (other.id == chosenToken.id) {
                    other.copy(stepPosition = nextPos, isHome = nextPos >= 56)
                } else if (!isSafeZone && other.stepPosition == nextPos && other.playerIndex != currentTurn) {
                    // Cut opposing token back to start (Yard)
                    cutHappened = true
                    other.copy(stepPosition = 2)
                } else {
                    other
                }
            }

            val safeMessage = if (isSafeZone) (if (isEn) " ⭐ Safe Star Zone!" else " ⭐ گوٹی محفوظ اسٹار زون پر!") else ""
            val cutMessage = if (cutHappened) (if (isEn) " ⚔️ Opponent Token Cut!" else " ⚔️ حریف کی گوٹی کٹ گئی!") else ""

            when (selectedMode) {
                LudoGameMode.VS_COMPUTER -> {
                    if (nextPos >= 56) {
                        winningTeamMessage = if (isEn)
                            "Winning Team: Farmer! | Losing Team: Computer AI"
                        else
                            "فاتح ٹیم: کسان بھائی! | ہارنے والی ٹیم: کمپیوٹر AI"
                    } else {
                        gameStatusText = (if (isEn) "Your Roll: $roll!" else "آپ کا ڈائس: $roll!") + safeMessage + cutMessage
                        currentTurn = 1
                    }
                }
                LudoGameMode.TWO_PLAYER -> {
                    if (nextPos >= 56) {
                        winningTeamMessage = if (currentTurn == 0)
                            (if (isEn) "Winning Team: Player 1 (Green)! | Losing Team: Player 2 (Red)" else "فاتح ٹیم: کھلاڑی 1 (سبز) | ہارنے والی ٹیم: کھلاڑی 2 (سرخ)")
                        else
                            (if (isEn) "Winning Team: Player 2 (Red)! | Losing Team: Player 1 (Green)" else "فاتح ٹیم: کھلاڑی 2 (سرخ) | ہارنے والی ٹیم: کھلاڑی 1 (سبز)")
                    } else {
                        val nextPlayer = (currentTurn + 1) % 2
                        currentTurn = nextPlayer
                        gameStatusText = (if (isEn) "Roll: $roll!" else "ڈائس رول: $roll!") + safeMessage + cutMessage
                    }
                }
                LudoGameMode.FOUR_PLAYER_TEAM -> {
                    val (scoreA, scoreB) = teamScores
                    if (scoreA >= 900) {
                        winningTeamMessage = if (isEn)
                            "Winning Team: Jatt-Jatt Alliance (Team A)! 🏆\nLosing Team: Kisan Alliance (Team B)"
                        else
                            "🏆 فاتح ٹیم: جٹ-جٹ اتحاد (Team A) شاندار فتح! 🏆\nہارنے والی ٹیم: کسان الائنس (Team B)"
                    } else if (scoreB >= 900) {
                        winningTeamMessage = if (isEn)
                            "Winning Team: Kisan Alliance (Team B)! 🏆\nLosing Team: Jatt Alliance (Team A)"
                        else
                            "🏆 فاتح ٹیم: کسان الائنس (Team B) شاندار فتح! 🏆\nہارنے والی ٹیم: جٹ الائنس (Team A)"
                    } else {
                        val nextPlayer = (currentTurn + 1) % 4
                        currentTurn = nextPlayer
                        val nextName = when (nextPlayer) {
                            0 -> if (isEn) "Jatt Alliance 1 (Green)" else "جٹ الائنس 1 (سبز)"
                            1 -> if (isEn) "Kisan Team 1 (Red)" else "کسان ٹیم 1 (سرخ)"
                            2 -> if (isEn) "Jatt Alliance 2 (Yellow)" else "جٹ الائنس 2 (پیلا)"
                            else -> if (isEn) "Kisan Team 2 (Blue)" else "کسان ٹیم 2 (نیلا)"
                        }
                        gameStatusText = (if (isEn) "Roll: $roll! Next Turn: $nextName" else "رول: $roll! اگلی باری: $nextName") + safeMessage + cutMessage
                    }
                }
            }
        }
    }

    fun resetGame() {
        tokens = listOf(
            LudoToken(id = 1, playerIndex = 0, stepPosition = 6),
            LudoToken(id = 2, playerIndex = 0, stepPosition = 14),
            LudoToken(id = 3, playerIndex = 1, stepPosition = 8),
            LudoToken(id = 4, playerIndex = 1, stepPosition = 22),
            LudoToken(id = 5, playerIndex = 2, stepPosition = 12),
            LudoToken(id = 6, playerIndex = 2, stepPosition = 28),
            LudoToken(id = 7, playerIndex = 3, stepPosition = 16),
            LudoToken(id = 8, playerIndex = 3, stepPosition = 32)
        )
        currentTurn = 0
        winningTeamMessage = null
        gameStatusText = if (isEn) "Game restarted. Roll the dice!" else "کھیل دوبارہ شروع ہو گیا۔ ڈائس پھینکیں!"
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ludo_kisan_fullscreen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFF1B5E20), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.SportsEsports, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isEn) "Ludo Kisan Pro" else "لڈو کسان پرو",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Text(
                            text = "[SYSTEM_ACTION: BOOT_LUDO_KISAN_CORE]",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 9.sp)
                        )
                    }
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Game Mode Switcher
            TabRow(
                selectedTabIndex = selectedMode.ordinal,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                contentColor = Color(0xFF1B5E20)
            ) {
                LudoGameMode.values().forEachIndexed { index, mode ->
                    Tab(
                        selected = selectedMode.ordinal == index,
                        onClick = {
                            selectedMode = mode
                            resetGame()
                        },
                        text = {
                            Text(
                                text = if (isEn) mode.titleEnglish else mode.titleUrdu,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (selectedMode.ordinal == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dual Alliance Scoreboard (4-Player Team Mode)
            if (selectedMode == LudoGameMode.FOUR_PLAYER_TEAM) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color(0xFF81C784))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                if (isEn) "Jatt Alliance (Green + Yellow)" else "جٹ الائنس (سبز + پیلا)",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                fontSize = 12.sp
                            )
                            Text("${teamScores.first} pts", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF1B5E20))
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(Color.Gray.copy(alpha = 0.4f))
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                if (isEn) "Kisan Alliance (Red + Blue)" else "کسان الائنس (سرخ + نیلا)",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828),
                                fontSize = 12.sp
                            )
                            Text("${teamScores.second} pts", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFFB71C1C))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Interactive Ludo Board Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color(0xFFF9FBE7), RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFF33691E), RoundedCornerShape(16.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                LudoBoardCanvas(tokens = tokens, activePlayer = currentTurn)

                // Winner Overlay Banner
                if (winningTeamMessage != null) {
                    Surface(
                        color = Color(0xFF1B5E20).copy(alpha = 0.94f),
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 8.dp,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFFD54F), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = winningTeamMessage!!,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { resetGame() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD54F))
                            ) {
                                Text(if (isEn) "Rematch" else "دوبارہ کھیلیں", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Commentary & Turn Banner
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val turnColor = when (currentTurn) {
                        0 -> Color(0xFF2E7D32)
                        1 -> Color(0xFFC62828)
                        2 -> Color(0xFFFBC02D)
                        else -> Color(0xFF1565C0)
                    }
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(turnColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = gameStatusText,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive 3D Dice and Action Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Interactive Dice Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 6.dp,
                    border = BorderStroke(2.dp, Color(0xFF2E7D32)),
                    modifier = Modifier
                        .size(80.dp)
                        .clickable(enabled = !isRolling && (selectedMode != LudoGameMode.VS_COMPUTER || currentTurn == 0)) {
                            handleDiceRoll()
                        }
                        .testTag("ludo_dice_roll_btn")
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(diceRotation),
                        contentAlignment = Alignment.Center
                    ) {
                        DiceFace(value = diceValue)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = { handleDiceRoll() },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        enabled = !isRolling && winningTeamMessage == null
                    ) {
                        Icon(Icons.Default.Casino, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isEn) "Roll Dice" else "ڈائس گھمائیں", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedButton(
                        onClick = { resetGame() },
                        modifier = Modifier.fillMaxWidth().height(36.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isEn) "Reset Game" else "نیا راؤنڈ شروع کریں", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun DiceFace(value: Int) {
    val dots = when (value) {
        1 -> listOf(Offset(0.5f, 0.5f))
        2 -> listOf(Offset(0.3f, 0.3f), Offset(0.7f, 0.7f))
        3 -> listOf(Offset(0.25f, 0.25f), Offset(0.5f, 0.5f), Offset(0.75f, 0.75f))
        4 -> listOf(Offset(0.3f, 0.3f), Offset(0.7f, 0.3f), Offset(0.3f, 0.7f), Offset(0.7f, 0.7f))
        5 -> listOf(Offset(0.25f, 0.25f), Offset(0.75f, 0.25f), Offset(0.5f, 0.5f), Offset(0.25f, 0.75f), Offset(0.75f, 0.75f))
        else -> listOf(
            Offset(0.3f, 0.2f), Offset(0.7f, 0.2f),
            Offset(0.3f, 0.5f), Offset(0.7f, 0.5f),
            Offset(0.3f, 0.8f), Offset(0.7f, 0.8f)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize().padding(14.dp)) {
        dots.forEach { dot ->
            drawCircle(
                color = Color(0xFFC62828),
                radius = size.width * 0.1f,
                center = Offset(size.width * dot.x, size.height * dot.y)
            )
        }
    }
}

@Composable
private fun LudoBoardCanvas(
    tokens: List<LudoToken>,
    activePlayer: Int
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val stepW = w / 15f
        val stepH = h / 15f

        // Player Bases (Green, Red, Blue, Yellow)
        drawRect(Color(0xFF2E7D32), Offset(0f, 0f), Size(stepW * 6, stepH * 6))
        drawRect(Color(0xFFC62828), Offset(stepW * 9, 0f), Size(stepW * 6, stepH * 6))
        drawRect(Color(0xFF1565C0), Offset(0f, stepH * 9), Size(stepW * 6, stepH * 6))
        drawRect(Color(0xFFFBC02D), Offset(stepW * 9, stepH * 9), Size(stepW * 6, stepH * 6))

        // Center Triangle / Home
        drawRect(Color(0xFFFFD54F), Offset(stepW * 6, stepH * 6), Size(stepW * 3, stepH * 3))

        // Grid lines
        for (i in 0..15) {
            drawLine(Color.Gray.copy(alpha = 0.3f), Offset(i * stepW, 0f), Offset(i * stepW, h), strokeWidth = 1f)
            drawLine(Color.Gray.copy(alpha = 0.3f), Offset(0f, i * stepH), Offset(w, i * stepH), strokeWidth = 1f)
        }

        // Draw Stars at Safe Zones
        val safeStars = listOf(
            Offset(stepW * 2.5f, stepH * 6.5f),
            Offset(stepW * 6.5f, stepH * 2.5f),
            Offset(stepW * 8.5f, stepH * 1.5f),
            Offset(stepW * 12.5f, stepH * 8.5f),
            Offset(stepW * 8.5f, stepH * 12.5f),
            Offset(stepW * 6.5f, stepH * 13.5f),
            Offset(stepW * 1.5f, stepH * 8.5f)
        )
        safeStars.forEach { starCenter ->
            drawCircle(Color(0xFFFFB300), radius = stepW * 0.35f, center = starCenter)
        }

        // Draw Player Tokens on Board
        tokens.forEach { token ->
            val color = when (token.playerIndex) {
                0 -> Color(0xFF2E7D32)
                1 -> Color(0xFFC62828)
                2 -> Color(0xFFFBC02D)
                else -> Color(0xFF1565C0)
            }

            val col = when (token.playerIndex) {
                0 -> (token.stepPosition % 15).toFloat()
                1 -> (14 - (token.stepPosition % 15)).toFloat()
                2 -> (token.stepPosition % 15).toFloat()
                else -> (14 - (token.stepPosition % 15)).toFloat()
            }.coerceIn(0f, 14f)

            val row = when (token.playerIndex) {
                0 -> 6.5f
                1 -> 7.5f
                2 -> 8.5f
                else -> 6.5f
            }

            val center = Offset((col + 0.5f) * stepW, (row + 0.5f) * stepH)

            // Token Circle with 3D Ring
            drawCircle(color = color, radius = stepW * 0.38f, center = center)
            drawCircle(color = Color.White, radius = stepW * 0.38f, center = center, style = Stroke(width = 2f))
            drawCircle(color = Color.White.copy(alpha = 0.6f), radius = stepW * 0.16f, center = center)
        }
    }
}
