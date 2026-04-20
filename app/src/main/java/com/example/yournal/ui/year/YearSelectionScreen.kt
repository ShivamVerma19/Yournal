package com.example.yournal.ui.year

import androidx.compose.foundation.clickable
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
import com.example.yournal.data.YearEntity
import java.util.Calendar
import java.util.GregorianCalendar

import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearSelectionScreen(
    navController: NavController,
    onYearSelected: (Int) -> Unit
) {
    val context = LocalContext.current
    val viewModel: YearSelectionViewModel = viewModel(
        factory = YearSelectionViewModelFactory((context.applicationContext as YournalApplication).repository)
    )
    val years by viewModel.years.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<YearEntity?>(null) }
    var newYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR).toString()) }

    Scaffold(
        topBar = { com.example.yournal.ui.components.YournalToolbar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Year")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(years) { year ->
                val completedDays by viewModel.getEntryCountForYear(year.year).collectAsState(initial = 0)
                YearCard(
                    year = year,
                    completedDays = completedDays,
                    onYearSelected = onYearSelected,
                    onDelete = { showDeleteDialog = it }
                )
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add New Year") },
                text = {
                    OutlinedTextField(
                        value = newYear,
                        onValueChange = { newYear = it },
                        label = { Text("Year") }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val yearInt = newYear.toIntOrNull()
                            if (yearInt != null) {
                                viewModel.addYear(yearInt)
                            }
                            showAddDialog = false
                            newYear = Calendar.getInstance().get(Calendar.YEAR).toString()
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    Button(onClick = { showAddDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        showDeleteDialog?.let { yearToDelete ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Delete Year") },
                text = { Text("Are you sure you want to delete the year ${yearToDelete.year} and all its data?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteYear(yearToDelete)
                            showDeleteDialog = null
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun YearCard(
    year: YearEntity,
    completedDays: Int,
    onYearSelected: (Int) -> Unit,
    onDelete: (YearEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onYearSelected(year.year) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = year.year.toString(), style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = { onDelete(year) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Year")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress Section
            val totalDays = if (GregorianCalendar().isLeapYear(year.year)) 366 else 365
            val progress = if (totalDays > 0) completedDays.toFloat() / totalDays else 0f

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$completedDays / $totalDays days",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
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
