package com.pethabittracker.gora.presentation.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.domain.models.CalendarData
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import com.pethabittracker.gora.presentation.models.Priority
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    private val calendarRepository: CalendarDataRepository
) : ViewModel() {

    private val _listCalendarDataFlow = MutableStateFlow(emptyList<CalendarData>())
    private val listCalendarDataFlow: Flow<List<CalendarData>> = _listCalendarDataFlow.asStateFlow()

    suspend fun getCalendarDataFlow(): Flow<List<CalendarData>> {
        return listCalendarDataFlow
            .runCatching {
                calendarRepository.getFlowCalendarData()
            }
            .fold(
                onSuccess = { it },
                onFailure = { emptyFlow() }
            )
    }

    suspend fun getDoneCalendarDataFlow(): Flow<List<LocalDate>> {
        return listCalendarDataFlow
            .runCatching {
                calendarRepository.getFlowCalendarData()
                    .map { list-> list.map { it.date } }
            }
            .fold(
                onSuccess = { it },
                onFailure = { emptyFlow() }
            )
    }

//    private val _calendarData = MutableStateFlow(emptyList<CalendarData>())
//    private val calendarData: Flow<List<CalendarData>> = _calendarData.asStateFlow()
//
//    private val _datesWithFulfilledHabitFlow = MutableStateFlow(emptyList<LocalDate>())
//    private val datesWithFulfilledHabitFlow: Flow<List<LocalDate>> =
//        _datesWithFulfilledHabitFlow.asStateFlow()
//
//    private val _datesWithUnfulfilledHabitFlow = MutableStateFlow(emptyList<LocalDate>())
//    private val datesWithUnfulfilledHabitFlow: Flow<List<LocalDate>> =
//        _datesWithUnfulfilledHabitFlow.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//            calendarRepository.getFlowCalendarData().collectLatest {
//                _calendarData.value = it
//                fillListDate()
//            }
//        }
//    }
//
//    private fun fillListDate() {
//        calendarData
//            .onEach { list ->
//                val dateWithFulfilledHabit: MutableSet<LocalDate> = mutableSetOf()
//                val dateWithUnfulfilledHabit: MutableSet<LocalDate> = mutableSetOf()
//                list.onEach { progressCalendar ->
//                    if (progressCalendar.areAllFulfilled) {
//                        dateWithFulfilledHabit.add(progressCalendar.date)
//                    } else {
//                        dateWithUnfulfilledHabit.add(progressCalendar.date)
//                    }
//                }
//                _datesWithFulfilledHabitFlow.value = dateWithFulfilledHabit.toList()
//                _datesWithUnfulfilledHabitFlow.value = dateWithUnfulfilledHabit.toList()
//            }.launchIn(viewModelScope)
//    }
//
//    fun getDateWithFulfilledHabitFlow(): List<LocalDate> {
//        return datesWithFulfilledHabitFlow
//            .onEach {
//                fillListDate()
//            }.stateIn(
//                viewModelScope,
//                SharingStarted.Eagerly,
//                emptyList()
//            ).value
//    }
//
//    fun getDateWithUnfulfilledHabitFlow(): List<LocalDate> {
//        return datesWithUnfulfilledHabitFlow
//            .onEach {
//                fillListDate()
//            }.stateIn(
//                viewModelScope,
//                SharingStarted.Eagerly,
//                emptyList()
//            ).value
//    }
}
