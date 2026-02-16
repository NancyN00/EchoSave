package com.nancy.echosave.navigation

import com.nancy.echosave.presentation.screens.audio.AudioScreen
import com.nancy.echosave.presentation.screens.saved.SavedScreen
import cafe.adriel.voyager.core.screen.Screen

enum class TabItem(
    val title: String,
    val screen: Screen
) {
    Audio(
        title = "Audio",
        screen = AudioScreen
    ),
    Saved(
        title = "Saved",
        screen = SavedScreen
    )
}


