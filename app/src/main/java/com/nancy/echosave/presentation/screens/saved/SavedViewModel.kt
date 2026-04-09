package com.nancy.echosave.presentation.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nancy.echosave.domain.AudioRepository
import com.nancy.echosave.domain.model.AudioMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class SavedUiState(
    val recordings: List<AudioMetadata> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val repository: AudioRepository
) : ViewModel() {

    val uiState: StateFlow<SavedUiState> = repository.getSavedAudios()
        .map { SavedUiState(recordings = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SavedUiState()
        )
}
