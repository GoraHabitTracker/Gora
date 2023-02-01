package com.pethabittracker.gora.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.data.utils.getCurrentDayOfWeek
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.repositories.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val repository: HabitRepository,
) : ViewModel() {

    private val _allHabitFlow = MutableStateFlow(emptyList<Habit>())
    private val allHabitFlow: Flow<List<Habit>> = _allHabitFlow.asStateFlow()

    fun checkSingleIdOne(): Boolean {
        return allHabitFlow
            .map { allHabits ->
                val thereIsIdEqualOne = allHabits.filter { it.id.id == 1 }.isNotEmpty()
                allHabits.size == theOnlyHabit && thereIsIdEqualOne
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                false
            ).value
    }

    //------------------ with Coroutine -------------------------------------------------------
    fun getAllHabitFlow(): Flow<List<Habit>> {

        return allHabitFlow
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
            else -> habit.repeatDays.sunday
        }
    }

    companion object {
        const val theOnlyHabit = 1
    }
}
