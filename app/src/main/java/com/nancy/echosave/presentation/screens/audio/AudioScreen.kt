package com.nancy.echosave.presentation.screens.audio

import android.R.attr.contentDescription
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.nancy.echosave.ElevenLabsConfig

object AudioScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: AudioViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsState()
        val context = LocalContext.current
        val player = remember { AudioPlayer(context) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = state.text,
                onValueChange = viewModel::onTextChange,
                label = { Text("Enter text") },
                modifier = Modifier
                    .fillMaxWidth()
                 //   .semantics { contentDescription = "Text input for audio generation" }
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.generateAudio(
                        voiceId = "Rachel",
                        modelId = ElevenLabsConfig.MODEL_MULTILINGUAL
                    )
                },
                enabled = state.text.isNotBlank() && !state.isGenerating,
                modifier = Modifier
                    .widthIn(max = 280.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (state.isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Generating...")
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Generate Audio")
                }
            }

            Spacer(Modifier.height(16.dp))

            state.audioFile?.let {
                IconButton(
                    onClick = { player.play(it) },
                  //  modifier = Modifier.semantics { contentDescription = "Play generated audio" }
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                }
            }
        }

        if (state.showSavedToast) {
            LaunchedEffect(state.showSavedToast) {
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                viewModel.toastShown()
            }
        }
    }
}