package com.nancy.echosave.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.nancy.echosave.presentation.screens.audio.AudioScreen
import com.nancy.echosave.presentation.screens.saved.SavedScreen

@Composable
fun MainScreen() {
    TabNavigator(AudioScreen()) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    TabNavigationItem(AudioScreen())
                    TabNavigationItem(SavedScreen())
                }
            }
        ) { paddingValues ->
            Surface(modifier = androidx.compose.ui.Modifier.padding(paddingValues)) {
                CurrentTab()
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current.key == tab.key,
        onClick = { tabNavigator.current = tab },
        label = { Text(tab.options.title) },
        icon = {
            Icon(
                painter = tab.options.icon!!,
                contentDescription = tab.options.title
            )
        }
    )
}
