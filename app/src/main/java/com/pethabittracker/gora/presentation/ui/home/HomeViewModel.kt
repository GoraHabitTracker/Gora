package com.pethabittracker.gora.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.data.usecase.UpdateHabitPriorityUseCase
import com.pethabittracker.gora.domain.models.CalendarData
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
    private val updateHabitPriorityUseCase: UpdateHabitPriorityUseCase
) : ViewModel() {

    private val today = LocalDate.now()
    private val _allHabitFlow = MutableStateFlow(emptyList<Habit>())
    private val allHabitFlow: Flow<List<Habit>> = _allHabitFlow.asStateFlow()

    private val _listCalendarDataFlow = MutableStateFlow(emptyList<CalendarData>())
    private val listCalendarDataFlow: Flow<List<CalendarData>> = _listCalendarDataFlow.asStateFlow()

    private val _fulfilledHabitFlow = MutableStateFlow(false)
    private val fulfilledHabitFlow: Flow<Boolean> = _fulfilledHabitFlow.asStateFlow()

    init {
        viewModelScope.launch {
            calendarRepository.getFlowCalendarData().collectLatest {
                _listCalendarDataFlow.value = it
            }
            checkAllFulfilled()
        }
    }

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
                        }
                    }
                },
                onFailure = { emptyFlow() }
            )
    }

    private fun changeThePriority(habit: Habit, priority: Int) {
        flow<Unit> {
            updateHabit(habit, priority)
        }.launchIn(viewModelScope)
    }

    suspend fun deleteHabit(habit: Habit) = withContext(Dispatchers.IO) {
        listCalendarDataFlow
            .map { list ->
                val currentCalendarData = list.first { it.date == today }

                val date = currentCalendarData.date
                val namesHabitsFulfilled =
                    currentCalendarData.namesHabitsFulfilled.minus(habit.name)
                val namesAllHabits = currentCalendarData.namesAllHabits.minus(habit.name)
                val newCalendarData = CalendarData(
                    date = date,
                    namesHabitsFulfilled = namesHabitsFulfilled,
                    namesAllHabits = namesAllHabits,
                    areAllFulfilled = currentCalendarData.areAllFulfilled
                )
                calendarRepository.updateCalendarData(newCalendarData)
                habitRepository.deleteHabits(habit)
            }.launchIn(viewModelScope)
    }

    private suspend fun updateHabit(habit: Habit, priority: Int) = withContext(Dispatchers.IO) {
        runCatching {
            updateHabitPriorityUseCase.invoke(habit, priority)
        }
    }

    private suspend fun priorityCurrentDay(habit: Habit) {

        when (habit.priority) {
            Priority.Default.value -> {
                if (!habit.filterCurrentDay()) {
                    updateHabit(habit, Priority.Inactive.value)
                }
            }
            Priority.Done.value -> {
                if (!habit.filterCurrentDay()) {
                    updateHabit(habit, Priority.Inactive.value)
                }
            }
            Priority.Skip.value -> {
                if (!habit.filterCurrentDay()) {
                    updateHabit(habit, Priority.Inactive.value)
                }
            }
            Priority.Inactive.value -> {
                if (habit.filterCurrentDay()) {
                    updateHabit(habit, Priority.Default.value)
                }
            }
        }
    }

    fun onDoneClicked(habit: Habit) {
        listCalendarDataFlow
            .map { listCalendar ->
                // обновляем приоритет текущей привычки
                changeThePriority(habit, Priority.Done.value)

                // добавляем привычку в выполненные и проверяем, все ли выполнены на сегодня
                val calendarDateOld = listCalendar.first { it.date == today }
                val calendarData = CalendarData(
                    date = calendarDateOld.date,
                    namesHabitsFulfilled = calendarDateOld.namesHabitsFulfilled.plus(habit.name),
                    namesAllHabits = calendarDateOld.namesAllHabits,
                    areAllFulfilled = _fulfilledHabitFlow.value
                )

                calendarRepository.updateCalendarData(calendarData)
            }
            .launchIn(viewModelScope)
    }

    private fun checkAllFulfilled(): Boolean {
        return fulfilledHabitFlow
            .map {
                habitRepository.getAllHabits().none { habit ->
                    // привычка не имеет ни Priority.Default ни Priority.Skip
                    habit.priority != Priority.Default.value && habit.priority != Priority.Skip.value
                }
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                false
            ).value
    }

    fun onSkipClicked(habit: Habit) {
        listCalendarDataFlow
            .map {
                changeThePriority(habit, Priority.Skip.value)
            }
            .launchIn(viewModelScope)
    }
}
