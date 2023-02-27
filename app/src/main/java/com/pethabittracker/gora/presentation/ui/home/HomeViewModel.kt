package com.pethabittracker.gora.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.data.usecase.NewCalendarDataUseCase
import com.pethabittracker.gora.data.usecase.UpdateCalendarDataUseCase
import com.pethabittracker.gora.data.usecase.UpdateHabitPriorityUseCase
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import com.pethabittracker.gora.domain.repositories.HabitRepository
import com.pethabittracker.gora.presentation.extensions.filterCurrentDay
import com.pethabittracker.gora.presentation.models.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HomeViewModel(
    private val calendarRepository: CalendarDataRepository,
    private val habitRepository: HabitRepository,
    private val updateHabitPriorityUseCase: UpdateHabitPriorityUseCase,
    private val newCalendarDataUseCase: NewCalendarDataUseCase,
    private val updateCalendarDataUseCase: UpdateCalendarDataUseCase,
) : ViewModel() {

    private val today = LocalDate.now()
    private val _allHabitFlow = MutableStateFlow(emptyList<Habit>())
    private val allHabitFlow: Flow<List<Habit>> = _allHabitFlow.asStateFlow()

   fun getAllHabitFlow(): Flow<List<Habit>> {

        return allHabitFlow
            .runCatching {
                habitRepository.getFlowAllHabits()
            }
            .fold(
                onSuccess = { flowListHabit ->
                    flowListHabit.map { list ->
                        list.onEach { habit ->
                            priorityCurrentDay(habit)
                            newCalendarDataUseCase.invoke(
                                habit,
                                habit.priority
                            )
                        }
                    }
                },
                onFailure = { emptyFlow() }
            )
    }


    suspend fun deleteHabit(habit: Habit) = withContext(Dispatchers.IO) {
        viewModelScope.launch {
            calendarRepository.getAllCalendarData().filter { it.name == habit.name }
                .onEach { calendarRepository.deleteCalendarData(it) }

            habitRepository.deleteHabits(habit)
        }
    }

    private suspend fun updateHabit(habit: Habit, priority: Int) = withContext(Dispatchers.IO) {
        runCatching {
            updateHabitPriorityUseCase.invoke(habit, priority)
        }
    }

    private suspend fun priorityCurrentDay(habit: Habit) {

        val currentClicked = calendarRepository.findCurrentCalendarData(
            habit.name,
            today.toString()
        )?.clicked ?: false

        when (habit.priority) {

            Priority.Default.value -> {
                if (!habit.filterCurrentDay()) {
                    updateHabit(habit, Priority.Inactive.value)
                    updateCalendarDataUseCase.invoke(habit, Priority.Inactive.value, currentClicked)
                }
            }
            Priority.Done.value -> {
                if (!habit.filterCurrentDay()) {
                    updateHabit(habit, Priority.Inactive.value)
                    updateCalendarDataUseCase.invoke(habit, Priority.Inactive.value, currentClicked)
                }
            }
            Priority.Skip.value -> {
                if (!habit.filterCurrentDay()) {
                    updateHabit(habit, Priority.Inactive.value)
                    updateCalendarDataUseCase.invoke(habit, Priority.Inactive.value, currentClicked)
                }
            }
            Priority.Inactive.value -> {
                if (habit.filterCurrentDay()) {
                    updateHabit(habit, Priority.Default.value)
                    updateCalendarDataUseCase.invoke(habit, Priority.Default.value, currentClicked)
                }
            }
        }
    }

    fun onDoneClicked(habit: Habit) {

        viewModelScope.launch {
            updateHabit(habit, Priority.Done.value)

            updateCalendarDataUseCase.invoke(habit, Priority.Done.value, true)
        }
    }

    fun onSkipClicked(habit: Habit) {
        viewModelScope.launch {
            updateHabit(habit, Priority.Skip.value)

            updateCalendarDataUseCase.invoke(habit, Priority.Skip.value, true)
        }
    }
}
