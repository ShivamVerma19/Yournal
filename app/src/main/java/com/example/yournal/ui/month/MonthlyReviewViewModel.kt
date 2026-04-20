package com.example.yournal.ui.month

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yournal.data.YournalRepository
import com.example.yournal.data.MonthlyReview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MonthlyReviewViewModel(
    private val repository: YournalRepository,
    private val year: Int,
    private val month: Int
) : ViewModel() {

    val review: StateFlow<MonthlyReview?> = repository.getReviewForMonth(year, month)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun upsertReview(
        whatWentWell: String,
        whatDidntGoWell: String,
        lessonsLearned: String
    ) {
        viewModelScope.launch {
            val currentReview = review.value
            if (currentReview != null) {
                repository.upsertReview(
                    currentReview.copy(
                        whatWentWell = whatWentWell,
                        whatDidntGoWell = whatDidntGoWell,
                        lessonsLearned = lessonsLearned
                    )
                )
            } else {
                repository.upsertReview(
                    MonthlyReview(
                        year = year,
                        month = month,
                        whatWentWell = whatWentWell,
                        whatDidntGoWell = whatDidntGoWell,
                        lessonsLearned = lessonsLearned
                    )
                )
            }
        }
    }
}

class MonthlyReviewViewModelFactory(
    private val repository: YournalRepository,
    private val year: Int,
    private val month: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthlyReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MonthlyReviewViewModel(repository, year, month) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
