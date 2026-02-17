package com.nancy.echosave.presentation.screens.audio

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.hilt.getViewModel
import com.nancy.echosave.ElevenLabsConfig
import timber.log.Timber


class AudioScreen : Screen {

    override val key: ScreenKey = "AudioScreen"

    @Composable
    override fun Content() {

        val viewModel = getViewModel<AudioViewModel>()

        val state by viewModel.uiState.collectAsState()
        val context = LocalContext.current
        val player = remember { AudioPlayer(context) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1️⃣ Text input
            OutlinedTextField(
                value = state.text,
                onValueChange = viewModel::onTextChange,
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // 2️⃣ Generate button
            Button(
                onClick = {
                    viewModel.generateAudio(
                        voiceId = "Rachel",
                        modelId = ElevenLabsConfig.MODEL_MULTILINGUAL
                    )
                },
                enabled = state.text.isNotBlank() && !state.isGenerating,
                modifier = Modifier.widthIn(max = 280.dp),
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
                    Text(if (state.audioFile != null) "Regenerate Audio" else "Generate Audio")
                }
            }

            Spacer(Modifier.height(16.dp))

            // 3️⃣ Show audio ready text
            state.audioFile?.let { file ->
                Text(
                    text = "Audio ready: ${file.name}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(8.dp))

                // 4️⃣ Play button
                IconButton(
                    onClick = { player.play(file) },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Play generated audio",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // 5️⃣ Toast for saved audio
        if (state.showSavedToast) {
            LaunchedEffect(state.showSavedToast) {
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                viewModel.toastShown()
            }
        }
    }
}
