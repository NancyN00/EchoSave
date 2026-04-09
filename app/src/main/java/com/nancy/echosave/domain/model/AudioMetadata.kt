package com.nancy.echosave.domain.model


data class AudioMetadata(
    val id: String = "",
    val text: String = "",
    val voiceId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val filePath: String = ""
)
