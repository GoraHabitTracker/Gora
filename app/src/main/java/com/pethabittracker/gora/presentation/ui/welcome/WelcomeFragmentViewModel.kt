package com.pethabittracker.gora.presentation.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.data.usecase.UpdateHabitPriorityUseCase
import com.pethabittracker.gora.domain.models.CalendarData
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import com.pethabittracker.gora.domain.repositories.HabitRepository
import com.pethabittracker.gora.presentation.extensions.filterCurrentDay
import com.pethabittracker.gora.presentation.models.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class WelcomeFragmentViewModel(
    private val repositoryCalendar: CalendarDataRepository,
    private val repositoryHabit: HabitRepository,
    private val updateHabitPriorityUseCase: UpdateHabitPriorityUseCase
) : ViewModel() {

    fun updateDaily() {

        viewModelScope.launch {
            val allHabits = repositoryHabit.getAllHabits()

//            val allNamesOfHabits: Set<String> =
//                allHabits.map { it.name }.toSet()
//            val areAllFulfilled = allHabits.filter { habit ->
//                // привычка не имеет ни Priority.Default ни Priority.Skip
//                habit.priority != Priority.Default.value && habit.priority != Priority.Skip.value
//            }.isEmpty()

            // создаем CalendarData на сегодня, если онa еще не созданa
//            runCatching {
//                repositoryCalendar.getCalendarData(LocalDate.now().toString())
//            }.onFailure {
//                repositoryCalendar.insertCalendarData(
//                    CalendarData(
//                        date = LocalDate.now(),
//                        namesAllHabits = allNamesOfHabits,
//                        namesHabitsFulfilled = emptySet(),
//                        areAllFulfilled = areAllFulfilled
//                    )
//                )
                // обновляем приоритеты хобитам

                withContext(Dispatchers.IO) {
                    allHabits.filter {
                        it.filterCurrentDay()
                    }.onEach {
                        updateHabitPriorityUseCase.invoke(it, Priority.Default.value)
                    }
                }
            }
        }
    }
//}
