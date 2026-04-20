package com.example.yournal.ui.month

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yournal.YournalApplication
import java.util.Calendar
import java.util.GregorianCalendar

import androidx.compose.material3.Scaffold
import androidx.compose.material3.LinearProgressIndicator

import androidx.navigation.NavController

@Composable
fun MonthSelectionScreen(
    navController: NavController,
    year: Int,
    onMonthSelected: (Int) -> Unit
) {
    val context = LocalContext.current
    val viewModel: MonthSelectionViewModel = viewModel(
        factory = MonthSelectionViewModelFactory((context.applicationContext as YournalApplication).repository)
    )
    val months = viewModel.months

    Scaffold(
        topBar = { com.example.yournal.ui.components.YournalToolbar(navController, showBackButton = true) }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(months) { index, month ->
                val completedDays by viewModel.getEntryCountForMonth(year, index + 1).collectAsState(initial = 0)
                MonthCard(
                    month = month,
                    year = year,
                    monthIndex = index + 1,
                    completedDays = completedDays,
                    onMonthSelected = { onMonthSelected(index + 1) }
                )
            }
        }
    }
}

@Composable
fun MonthCard(month: String, year: Int, monthIndex: Int, completedDays: Int, onMonthSelected: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1.2f) // Adjusted ratio
            .clickable(onClick = onMonthSelected),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = month, style = MaterialTheme.typography.headlineSmall)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val calendar = GregorianCalendar(year, monthIndex - 1, 1)
            val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val progress = if (totalDays > 0) completedDays.toFloat() / totalDays else 0f

            Text(
                text = "$completedDays / $totalDays days",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                trackColor = MaterialTheme.colorScheme.surfaceVariant, // Use explicit surfaceVariant for visibility
                strokeCap = StrokeCap.Round
            )
        }
    }
}
