package com.nancy.echosave.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.nancy.echosave.presentation.screens.audio.AudioScreen
import com.nancy.echosave.presentation.screens.saved.SavedScreen

sealed class MainTab(
    val title: String,
    val icon: ImageVector,
    val createScreen: () -> Screen
) {
    object Audio : MainTab("Audio", Icons.Default.Home, { AudioScreen() })
    object Saved : MainTab("Saved", Icons.Default.List, { SavedScreen() })
}
@Composable
fun MainScreen() {
    // Start with a fresh instance
    Navigator(AudioScreen()) { navigator ->
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val tabs = listOf(MainTab.Audio, MainTab.Saved)

                    tabs.forEach { tab ->
                        val isSelected = navigator.lastItem::class == tab.createScreen()::class

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    navigator.replaceAll(tab.createScreen())
                                }
                            },
                            label = { Text(tab.title) },
                            icon = {
                                Icon(
                                    tab.icon,
                                    contentDescription = "${tab.title} tab"
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Blue,
                                selectedTextColor = Color.Blue
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CurrentScreen()
            }
        }
    }
}