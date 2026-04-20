package com.example.yournal.ui.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yournal.data.YournalRepository
import com.example.yournal.data.DailyEntry
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class DailyEntriesViewModel(private val repository: YournalRepository) : ViewModel() {
    
    fun getDaysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun getEntriesForMonth(year: Int, month: Int): Flow<List<DailyEntry>> {
        return repository.getEntriesForMonth(year, month)
    }

    fun getTodosForEntry(entryId: Int) = repository.getTodosForEntry(entryId)
}

class DailyEntriesViewModelFactory(private val repository: YournalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DailyEntriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DailyEntriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
