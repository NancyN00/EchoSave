package com.nancy.echosave.data.repository

import android.content.Context
import com.nancy.echosave.data.ElevenLabsApi
import com.nancy.echosave.data.TextToSpeechRequest
import com.nancy.echosave.domain.AudioRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    private val api: ElevenLabsApi,
    @ApplicationContext private val context: Context
) : AudioRepository {

    override suspend fun generateAudio(
        text: String,
        voiceId: String,
        modelId: String
    ): File {
        val response = api.textToSpeech(
            voiceId = voiceId,
            body = TextToSpeechRequest(text = text, model_id = modelId)
        )

        if (!response.isSuccessful) {
            throw Exception("Failed to generate audio")
        }

        val bytes = response.body()!!.bytes()
        val file = File(context.cacheDir, "audio_${System.currentTimeMillis()}.mp3")
        file.writeBytes(bytes)
        return file
    }
}