package com.nancy.echosave.data.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nancy.echosave.ElevenLabsConfig
import com.nancy.echosave.data.ElevenLabsApi
import com.nancy.echosave.data.TextToSpeechRequest
import com.nancy.echosave.domain.AudioRepository
import com.nancy.echosave.domain.model.AudioMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    private val api: ElevenLabsApi,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : AudioRepository {

    override suspend fun generateAudio(
        text: String,
        voiceId: String,
        modelId: String
    ): File {
        val response = api.textToSpeech(
            voiceId = voiceId,
            body = TextToSpeechRequest(text = text, model_id = modelId),
            apiKey = ElevenLabsConfig.API_KEY
        )

        if (!response.isSuccessful) {
            throw Exception("ElevenLabs API Error: ${response.code()}")
        }

        val bytes = response.body()?.bytes() ?: throw Exception("Received empty audio data")

        // Save the audio file to the local cache folder
        val file = File(context.cacheDir, "audio_${System.currentTimeMillis()}.mp3")
        file.writeBytes(bytes)

        // Automatically save the details to Firestore so they appear in Saved screen
        saveMetadata(text, voiceId, file.absolutePath)

        return file
    }

    override fun getSavedAudios(): Flow<List<AudioMetadata>> = callbackFlow {
        // real-time listener for the Firestore "saved_audios" collection
        val listenerRegistration = firestore.collection("saved_audios")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                // Convert Firestore documents into our AudioMetadata objects
                val items = snapshot?.toObjects(AudioMetadata::class.java) ?: emptyList()
                trySend(items)
            }

        // Keep the listener alive until the screen is closed
        awaitClose {
            listenerRegistration.remove()
        }
    }

    private suspend fun saveMetadata(text: String, voiceId: String, path: String) {
        val metadata = AudioMetadata(
            id = System.currentTimeMillis().toString(),
            text = text,
            voiceId = voiceId,
            filePath = path,
            timestamp = System.currentTimeMillis() // Track when it was created
        )

        // Push the metadata to Firestore "saved_audios" collection
        firestore.collection("saved_audios")
            .document(metadata.id)
            .set(metadata)
            .await()
    }
}
