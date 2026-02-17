package com.nancy.echosave.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.nancy.echosave.presentation.screens.audio.AudioScreen


@Composable
fun MainScreen() {
    Navigator(AudioScreen) { navigator ->
        Scaffold(
            bottomBar = {
                NavigationBar {
                    TabItem.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = navigator.lastItem == tab.screen,
                            onClick = {
                                if (navigator.lastItem != tab.screen) {
                                    navigator.replaceAll(tab.screen)
                                }
                            },
                            label = { Text(tab.title) },
                            icon = {
                                Icon(
                                    imageVector = if (tab == TabItem.Audio) Icons.Default.Home else Icons.Default.List,
                                    contentDescription = tab.title
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                CurrentScreen()
            }
        }
    }
}
