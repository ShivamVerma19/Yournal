package com.example.yournal.ui.daily

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yournal.YournalApplication
import com.example.yournal.data.DailyEntry

import androidx.compose.material3.Scaffold

import androidx.navigation.NavController

@Composable
fun DailyEntriesScreen(
    navController: NavController,
    year: Int,
    month: Int,
    onDaySelected: (Int) -> Unit
) {
    val context = LocalContext.current
    val viewModel: DailyEntriesViewModel = viewModel(
        factory = DailyEntriesViewModelFactory((context.applicationContext as YournalApplication).repository)
    )
    val daysInMonthCount = viewModel.getDaysInMonth(year, month)
    val entries by viewModel.getEntriesForMonth(year, month).collectAsState(initial = emptyList())

    Scaffold(
        topBar = { com.example.yournal.ui.components.YournalToolbar(navController, showBackButton = true) }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(daysInMonthCount) { index ->
                val day = index + 1
                val entry = entries.find { it.day == day }
                val todos by if (entry != null) {
                    viewModel.getTodosForEntry(entry.id).collectAsState(initial = emptyList())
                } else {
                    remember { mutableStateOf(emptyList()) }
                }

                val hasNote = entry != null && !entry.note.isNullOrBlank()
                val hasTodos = todos.isNotEmpty()
                val isCompleted = hasNote && hasTodos

                DayCard(
                    day = day, 
                    isCompleted = isCompleted,
                    onDaySelected = { onDaySelected(day) }
                )
            }
        }
    }
}

@Composable
fun DayCard(day: Int, isCompleted: Boolean, onDaySelected: () -> Unit) {
    // Determine if we are in dark mode based on the current theme's background luminance
    val isDark = MaterialTheme.colorScheme.background.let { 
        (it.red * 0.299 + it.green * 0.587 + it.blue * 0.114) < 0.5 
    }
    
    // Incomplete day color logic
    val incompleteColor = if (isDark) Color(0xFF36454F) else Color(0xFFF0F8FF)
    
    // Final card color logic
    val cardColors = if (isCompleted) {
        CardDefaults.cardColors() // Keep theme default for completed days
    } else {
        CardDefaults.cardColors(containerColor = incompleteColor)
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onDaySelected),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = cardColors
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = day.toString(), style = MaterialTheme.typography.headlineSmall)
        }
    }
}
