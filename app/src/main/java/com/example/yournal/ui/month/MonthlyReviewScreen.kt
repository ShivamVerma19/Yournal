package com.example.yournal.ui.month

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yournal.YournalApplication
import java.text.DateFormatSymbols

import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyReviewScreen(
    navController: NavController,
    year: Int,
    month: Int
) {
    val context = LocalContext.current
    val viewModel: MonthlyReviewViewModel = viewModel(
        factory = MonthlyReviewViewModelFactory(
            (context.applicationContext as YournalApplication).repository,
            year,
            month
        )
    )

    val review by viewModel.review.collectAsState()
    var whatWentWell by remember(review) { mutableStateOf(review?.whatWentWell ?: "") }
    var whatDidntGoWell by remember(review) { mutableStateOf(review?.whatDidntGoWell ?: "") }
    var lessonsLearned by remember(review) { mutableStateOf(review?.lessonsLearned ?: "") }

    Scaffold(
        topBar = { com.example.yournal.ui.components.YournalToolbar(navController, showBackButton = true) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Monthly Review for ${DateFormatSymbols().months[month - 1]} $year",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = whatWentWell,
                onValueChange = {
                    whatWentWell = it
                    viewModel.upsertReview(it, whatDidntGoWell, lessonsLearned)
                },
                label = { Text("What went well?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = whatDidntGoWell,
                onValueChange = {
                    whatDidntGoWell = it
                    viewModel.upsertReview(whatWentWell, it, lessonsLearned)
                },
                label = { Text("What didn't go well?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = lessonsLearned,
                onValueChange = {
                    lessonsLearned = it
                    viewModel.upsertReview(whatWentWell, whatDidntGoWell, it)
                },
                label = { Text("Lessons learned") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}
