package com.pethabittracker.gora.presentation.ui.newhabits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.data.usecase.NewHabitUseCase
import com.pethabittracker.gora.domain.models.CalendarData
import com.pethabittracker.gora.domain.models.WeekList
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import com.pethabittracker.gora.domain.repositories.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class NewHabitViewModel(
    private val calendarRepository: CalendarDataRepository,
    private val habitRepository: HabitRepository,
    private val newHabitUseCase: NewHabitUseCase
) : ViewModel() {

    private var _allNameOfHabitsFlow = MutableStateFlow(setOf<String>())
    private val allNameOfHabitsFlow: Flow<Set<String>> = _allNameOfHabitsFlow.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {   // без этой строчки запрос не выполняется
                habitRepository.getFlowAllHabits()
                    .collectLatest { habits ->
                        _allNameOfHabitsFlow.value = habits.map { it.name }.toSet()
                    }
            }
        }
    }

    suspend fun newHabit(name: String, url: Int, priority: Int, repeatDays: WeekList) {
        // TODO добавить валидацию на уникальность имени привычек
        /* если убрать newHabitUseCase() из withContext(Dispatchers.IO) {},
        то всё остальное в withContext(Dispatchers.IO){} работать не будет */

        withContext(Dispatchers.IO) {
            newHabitUseCase.invoke(name, url, priority, repeatDays)
            changeListOfNamesAllHabits()
        }
    }

    private fun changeListOfNamesAllHabits() {

        allNameOfHabitsFlow
            .onEach {
                val today = LocalDate.now().toString()
//                val progressCalendarToday = calendarRepository.getCalendarData(today)
//                calendarRepository.insertCalendarData(
//                    CalendarData(
//                        date = progressCalendarToday.date,
//                        namesHabitsFulfilled = progressCalendarToday.namesHabitsFulfilled,
//                        namesAllHabits = it,
//                        areAllFulfilled = progressCalendarToday.areAllFulfilled
//                    )
//                )
            }
    }
}
