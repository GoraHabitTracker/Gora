package com.pethabittracker.gora.presentation.ui.newhabits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.data.usecase.NewHabitUseCase
import com.pethabittracker.gora.domain.models.WeekList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class NewHabitViewModel(
    private val newHabitUseCase: NewHabitUseCase
) : ViewModel() {

    private val _helperFlow = MutableStateFlow(0)
    private var isSetTitle = false
    private var isSetIcon = false
    private var isSetDays = false

    suspend fun newHabit(name: String, url: Int, priority: Int, repeatDays: WeekList) {
        // TODO добавить валидацию на уникальность имени привычек

        withContext(Dispatchers.IO) {
            newHabitUseCase.invoke(name, url, priority, repeatDays)
        }
    }

    val isAllowedSave = _helperFlow
        .map {
            isSetTitle && isSetDays && isSetIcon
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            false
        )

    fun onChangeTitle(value: Boolean) {
        isSetTitle = value
        _helperFlow.value++
    }

    fun onChangeIcon() {
        isSetIcon = true
        _helperFlow.value++
    }

    fun onChangeDays(weekList: WeekList) {
        isSetDays = weekList.monday || weekList.thursday || weekList.wednesday ||
                weekList.tuesday || weekList.friday || weekList.saturday || weekList.sunday
        _helperFlow.value++
    }
}
