package com.nancy.echosave.domain

import com.nancy.echosave.domain.model.AudioMetadata
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AudioRepository {
    suspend fun generateAudio(
        text: String,
        voiceId: String,
        modelId: String
    ): File

    fun getSavedAudios(): Flow<List<AudioMetadata>>
}