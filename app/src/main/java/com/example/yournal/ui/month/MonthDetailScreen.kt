package com.example.yournal.ui.month

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.DateFormatSymbols

import androidx.compose.material3.Scaffold

import androidx.navigation.NavController

@Composable
fun MonthDetailScreen(
    navController: NavController,
    year: Int,
    month: Int,
    onMonthlyGoalsClicked: () -> Unit,
    onDailyEntriesClicked: () -> Unit,
    onMonthlyReviewClicked: () -> Unit
) {
    val monthName = DateFormatSymbols().months[month - 1]
    Scaffold(
        topBar = { com.example.yournal.ui.components.YournalToolbar(navController, showBackButton = true) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "$monthName $year", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onMonthlyGoalsClicked, modifier = Modifier.fillMaxWidth()) {
                Text("Monthly Goals")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onDailyEntriesClicked, modifier = Modifier.fillMaxWidth()) {
                Text("Daily Entries")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onMonthlyReviewClicked, modifier = Modifier.fillMaxWidth()) {
                Text("Monthly Review")
            }
        }
    }
}
