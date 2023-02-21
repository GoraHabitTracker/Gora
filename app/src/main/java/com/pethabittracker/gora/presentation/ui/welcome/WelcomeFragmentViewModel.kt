package com.pethabittracker.gora.presentation.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.data.usecase.UpdateHabitPriorityUseCase
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import com.pethabittracker.gora.domain.repositories.HabitRepository
import com.pethabittracker.gora.presentation.models.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class WelcomeFragmentViewModel(
    private val calendarRepository: CalendarDataRepository,
    private val repositoryHabit: HabitRepository,
    private val updateHabitPriorityUseCase: UpdateHabitPriorityUseCase,
) : ViewModel() {

    private val today = LocalDate.now().toString()
    fun updateDaily() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val allDateHabitList = calendarRepository.getAllCalendarData()

                val allHabits = repositoryHabit.getAllHabits()

                allHabits.filterNot {
                    val currentCalendarData = calendarRepository.findCurrentCalendarData(
                        it.name,
                        today
                    )
                    allDateHabitList.contains(currentCalendarData)&&currentCalendarData?.clicked==true
                }
                    .onEach {
                        updateHabitPriorityUseCase.invoke(it, Priority.Default.value)
                    }
            }
        }
    }
}
