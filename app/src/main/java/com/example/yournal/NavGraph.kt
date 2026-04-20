package com.example.yournal

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yournal.ui.daily.DailyEntriesScreen
import com.example.yournal.ui.daily.DayEntryScreen
import com.example.yournal.ui.month.MonthDetailScreen
import com.example.yournal.ui.month.MonthSelectionScreen
import com.example.yournal.ui.month.MonthlyGoalsScreen
import com.example.yournal.ui.month.MonthlyReviewScreen
import com.example.yournal.ui.settings.SettingsScreen
import com.example.yournal.ui.year.YearSelectionScreen

object Routes {
    const val YEAR_SELECTION = "year_selection"
    const val MONTH_SELECTION = "month_selection/{year}"
    const val MONTH_DETAILS = "month_details/{year}/{month}"
    const val MONTHLY_GOALS = "monthly_goals/{year}/{month}"
    const val DAILY_ENTRIES = "daily_entries/{year}/{month}"
    const val DAY_ENTRY = "day_entry/{year}/{month}/{day}"
    const val MONTHLY_REVIEW = "monthly_review/{year}/{month}"
    const val SETTINGS = "settings"
}

@Composable
fun YournalNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.YEAR_SELECTION
    ) {
        composable(Routes.YEAR_SELECTION) {
            YearSelectionScreen(
                navController = navController,
                onYearSelected = { year ->
                    navController.navigate("month_selection/$year")
                }
            )
        }
        composable(
            route = Routes.MONTH_SELECTION,
            arguments = listOf(navArgument("year") { type = NavType.IntType })
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year") ?: 0
            MonthSelectionScreen(
                navController = navController,
                year = year,
                onMonthSelected = { month ->
                    navController.navigate("month_details/$year/$month")
                }
            )
        }
        composable(
            route = Routes.MONTH_DETAILS,
            arguments = listOf(
                navArgument("year") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year") ?: 0
            val month = backStackEntry.arguments?.getInt("month") ?: 0
            MonthDetailScreen(
                navController = navController,
                year = year,
                month = month,
                onMonthlyGoalsClicked = { navController.navigate("monthly_goals/$year/$month") },
                onDailyEntriesClicked = { navController.navigate("daily_entries/$year/$month") },
                onMonthlyReviewClicked = { navController.navigate("monthly_review/$year/$month") }
            )
        }
        composable(
            route = Routes.MONTHLY_GOALS,
            arguments = listOf(
                navArgument("year") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year") ?: 0
            val month = backStackEntry.arguments?.getInt("month") ?: 0
            MonthlyGoalsScreen(
                navController = navController,
                year = year,
                month = month
            )
        }
        composable(
            route = Routes.DAILY_ENTRIES,
            arguments = listOf(
                navArgument("year") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year") ?: 0
            val month = backStackEntry.arguments?.getInt("month") ?: 0
            DailyEntriesScreen(
                navController = navController,
                year = year,
                month = month,
                onDaySelected = { day ->
                    navController.navigate("day_entry/$year/$month/$day")
                }
            )
        }
        composable(
            route = Routes.DAY_ENTRY,
            arguments = listOf(
                navArgument("year") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType },
                navArgument("day") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year") ?: 0
            val month = backStackEntry.arguments?.getInt("month") ?: 0
            val day = backStackEntry.arguments?.getInt("day") ?: 0
            DayEntryScreen(
                navController = navController,
                year = year,
                month = month,
                day = day
            )
        }
        composable(
            route = Routes.MONTHLY_REVIEW,
            arguments = listOf(
                navArgument("year") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year") ?: 0
            val month = backStackEntry.arguments?.getInt("month") ?: 0
            MonthlyReviewScreen(
                navController = navController,
                year = year,
                month = month
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(navController = navController)
        }
    }
}
