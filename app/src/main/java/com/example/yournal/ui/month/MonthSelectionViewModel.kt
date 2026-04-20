package com.example.yournal.ui.month

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yournal.data.YournalRepository
import kotlinx.coroutines.flow.Flow
import java.text.DateFormatSymbols

class MonthSelectionViewModel(private val repository: YournalRepository) : ViewModel() {
    val months: List<String> = DateFormatSymbols().months.toList()

    fun getEntryCountForMonth(year: Int, month: Int): Flow<Int> {
        return repository.getEntryCountForMonth(year, month)
    }
}

class MonthSelectionViewModelFactory(private val repository: YournalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthSelectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MonthSelectionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
