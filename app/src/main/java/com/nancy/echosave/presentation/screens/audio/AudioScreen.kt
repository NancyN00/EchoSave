package com.nancy.echosave.presentation.screens.audio

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.nancy.echosave.ElevenLabsConfig
import java.util.concurrent.TimeUnit

private val BrandBlue = Color(0xFF1967D2)

class AudioScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Audio"
            val icon = rememberVectorPainter(Icons.Default.Home)
            return remember { TabOptions(index = 0u, title = title, icon = icon) }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getViewModel<AudioViewModel>()
        val state by viewModel.uiState.collectAsState()
        val context = LocalContext.current
        val player = remember { AudioPlayer(context) }
        var selectedVoice by remember { mutableStateOf("Voice A") }
        val scrollState = rememberScrollState()

        LaunchedEffect(player.player) {
            viewModel.monitorPlayback(player.player)
        }

        DisposableEffect(Unit) {
            onDispose { player.release() }
        }

        LaunchedEffect(state.showSavedToast, state.errorMessage) {
            if (state.errorMessage != null) {
                Toast.makeText(context, "Error: ${state.errorMessage}", Toast.LENGTH_LONG).show()
                viewModel.toastShown()
            }
        }

        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6))) {

            TopAppBar(
                title = {
                    Text(
                        text = "EchoSave",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    Text(
                        text = "Audio",
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 16.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandBlue)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    TextField(
                        value = state.text,
                        onValueChange = viewModel::onTextChange,
                        placeholder = { Text("Enter text to convert...") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    Text(
                        text = "Curated AI Voices",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFE5E7EB))
                ) {
                    VoiceTab("Voice A", selectedVoice == "Voice A") { selectedVoice = "Voice A" }
                    VoiceTab("Voice B", selectedVoice == "Voice B") { selectedVoice = "Voice B" }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val selectedVoiceId =
                            if (selectedVoice == "Voice A")
                                "21m00Tcm4TlvDq8ikWAM"
                            else
                                "pNInz6obpgDQGcFmaJgB"

                        viewModel.generateAudio(
                            selectedVoiceId,
                            ElevenLabsConfig.MODEL_MULTILINGUAL
                        )
                    },
                    enabled = state.text.isNotBlank() && !state.isGenerating,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    if (state.isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(text = "Generate Audio", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledIconButton(
                            onClick = {
                                if (state.audioFile != null) {
                                    if (state.isPlaying) player.player.pause()
                                    else player.play(state.audioFile!!)
                                }
                            },
                            enabled = state.audioFile != null,
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = BrandBlue),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (state.isPlaying) Icons.Default.Close else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        val current = formatTime(state.currentPosition)
                        val total = formatTime(state.totalDuration)

                        Slider(
                            value = if (state.totalDuration > 0)
                                state.currentPosition.toFloat() / state.totalDuration.toFloat()
                            else 0f,
                            onValueChange = {},
                            modifier = Modifier.weight(1f),
                            enabled = state.audioFile != null
                        )

                        Text(text = "$current / $total", color = Color.DarkGray, fontSize = 14.sp)
                    }
                }

                AnimatedVisibility(visible = state.showSavedToast) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Saved ", color = BrandBlue, fontWeight = FontWeight.SemiBold)
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = BrandBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(text = "Upload Audio", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val mins = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    val secs = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60
    return String.format("%01d:%02d", mins, secs)
}

@Composable
fun VoiceTab(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) BrandBlue else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 32.dp, vertical = 12.dp)
    ) {
        Text(
            text = name,
            color = if (isSelected) Color.White else Color.DarkGray,
            fontWeight = FontWeight.SemiBold
        )
    }
}