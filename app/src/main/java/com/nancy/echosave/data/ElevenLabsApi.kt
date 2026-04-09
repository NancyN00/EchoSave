package com.nancy.echosave.data

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


interface ElevenLabsApi {

    @POST("v1/text-to-speech/{voiceId}")
    @Headers(
        "Content-Type: application/json",
        "Accept: audio/mpeg"
    )
    suspend fun textToSpeech(
        @Path("voiceId") voiceId: String,
        @Body body: TextToSpeechRequest
    ): Response<ResponseBody>
}

data class TextToSpeechRequest(
    val text: String,
    val model_id: String,
    val voice_settings: VoiceSettings = VoiceSettings()
)

data class VoiceSettings(
    val stability: Float = 0.5f,
    val similarity_boost: Float = 0.5f
)