package com.pethabittracker.gora.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.data.utils.getCurrentDayOfWeek
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.repositories.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.Locale.filter

class HomeViewModel(
    private val repository: HabitRepository,
) : ViewModel() {

    private val _dataFlow = MutableStateFlow(emptyList<Habit>())
    private val dataFlow: Flow<List<Habit>> = _dataFlow.asStateFlow()

    //------------------ with Coroutine -------------------------------------------------------
    fun getAllHabit(): Flow<List<Habit>> {

        return dataFlow    // работает, но что-то здесь не то
            .runCatching {
                repository.getFlowAllHabits()
            }
            .fold(
                onSuccess = { flowListHabit ->
                    flowListHabit.map { list ->
                        list.filter { habit ->
                            filterCurrentDay(habit)
                        }
                    }
                },
                onFailure = { emptyFlow() }
            )
    }

    //----------------- with LiveData -------------------------------------------------------------

    //  val allHabit: LiveData<List<Habit>> = repository.getFlowAllHabits().asLiveData()

    fun skipDown(habit: Habit, priority: Int) {
        flow<Unit> {
            updateHabit(habit, priority)
        }.launchIn(viewModelScope)
    }

    suspend fun deleteHabit(habit: Habit) = withContext(Dispatchers.IO) {
        repository.deleteHabits(habit)
    }

    private suspend fun updateHabit(habit: Habit, priority: Int) = withContext(Dispatchers.IO) {
        runCatching {
            repository.updateHabitPriority(habit, priority)
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
           else -> {habit.repeatDays.sunday}
       }
    }
}
