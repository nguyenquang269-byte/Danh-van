package com.example.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class VietnameseSpeechEngine(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    private val _lastSpokenText = MutableStateFlow("")
    val lastSpokenText: StateFlow<String> = _lastSpokenText

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("vi", "VN"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to general Vietnamese or default locale
                tts?.setLanguage(Locale("vi"))
            }
            tts?.setSpeechRate(0.85f) // Slightly slower speech rate for young children to hear clearly
            tts?.setPitch(1.1f) // Pleasant slightly higher pitch suitable for kid learning

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
            })

            isInitialized = true
        } else {
            Log.e("SpeechEngine", "TextToSpeech initialization failed")
        }
    }

    /**
     * Standardized Vietnamese phonetic mapping for individual letters & consonants
     */
    fun getPhoneticPronunciation(raw: String): String {
        return when (raw.lowercase().trim()) {
            "a" -> "a"
            "ă" -> "á"
            "â" -> "ớ"
            "b" -> "bờ"
            "c" -> "cờ"
            "d" -> "dờ"
            "đ" -> "đờ"
            "e" -> "e"
            "ê" -> "ê"
            "g" -> "gờ"
            "h" -> "hờ"
            "i" -> "i"
            "k" -> "ca"
            "l" -> "lờ"
            "m" -> "mờ"
            "n" -> "nờ"
            "o" -> "o"
            "ô" -> "ô"
            "ơ" -> "ơ"
            "p" -> "pờ"
            "q" -> "quy"
            "r" -> "rờ"
            "s" -> "sờ"
            "t" -> "tờ"
            "u" -> "u"
            "ư" -> "ư"
            "v" -> "vờ"
            "x" -> "xờ"
            "y" -> "y"
            "ch" -> "chờ"
            "gh" -> "gờ"
            "gi" -> "giơ"
            "kh" -> "khờ"
            "nh" -> "nhờ"
            "ng" -> "ngờ"
            "ngh" -> "ngờ"
            "ph" -> "phờ"
            "qu" -> "quơ"
            "th" -> "thờ"
            "tr" -> "trờ"
            "sắc" -> "dấu sắc"
            "huyền" -> "dấu huyền"
            "hỏi" -> "dấu hỏi"
            "ngã" -> "dấu ngã"
            "nặng" -> "dấu nặng"
            else -> raw
        }
    }

    /**
     * Speaks plain text or full sentence clearly
     */
    fun speakText(text: String) {
        _lastSpokenText.value = text
        if (!isInitialized || tts == null) return
        
        tts?.stop()
        val formatted = text.replace("-", ", ")
        tts?.speak(formatted, TextToSpeech.QUEUE_FLUSH, null, "Utterance_${System.currentTimeMillis()}")
    }

    /**
     * Speaks step-by-step standardized Vietnamese spelling guide (e.g., "bờ - a - ba - sắc - bá")
     */
    fun speakSpellingGuide(spellingGuide: String, finalWord: String? = null) {
        val script = if (finalWord != null) {
            "$spellingGuide ... $finalWord"
        } else {
            spellingGuide
        }
        speakText(script)
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
