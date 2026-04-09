package com.nancy.echosave.presentation.screens.audio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nancy.echosave.ElevenLabsConfig
import com.nancy.echosave.domain.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(
    private val repository: AudioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AudioUiState())
    val uiState: StateFlow<AudioUiState> = _uiState.asStateFlow()

    private var playbackJob: Job? = null

    fun onTextChange(text: String) {
        _uiState.update { it.copy(text = text) }
    }


    fun generateAudio(voiceId: String, modelId: String) {
        viewModelScope.launch {
            println("--- ELEVENLABS DEBUG --- Key is: [${ElevenLabsConfig.API_KEY}]")
            _uiState.update { it.copy(isGenerating = true, audioFile = null, errorMessage = null) }
            try {
                val file = repository.generateAudio(
                    text = _uiState.value.text,
                    voiceId = voiceId,
                    modelId = modelId
                )

                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        audioFile = file,
                        showSavedToast = true,
                        text = ""
                    )
                }

                delay(3000)
                _uiState.update { it.copy(showSavedToast = false) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isGenerating = false, errorMessage = e.message) }
            }
        }
    }


    fun monitorPlayback(player: androidx.media3.common.Player) {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (true) {
                _uiState.update {
                    it.copy(
                        isPlaying = player.isPlaying,
                        currentPosition = player.currentPosition,
                        totalDuration = if (player.duration > 0) player.duration else 0L
                    )
                }
                delay(500)
            }
        }
    }

    fun toastShown() {
        _uiState.update { it.copy(showSavedToast = false, errorMessage = null) }
    }
}
