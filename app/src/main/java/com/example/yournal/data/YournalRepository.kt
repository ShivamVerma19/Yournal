package com.example.yournal.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class YournalRepository(
    private val yearDao: YearDao,
    private val monthlyGoalDao: MonthlyGoalDao,
    private val dailyEntryDao: DailyEntryDao,
    private val dailyTodoDao: DailyTodoDao,
    private val monthlyReviewDao: MonthlyReviewDao
) {

    // Year
    fun getAllYears(): Flow<List<YearEntity>> = yearDao.getAllYears()
    suspend fun insertYear(year: YearEntity) = withContext(Dispatchers.IO) { yearDao.insertYear(year) }
    suspend fun deleteYear(year: YearEntity) = withContext(Dispatchers.IO) { yearDao.deleteYear(year) }

    // MonthlyGoal
    fun getGoalsForMonth(year: Int, month: Int): Flow<List<MonthlyGoal>> =
        monthlyGoalDao.getGoalsForMonth(year, month)
    suspend fun upsertGoal(goal: MonthlyGoal) = withContext(Dispatchers.IO) { monthlyGoalDao.upsertGoal(goal) }
    suspend fun updateGoal(goal: MonthlyGoal) = withContext(Dispatchers.IO) { monthlyGoalDao.updateGoal(goal) }
    suspend fun deleteGoal(goal: MonthlyGoal) = withContext(Dispatchers.IO) { monthlyGoalDao.deleteGoal(goal) }

    // DailyEntry
    fun getEntriesForMonth(year: Int, month: Int): Flow<List<DailyEntry>> =
        dailyEntryDao.getEntriesForMonth(year, month)
    fun getEntryForDay(year: Int, month: Int, day: Int): Flow<DailyEntry?> =
        dailyEntryDao.getEntryForDay(year, month, day)
    fun getEntryCountForMonth(year: Int, month: Int): Flow<Int> =
        dailyEntryDao.getEntryCountForMonth(year, month)
    fun getEntryCountForYear(year: Int): Flow<Int> =
        dailyEntryDao.getEntryCountForYear(year)
    suspend fun upsertEntry(entry: DailyEntry): Long = withContext(Dispatchers.IO) { dailyEntryDao.upsertEntry(entry) }
    suspend fun deleteEntry(entry: DailyEntry) = withContext(Dispatchers.IO) { dailyEntryDao.deleteEntry(entry) }

    // DailyTodo
    fun getTodosForEntry(dailyEntryId: Int): Flow<List<DailyTodo>> =
        dailyTodoDao.getTodosForEntry(dailyEntryId)
    suspend fun upsertTodo(todo: DailyTodo) = withContext(Dispatchers.IO) { dailyTodoDao.upsertTodo(todo) }
    suspend fun updateTodo(todo: DailyTodo) = withContext(Dispatchers.IO) { dailyTodoDao.updateTodo(todo) }
    suspend fun deleteTodo(todo: DailyTodo) = withContext(Dispatchers.IO) { dailyTodoDao.deleteTodo(todo) }
    suspend fun deleteTodosForEntry(dailyEntryId: Int) = withContext(Dispatchers.IO) { dailyTodoDao.deleteTodosForEntry(dailyEntryId) }

    // MonthlyReview
    fun getReviewForMonth(year: Int, month: Int): Flow<MonthlyReview?> =
        monthlyReviewDao.getReviewForMonth(year, month)
    suspend fun upsertReview(review: MonthlyReview) = withContext(Dispatchers.IO) { monthlyReviewDao.upsertReview(review) }
    suspend fun updateReview(review: MonthlyReview) = withContext(Dispatchers.IO) { monthlyReviewDao.updateReview(review) }
}
