package com.example.yournal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface YearDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertYear(year: YearEntity)

    @Query("SELECT * FROM years ORDER BY year DESC")
    fun getAllYears(): Flow<List<YearEntity>>

    @Delete
    fun deleteYear(year: YearEntity)
}

@Dao
interface MonthlyGoalDao {
    @Upsert
    fun upsertGoal(goal: MonthlyGoal)

    @Query("SELECT * FROM monthly_goals WHERE year = :year AND month = :month")
    fun getGoalsForMonth(year: Int, month: Int): Flow<List<MonthlyGoal>>

    @Update
    fun updateGoal(goal: MonthlyGoal)

    @Delete
    fun deleteGoal(goal: MonthlyGoal)
}

@Dao
interface DailyEntryDao {
    @Upsert
    fun upsertEntry(entry: DailyEntry): Long

    @Query("SELECT * FROM daily_entries WHERE year = :year AND month = :month ORDER BY day ASC")
    fun getEntriesForMonth(year: Int, month: Int): Flow<List<DailyEntry>>

    @Query("SELECT * FROM daily_entries WHERE year = :year AND month = :month AND day = :day")
    fun getEntryForDay(year: Int, month: Int, day: Int): Flow<DailyEntry?>

    @Query("""
        SELECT COUNT(*) FROM daily_entries 
        WHERE year = :year 
        AND note IS NOT NULL AND TRIM(note) != ''
        AND id IN (SELECT DISTINCT dailyEntryId FROM daily_todos)
    """)
    fun getEntryCountForYear(year: Int): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM daily_entries 
        WHERE year = :year AND month = :month 
        AND note IS NOT NULL AND TRIM(note) != ''
        AND id IN (SELECT DISTINCT dailyEntryId FROM daily_todos)
    """)
    fun getEntryCountForMonth(year: Int, month: Int): Flow<Int>

    @Delete
    fun deleteEntry(entry: DailyEntry)
}

@Dao
interface DailyTodoDao {
    @Upsert
    fun upsertTodo(todo: DailyTodo)

    @Query("SELECT * FROM daily_todos WHERE dailyEntryId = :dailyEntryId")
    fun getTodosForEntry(dailyEntryId: Int): Flow<List<DailyTodo>>

    @Update
    fun updateTodo(todo: DailyTodo)

    @Delete
    fun deleteTodo(todo: DailyTodo)

    @Query("DELETE FROM daily_todos WHERE dailyEntryId = :dailyEntryId")
    fun deleteTodosForEntry(dailyEntryId: Int)
}

@Dao
interface MonthlyReviewDao {
    @Upsert
    fun upsertReview(review: MonthlyReview)

    @Query("SELECT * FROM monthly_reviews WHERE year = :year AND month = :month")
    fun getReviewForMonth(year: Int, month: Int): Flow<MonthlyReview?>
    
    @Update
    fun updateReview(review: MonthlyReview)
}
