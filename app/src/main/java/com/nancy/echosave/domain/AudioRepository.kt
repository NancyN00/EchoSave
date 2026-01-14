package com.nancy.echosave.domain

import java.io.File

interface AudioRepository {
    suspend fun generateAudio(
        text: String,
        voiceId: String,
        modelId: String
    ): File
}