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

            tts?.setSpeechRate(0.92f) // Clear, respectful cadence for farmers
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
     * Speaks the diagnosis to the farmer automatically with natural Urdu phrasing,
     * including leaf morphology, disease location on the plant, and government remedies.
     */
    fun speakDiagnosis(
        farmerName: String,
        cropOrTreeName: String,
        diseaseNameUrdu: String,
        severityUrdu: String,
        leafStructureUrdu: String,
        diseaseLocationUrdu: String,
        approvedMedicineUrdu: String,
        waterStatusUrdu: String,
        monitoringDaysUrdu: String,
        isUnknownOrRejected: Boolean = false,
        rejectionReasonUrdu: String = ""
    ) {
        val greeting = if (farmerName.isNotBlank() && farmerName != "Google User") {
            "السلام علیکم کسان بھائی $farmerName!"
        } else {
            "السلام علیکم پیارے کسان بھائی!"
        }

        val speechText = if (isUnknownOrRejected) {
            """
                $greeting
                توجہ فرمائیں! آپ کی بھیجی گئی تصویر کسی منظور شدہ فصل یا درخت کے پتے سے مطابقت نہیں رکھتی۔
                کسان کو کسی بھی غلط رہنمائی یا غلط دوائی کے نقصان سے بچانے کے لیے، سسٹم نے اس نتیجے کو روک دیا ہے۔
                مہربانی فرما کر پودے کے پتے کی واضح تصویر دوبارہ کھینچیں۔ شکریہ۔
            """.trimIndent()
        } else {
            """
                $greeting
                آپ کے $cropOrTreeName کا نباتاتی تجزیہ مکمل ہو چکا ہے۔
                پتے کی ساخت: $leafStructureUrdu کی تصدیق ہوئی ہے۔
                بیماری کی شناخت: $diseaseNameUrdu۔ یہ بیماری پودے کے اس حصے پر اثر انداز ہے: $diseaseLocationUrdu۔
                حکومتِ پاکستان اور محکمہ زراعت کی منظور شدہ ہدایت کے مطابق: $approvedMedicineUrdu
                آبپاشی کی صورتحال: $waterStatusUrdu
                آئندہ $monitoringDaysUrdu تک باقاعدہ فیلڈ معائنہ جاری رکھیں۔ اللہ تعالیٰ آپ کی فصل میں برکت عطا فرمائے۔
            """.trimIndent()
        }

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
