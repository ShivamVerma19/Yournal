package com.example.yournal

import android.app.Application
import com.example.yournal.data.UserPreferencesRepository
import com.example.yournal.data.YournalDatabase
import com.example.yournal.data.YournalRepository

class YournalApplication : Application() {
    private val database by lazy { YournalDatabase.getDatabase(this) }
    val repository by lazy {
        YournalRepository(
            database.yearDao(),
            database.monthlyGoalDao(),
            database.dailyEntryDao(),
            database.dailyTodoDao(),
            database.monthlyReviewDao()
        )
    }
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(this)
    }
}
