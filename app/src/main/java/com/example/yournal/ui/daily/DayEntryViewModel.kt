package com.example.yournal.ui.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yournal.data.YournalRepository
import com.example.yournal.data.DailyEntry
import com.example.yournal.data.DailyTodo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class DayEntryViewModel(
    private val repository: YournalRepository,
    private val year: Int,
    private val month: Int,
    private val day: Int
) : ViewModel() {

    private val _dailyEntry = MutableStateFlow<DailyEntry?>(null)

    val dailyEntry: StateFlow<DailyEntry?> = repository.getEntryForDay(year, month, day)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val todos: StateFlow<List<DailyTodo>> = dailyEntry.flatMapLatest { entry ->
        entry?.let {
            repository.getTodosForEntry(it.id)
        } ?: MutableStateFlow(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getEntryId(callback: (Int) -> Unit) {
        viewModelScope.launch {
            val entry = dailyEntry.value
            if (entry != null) {
                callback(entry.id)
            } else {
                val newEntryId = repository.upsertEntry(DailyEntry(year = year, month = month, day = day))
                callback(newEntryId.toInt())
            }
        }
    }

    fun upsertNote(note: String) {
        viewModelScope.launch {
            val entry = dailyEntry.value
            if (note.isBlank() && todos.value.isEmpty()) {
                entry?.let { repository.deleteEntry(it) }
            } else {
                if (entry != null) {
                    repository.upsertEntry(entry.copy(note = note))
                } else {
                    repository.upsertEntry(DailyEntry(year = year, month = month, day = day, note = note))
                }
            }
        }
    }

    fun addTodo(todoText: String) {
        getEntryId { entryId ->
            viewModelScope.launch {
                repository.upsertTodo(DailyTodo(dailyEntryId = entryId, todo = todoText))
            }
        }
    }

    fun updateTodo(todo: DailyTodo) {
        viewModelScope.launch {
            repository.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: DailyTodo) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
            val entry = dailyEntry.value
            // If this was the last todo and note is blank, delete entry
            if (entry != null && entry.note.isNullOrBlank() && todos.value.size <= 1) {
                repository.deleteEntry(entry)
            }
        }
    }
}

class DayEntryViewModelFactory(
    private val repository: YournalRepository,
    private val year: Int,
    private val month: Int,
    private val day: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DayEntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DayEntryViewModel(repository, year, month, day) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
