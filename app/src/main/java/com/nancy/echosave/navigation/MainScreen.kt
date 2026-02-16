package com.nancy.echosave.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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


@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(TabItem.Audio) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                TabItem.values().forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = { Text(tab.title) },
                        icon = {
                            when (tab) {
                                TabItem.Audio -> Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Audio"
                                )

                                TabItem.Saved -> Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = "Saved"
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        Navigator(selectedTab.screen) {
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                CurrentScreen()
            }
        }

    }
}

