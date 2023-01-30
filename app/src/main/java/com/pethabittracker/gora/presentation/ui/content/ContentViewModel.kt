package com.pethabittracker.gora.presentation.ui.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.domain.repositories.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ContentViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    private var _countHabitFlow = MutableStateFlow(0)
    val countHabitFlow: Flow<Int> = _countHabitFlow.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFlowAllHabits().map {
                it.size
            }.collect {
                _countHabitFlow.value = it
            }
        }
    }
}

