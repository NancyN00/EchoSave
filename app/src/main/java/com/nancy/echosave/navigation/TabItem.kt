package com.nancy.echosave.navigation

import com.nancy.echosave.presentation.AudioScreen
import com.nancy.echosave.presentation.SavedScreen

enum class TabItem(val title: String, val screen: Screen) {
    Audio("Audio", AudioScreen),
    Saved("Saved", SavedScreen)
}
