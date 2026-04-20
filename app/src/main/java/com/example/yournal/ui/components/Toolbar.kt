package com.example.yournal.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.yournal.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YournalToolbar(
    navController: NavController,
    showBackButton: Boolean = false
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var lastClickTime by remember { mutableLongStateOf(0L) }

    CenterAlignedTopAppBar(
        title = { Text("Yournal") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            IconButton(
                onClick = {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime > 500) {
                        lastClickTime = currentTime
                        navController.navigate(Routes.SETTINGS)
                    }
                },
                enabled = currentRoute != Routes.SETTINGS
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime > 500) {
                        lastClickTime = currentTime
                        navController.navigateUp()
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}
