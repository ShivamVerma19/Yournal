package com.example.yournal.ui.month

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yournal.data.YournalRepository
import com.example.yournal.data.MonthlyGoal
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MonthlyGoalsViewModel(
    private val repository: YournalRepository,
    private val year: Int,
    private val month: Int
) : ViewModel() {

    val goals: StateFlow<List<MonthlyGoal>> = repository.getGoalsForMonth(year, month)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addGoal(goalText: String) {
        viewModelScope.launch {
            repository.upsertGoal(MonthlyGoal(year = year, month = month, goal = goalText))
        }
    }

    fun updateGoal(goal: MonthlyGoal) {
        viewModelScope.launch {
            repository.updateGoal(goal)
        }
    }

    fun deleteGoal(goal: MonthlyGoal) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }
}

class MonthlyGoalsViewModelFactory(
    private val repository: YournalRepository,
    private val year: Int,
    private val month: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthlyGoalsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MonthlyGoalsViewModel(repository, year, month) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
