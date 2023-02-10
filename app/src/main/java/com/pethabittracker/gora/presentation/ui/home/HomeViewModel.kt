package com.pethabittracker.gora.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.data.usecase.UpdateHabitPriorityUseCase
import com.pethabittracker.gora.data.utils.getCurrentDayOfWeek
import com.pethabittracker.gora.domain.models.CalendarData
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import com.pethabittracker.gora.domain.repositories.HabitRepository
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

    private val _listCalendarData = MutableStateFlow(emptyList<CalendarData>())
    private val listCalendarData: Flow<List<CalendarData>> = _listCalendarData.asStateFlow()

    init {
        viewModelScope.launch {
            calendarRepository.getFlowCalendarData().collectLatest {
                _listCalendarData.value = it
            }
        }
    }

    //------------------ with Coroutine -------------------------------------------------------
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

    //----------------- with LiveData -------------------------------------------------------------

    //  val allHabit: LiveData<List<Habit>> = repository.getFlowAllHabits().asLiveData()

    private fun changeThePriority(habit: Habit, priority: Int) {
        flow<Unit> {
            updateHabit(habit, priority)
        }.launchIn(viewModelScope)
    }

    suspend fun deleteHabit(habit: Habit) = withContext(Dispatchers.IO) {
        listCalendarData
            .map { list ->
                val currentCalendarData = list.first { it.date == today }

                val date = currentCalendarData.date
                val namesAllHabits = currentCalendarData.namesAllHabits.minus(habit.name)
                val namesHabitsFulfilled =
                    currentCalendarData.namesHabitsFulfilled.minus(habit.name)
                val newCalendarData = CalendarData(date, namesHabitsFulfilled, namesAllHabits)
                calendarRepository.updateCalendarData(newCalendarData)
                habitRepository.deleteHabits(habit)
            }.launchIn(viewModelScope)
    }

    private suspend fun updateHabit(habit: Habit, priority: Int) = withContext(Dispatchers.IO) {
        runCatching {
            updateHabitPriorityUseCase.invoke(habit, priority)
        }
    }

    private fun filterCurrentDay(habit: Habit): Boolean {
        return when (getCurrentDayOfWeek()) {
            "Monday" -> habit.repeatDays.monday
            "Thursday" -> habit.repeatDays.thursday
            "Wednesday" -> habit.repeatDays.wednesday
            "Tuesday" -> habit.repeatDays.tuesday
            "Friday" -> habit.repeatDays.friday
            "Saturday" -> habit.repeatDays.saturday
            else -> habit.repeatDays.sunday
        }
    }

    private suspend fun priorityCurrentDay(habit: Habit) {

        when (habit.priority) {
            Priority.Default.value -> {
                if (!filterCurrentDay(habit)) {
                    updateHabit(habit, Priority.Inactive.value)
                }
            }
            Priority.Done.value -> {
                if (!filterCurrentDay(habit)) {
                    updateHabit(habit, Priority.Inactive.value)
                }
            }
            Priority.Skip.value -> {
                if (!filterCurrentDay(habit)) {
                    updateHabit(habit, Priority.Inactive.value)
                }
            }
            Priority.Inactive.value -> {
                if (filterCurrentDay(habit)) {
                    updateHabit(habit, Priority.Default.value)
                }
            }
        }
    }

    fun onDoneClicked(habit: Habit) {
        /*
         1) меняем приоритет у привычки
         2) добавляем в calendarProgress на сегодняшнюю дату текущую привычку в поле сделанные
         */
        listCalendarData
            .map { listProgrCal ->
                changeThePriority(habit, Priority.Done.value)

                val progressCalendarOld = listProgrCal.first { it.date == today }
                val date = progressCalendarOld.date
                val namesHabitsFulfilled = progressCalendarOld.namesHabitsFulfilled.plus(habit.name)
                val namesAllHabits = progressCalendarOld.namesAllHabits
                val calendarData = CalendarData(date, namesHabitsFulfilled, namesAllHabits)

                calendarRepository.updateCalendarData(calendarData)
            }
            .launchIn(viewModelScope)
    }

    fun onSkipClicked(habit: Habit) {
        listCalendarData
            .map {
                changeThePriority(habit, Priority.Skip.value)
            }
            .launchIn(viewModelScope)
    }
}
