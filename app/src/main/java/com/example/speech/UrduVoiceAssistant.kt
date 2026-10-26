package com.example.speech

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class UrduVoiceAssistant(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Attempt to set Urdu (Pakistan) locale
            val urduPkLocale = Locale("ur", "PK")
            val urduLocale = Locale("ur")

            val resultPk = tts?.setLanguage(urduPkLocale)
            if (resultPk == TextToSpeech.LANG_MISSING_DATA || resultPk == TextToSpeech.LANG_NOT_SUPPORTED) {
                val resultUr = tts?.setLanguage(urduLocale)
                if (resultUr == TextToSpeech.LANG_MISSING_DATA || resultUr == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w("UrduVoiceAssistant", "Urdu language data missing, falling back to default locale")
                    tts?.language = Locale.getDefault()
                }
            }

            tts?.setSpeechRate(0.92f) // Slightly slower, calm cadence for clear comprehension
            tts?.setPitch(1.0f)

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    _isSpeaking.value = false
                }
            })

            isInitialized = true
        } else {
            Log.e("UrduVoiceAssistant", "TextToSpeech initialization failed with status $status")
        }
    }

    /**
     * Speaks the diagnosis to the farmer automatically with natural Urdu phrasing.
     */
    fun speakDiagnosis(
        farmerName: String,
        cropOrTreeName: String,
        diseaseNameUrdu: String,
        severityUrdu: String,
        approvedMedicineUrdu: String,
        waterStatusUrdu: String,
        monitoringDaysUrdu: String
    ) {
        val greeting = if (farmerName.isNotBlank() && farmerName != "Google User") {
            "السلام علیکم کسان بھائی $farmerName!"
        } else {
            "السلام علیکم پیارے کسان بھائی!"
        }

        val speechText = """
            $greeting
            آپ کے $cropOrTreeName کا ڈیجیٹل معائنہ مکمل ہو چکا ہے۔
            پودے میں $diseaseNameUrdu کی تشخیص ہوئی ہے۔ بیماری کی شدت $severityUrdu ہے۔
            حکومتِ پاکستان اور محکمہ زراعت کی منظور شدہ ہدایت کے مطابق: $approvedMedicineUrdu
            پانی کی صورتحال: $waterStatusUrdu
            آئندہ $monitoringDaysUrdu تک فصل کی کڑی نگرانی رکھیں۔ اللہ تعالیٰ آپ کی فصل میں برکت عطا فرمائے۔
        """.trimIndent()

        speakText(speechText)
    }

    fun speakText(text: String) {
        if (!isInitialized || tts == null) {
            Log.w("UrduVoiceAssistant", "TTS not initialized yet")
            return
        }

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "crop_diagnosis_voice")
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "crop_diagnosis_voice")
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
