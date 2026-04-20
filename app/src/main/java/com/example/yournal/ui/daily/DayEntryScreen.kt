package com.example.yournal.ui.daily

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
import com.example.yournal.data.DailyTodo
import java.text.DateFormatSymbols

import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayEntryScreen(
    navController: NavController,
    year: Int,
    month: Int,
    day: Int,
) {
    val context = LocalContext.current
    val viewModel: DayEntryViewModel = viewModel(
        factory = DayEntryViewModelFactory(
            (context.applicationContext as YournalApplication).repository,
            year,
            month,
            day
        )
    )

    val dailyEntry by viewModel.dailyEntry.collectAsState()
    val todos by viewModel.todos.collectAsState()
    var note by remember(dailyEntry) { mutableStateOf(dailyEntry?.note ?: "") }
    var newTodo by remember { mutableStateOf("") }

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
                text = "${DateFormatSymbols().months[month - 1]} $day, $year",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Progress Indicator
            val completedTodos = todos.count { it.isCompleted }
            val totalTodos = todos.size
            val progress = if (totalTodos > 0) completedTodos.toFloat() / totalTodos else 0f

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
                    text = "$completedTodos / $totalTodos",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = "completed",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Journal Note
            OutlinedTextField(
                value = note,
                onValueChange = {
                    note = it
                    viewModel.upsertNote(it)
                },
                label = { Text("Journal Note") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Todos
            Text("Todos", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.align(Alignment.Start))
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                items(todos) { todo ->
                    TodoListItem(
                        todo = todo,
                        onUpdate = { viewModel.updateTodo(it) },
                        onDelete = { viewModel.deleteTodo(it) }
                    )
                }
            }

            // Add Todo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTodo,
                    onValueChange = { newTodo = it },
                    label = { Text("New Todo") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    if (newTodo.isNotBlank()) {
                        viewModel.addTodo(newTodo)
                        newTodo = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Todo")
                }
            }
        }
    }
}

@Composable
fun TodoListItem(
    todo: DailyTodo,
    onUpdate: (DailyTodo) -> Unit,
    onDelete: (DailyTodo) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = { onUpdate(todo.copy(isCompleted = it)) }
        )
        Text(text = todo.todo, modifier = Modifier.weight(1f))
        IconButton(onClick = { onDelete(todo) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Todo")
        }
    }
}
