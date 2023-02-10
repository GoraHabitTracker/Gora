package com.pethabittracker.gora.presentation.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.domain.models.CalendarData
import com.pethabittracker.gora.domain.repositories.HabitRepository
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class WelcomeFragmentViewModel(
    private val repositoryCalendar: CalendarDataRepository,
    private val repositoryHabit: HabitRepository
) : ViewModel() {

    fun updateCalendarData() {

        viewModelScope.launch {
            val allNamesOfHabits: Set<String> =
                repositoryHabit.getAllHabits().map { it.name }.toSet()

            // создаем CalendarData на сегодня, если онa еще не созданa
            runCatching {
                repositoryCalendar.getCalendarData(LocalDate.now().toString())
            }.onFailure {
                repositoryCalendar.insertCalendarData(
                    CalendarData(
                        date = LocalDate.now(),
                        namesAllHabits = allNamesOfHabits,
                        namesHabitsFulfilled = emptySet()
                    )
                )
            }
        }
    }
}
