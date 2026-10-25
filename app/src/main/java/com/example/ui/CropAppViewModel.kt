package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CropScanEntity
import com.example.data.repository.CropScanRepository
import com.example.model.CropDiseaseInfo
import com.example.model.CropDiseaseKnowledgeBase
import com.example.network.FlaskCropService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface Screen {
    data object Splash : Screen
    data object SignIn : Screen
    data object Home : Screen
    data class Result(val info: CropDiseaseInfo, val imageUri: Uri?, val sourceText: String) : Screen
}

data class DiagnosisHistoryItem(
    val id: String,
    val diseaseName: String,
    val diseaseNameUrdu: String,
    val confidence: Float,
    val dateString: String,
    val imageUri: Uri?
)

data class CropUiState(
    val currentScreen: Screen = Screen.Splash,
    val isAnalyzing: Boolean = false,
    val analysisProgressText: String = "",
    val selectedImageUri: Uri? = null,
    val serverUrl: String = "http://10.0.2.2:5000/predict",
    val languageMode: String = "Dual", // "Dual", "English", "Urdu"
    val history: List<DiagnosisHistoryItem> = emptyList(),
    val cloudScans: List<CropScanEntity> = emptyList(),
    val currentUser: FirebaseUser? = null,
    val isCloudSyncing: Boolean = false,
    val errorMessage: String? = null
)

class CropAppViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CropUiState())
    val uiState: StateFlow<CropUiState> = _uiState.asStateFlow()

    private val cropService = FlaskCropService(application)
    private val scanRepository = CropScanRepository(application)
    private var cloudObservationJob: Job? = null

    init {
        // Track Firebase Auth state changes
        val authListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            _uiState.update { it.copy(currentUser = user) }
            if (user != null) {
                observeCloudScans()
            } else {
                cloudObservationJob?.cancel()
                _uiState.update { it.copy(cloudScans = emptyList()) }
            }
        }
        Firebase.auth.addAuthStateListener(authListener)

        // Automatic splash screen transition
        viewModelScope.launch {
            delay(2200)
            _uiState.update { it.copy(currentScreen = Screen.Home) }
        }
    }

    private fun observeCloudScans() {
        cloudObservationJob?.cancel()
        cloudObservationJob = viewModelScope.launch {
            scanRepository.observeScans()
                .catch { /* Handle/ignore error gracefully */ }
                .collect { scans ->
                    _uiState.update { it.copy(cloudScans = scans) }
                }
        }
    }

    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(selectedImageUri = uri, errorMessage = null) }
        analyzeImage(uri)
    }

    fun analyzeImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isAnalyzing = true, 
                    analysisProgressText = "Preprocessing crop image (224x224 RGB)..." 
                ) 
            }
            delay(400)
            _uiState.update { 
                it.copy(analysisProgressText = "Sending tensor array to CNN model..." ) 
            }
            
            try {
                val (result, source) = cropService.analyzeCropImage(uri, _uiState.value.serverUrl)
                
                // Add to local history
                val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                val historyEntry = DiagnosisHistoryItem(
                    id = System.currentTimeMillis().toString(),
                    diseaseName = result.diseaseName,
                    diseaseNameUrdu = result.diseaseNameUrdu,
                    confidence = result.confidence,
                    dateString = dateFormat.format(Date()),
                    imageUri = uri
                )

                // Save to Firestore if user is authenticated with Google
                if (Firebase.auth.currentUser != null) {
                    launch {
                        scanRepository.saveScan(
                            cropType = "Rice",
                            diseaseName = result.diseaseName,
                            diseaseNameUrdu = result.diseaseNameUrdu,
                            confidence = result.confidence.toDouble(),
                            severity = result.severity,
                            notes = "Diagnosed via CropDoc AI Scanner ($source)"
                        )
                    }
                }

                _uiState.update { state ->
                    state.copy(
                        isAnalyzing = false,
                        currentScreen = Screen.Result(result, uri, source),
                        history = listOf(historyEntry) + state.history
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isAnalyzing = false,
                        errorMessage = "Analysis failed: ${e.localizedMessage}"
                    ) 
                }
            }
        }
    }

    fun navigateToHome() {
        _uiState.update { it.copy(currentScreen = Screen.Home, selectedImageUri = null) }
    }

    fun navigateToSignIn() {
        _uiState.update { it.copy(currentScreen = Screen.SignIn) }
    }

    fun updateServerUrl(newUrl: String) {
        _uiState.update { it.copy(serverUrl = newUrl) }
    }

    fun setLanguageMode(mode: String) {
        _uiState.update { it.copy(languageMode = mode) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun createTempPictureFile(): File {
        val storageDir = getApplication<Application>().cacheDir
        return File.createTempFile("crop_scan_", ".jpg", storageDir)
    }
}
