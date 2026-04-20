package com.example.yournal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        YearEntity::class,
        MonthlyGoal::class,
        DailyEntry::class,
        DailyTodo::class,
        MonthlyReview::class
    ],
    version = 1,
    exportSchema = false
)
abstract class YournalDatabase : RoomDatabase() {

    abstract fun yearDao(): YearDao
    abstract fun monthlyGoalDao(): MonthlyGoalDao
    abstract fun dailyEntryDao(): DailyEntryDao
    abstract fun dailyTodoDao(): DailyTodoDao
    abstract fun monthlyReviewDao(): MonthlyReviewDao

    companion object {
        @Volatile
        private var INSTANCE: YournalDatabase? = null

        fun getDatabase(context: Context): YournalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YournalDatabase::class.java,
                    "yournal_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
