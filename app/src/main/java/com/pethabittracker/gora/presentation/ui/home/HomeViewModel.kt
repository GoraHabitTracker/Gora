package com.pethabittracker.gora.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.data.usecase.UpdateHabitPriorityUseCase
import com.pethabittracker.gora.data.utils.getCurrentDayOfWeek
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.repositories.HabitRepository
import com.pethabittracker.gora.presentation.models.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val repository: HabitRepository,
    private val updateHabitPriorityUseCase: UpdateHabitPriorityUseCase
) : ViewModel() {

    private val _allHabitFlow = MutableStateFlow(emptyList<Habit>())
    private val allHabitFlow: Flow<List<Habit>> = _allHabitFlow.asStateFlow()

    //------------------ with Coroutine -------------------------------------------------------
    fun getAllHabitFlow(): Flow<List<Habit>> {

        return allHabitFlow
            .runCatching {
                repository.getFlowAllHabits()
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

    fun changeThePriority(habit: Habit, priority: Int) {
        flow<Unit> {
            updateHabit(habit, priority)
        }.launchIn(viewModelScope)
    }

    suspend fun deleteHabit(habit: Habit) = withContext(Dispatchers.IO) {
        repository.deleteHabits(habit)
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
}
