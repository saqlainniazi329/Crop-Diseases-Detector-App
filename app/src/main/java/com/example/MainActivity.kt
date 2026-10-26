package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.CropAppViewModel
import com.example.ui.Screen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.SignInScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.CropDocTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CropAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CropDocTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CropDoctorApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun CropDoctorApp(viewModel: CropAppViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Crossfade(targetState = uiState.currentScreen, label = "screen_transition") { screen ->
        when (screen) {
            is Screen.Splash -> {
                SplashScreen()
            }
            is Screen.SignIn -> {
                SignInScreen(
                    onSignInSuccess = { viewModel.navigateToHome() },
                    onSkipSignIn = { viewModel.navigateToHome() }
                )
            }
            is Screen.Home -> {
                HomeScreen(uiState = uiState, viewModel = viewModel)
            }
            is Screen.Result -> {
                val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
                ResultScreen(
                    resultInfo = screen.info,
                    imageUri = screen.imageUri,
                    sourceText = screen.sourceText,
                    isSpeaking = isSpeaking,
                    onReplayVoice = { viewModel.replayVoice(screen.info) },
                    onStopVoice = { viewModel.stopVoice() },
                    onBackToHome = { viewModel.navigateToHome() }
                )
            }
        }
    }
}
