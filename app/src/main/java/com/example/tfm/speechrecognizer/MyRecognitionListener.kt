package com.example.tfm.speechrecognizer

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import com.example.tfm.activity.ChatActivity
import com.example.tfm.util.LogUtil

class MyRecognitionListener : RecognitionListener{
    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEvent(eventType: Int, params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onResults(results: Bundle?) {}

    override fun onError(error: Int) {
        when(error){
            SpeechRecognizer.ERROR_AUDIO -> Log.d(LogUtil.TAG, "ERROR AUDIO")
            SpeechRecognizer.ERROR_CLIENT -> Log.d(LogUtil.TAG, "ERROR CLIENT")
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> Log.d(LogUtil.TAG, "ERROR_INSUFFICIENT_PERMISSIONS")
            SpeechRecognizer.ERROR_NETWORK -> Log.d(LogUtil.TAG, "ERROR NETWORK")
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> Log.d(LogUtil.TAG, "ERROR NETWORK TIMEOUT")
            SpeechRecognizer.ERROR_NO_MATCH -> Log.d(LogUtil.TAG, "ERROR NO MATCH")
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> Log.d(LogUtil.TAG, "ERROR RECOGNIZER BUSY")
            SpeechRecognizer.ERROR_SERVER -> Log.d(LogUtil.TAG, "ERROR SERVER")
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> Log.d(LogUtil.TAG, "ERROR SPEECH TIMEOUT")
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val voiceInput = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0).toString().capitalize()
        ChatActivity.emojiEditText.setText(voiceInput)
    }

    override fun onEndOfSpeech() {
    }
}