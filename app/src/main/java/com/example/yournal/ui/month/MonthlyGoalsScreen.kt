package com.example.yournal.ui.month

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yournal.YournalApplication
import com.example.yournal.data.MonthlyGoal
import java.text.DateFormatSymbols

import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyGoalsScreen(
    navController: NavController,
    year: Int,
    month: Int,
) {
    val context = LocalContext.current
    val viewModel: MonthlyGoalsViewModel = viewModel(
        factory = MonthlyGoalsViewModelFactory(
            (context.applicationContext as YournalApplication).repository,
            year,
            month
        )
    )

    val goals by viewModel.goals.collectAsState()
    var newGoal by remember { mutableStateOf("") }

    Scaffold(
        topBar = { com.example.yournal.ui.components.YournalToolbar(navController, showBackButton = true) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Monthly Goals for ${DateFormatSymbols().months[month - 1]} $year",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Progress Indicator
            val completedGoals = goals.count { it.isCompleted }
            val totalGoals = goals.size
            val progress = if (totalGoals > 0) completedGoals.toFloat() / totalGoals else 0f

            Box(contentAlignment = Alignment.Center) {
                // Background Track
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(100.dp),
                    strokeWidth = 8.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )
                // Foreground Progress
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(100.dp),
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "$completedGoals / $totalGoals",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = "goals completed",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Goals List
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                items(goals) { goal ->
                    GoalListItem(
                        goal = goal,
                        onUpdate = { viewModel.updateGoal(it) },
                        onDelete = { viewModel.deleteGoal(it) }
                    )
                }
            }

            // Add Goal
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newGoal,
                    onValueChange = { newGoal = it },
                    label = { Text("New Goal") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    if (newGoal.isNotBlank()) {
                        viewModel.addGoal(newGoal)
                        newGoal = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Goal")
                }
            }
        }
    }
}

@Composable
fun GoalListItem(
    goal: MonthlyGoal,
    onUpdate: (MonthlyGoal) -> Unit,
    onDelete: (MonthlyGoal) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = goal.isCompleted,
            onCheckedChange = { onUpdate(goal.copy(isCompleted = it)) }
        )
        Text(text = goal.goal, modifier = Modifier.weight(1f))
        IconButton(onClick = { onDelete(goal) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Goal")
        }
    }
}
