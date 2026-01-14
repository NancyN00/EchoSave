package com.nancy.echosave.presentation.screens.audio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nancy.echosave.domain.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun onTextChange(text: String) {
        _uiState.update { it.copy(text = text) }
    }

    fun generateAudio(voiceId: String, modelId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true) }
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
                        showSavedToast = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isGenerating = false) }
                Timber.e(e)
            }
        }
    }

    fun toastShown() {
        _uiState.update { it.copy(showSavedToast = false) }
    }
}