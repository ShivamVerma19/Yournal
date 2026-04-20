package com.example.yournal.ui.year

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yournal.data.YournalRepository
import com.example.yournal.data.YearEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class YearSelectionViewModel(private val repository: YournalRepository) : ViewModel() {

    val years: StateFlow<List<YearEntity>> = repository.getAllYears()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getEntryCountForYear(year: Int) = repository.getEntryCountForYear(year)

    fun addYear(year: Int) {
        viewModelScope.launch {
            repository.insertYear(YearEntity(year))
        }
    }

    fun deleteYear(year: YearEntity) {
        viewModelScope.launch {
            repository.deleteYear(year)
        }
    }
}

class YearSelectionViewModelFactory(private val repository: YournalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(YearSelectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return YearSelectionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
