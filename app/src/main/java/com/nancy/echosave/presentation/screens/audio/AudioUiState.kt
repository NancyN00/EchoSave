package com.nancy.echosave.presentation.screens.audio

import java.io.File
data class AudioUiState(
    val text: String = "",
    val isGenerating: Boolean = false,
    val audioFile: File? = null,
    val showSavedToast: Boolean = false,
    val errorMessage: String? = null,
    // Playback states
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L
)


