package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.CropDiseaseInfo
import com.example.model.CropDiseaseKnowledgeBase
import com.example.network.FlaskCropService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface Screen {
    data object Splash : Screen
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
    val errorMessage: String? = null
)

class CropAppViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CropUiState())
    val uiState: StateFlow<CropUiState> = _uiState.asStateFlow()

    private val cropService = FlaskCropService(application)

    init {
        // Automatic splash screen transition
        viewModelScope.launch {
            delay(2200)
            _uiState.update { it.copy(currentScreen = Screen.Home) }
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
                
                // Add to history
                val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                val historyEntry = DiagnosisHistoryItem(
                    id = System.currentTimeMillis().toString(),
                    diseaseName = result.diseaseName,
                    diseaseNameUrdu = result.diseaseNameUrdu,
                    confidence = result.confidence,
                    dateString = dateFormat.format(Date()),
                    imageUri = uri
                )

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

    fun updateServerUrl(newUrl: String) {
        _uiState.update { it.copy(serverUrl = newUrl) }
    }

    fun setLanguageMode(mode: String) {
        _uiState.update { it.copy(languageMode = mode) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Helper to create a temporary camera storage file
     */
    fun createTempPictureFile(): File {
        val storageDir = getApplication<Application>().cacheDir
        return File.createTempFile("crop_scan_", ".jpg", storageDir)
    }
}
