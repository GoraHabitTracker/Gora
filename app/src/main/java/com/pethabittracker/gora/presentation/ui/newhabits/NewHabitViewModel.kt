package com.pethabittracker.gora.presentation.ui.newhabits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.models.WeekList
import com.pethabittracker.gora.domain.repositories.HabitRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewHabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    private var _allHabitFlow = MutableStateFlow(emptyList<Habit>())
     val allHabitFlow: Flow<List<Habit>> = _allHabitFlow.asStateFlow()

    init {
        viewModelScope.launch {
//            _allHabitFlow.value = repository.getAllHabits()     // не работает! не присваивает значение
            repository.getFlowAllHabits().collectLatest {
                _allHabitFlow.value = it
            }
        }
    }

    fun checkSingleIdOne(): Flow<Boolean> {

        return allHabitFlow
            .map { allHabits ->
                val thereIsIdEqualOne = allHabits.filter { it.id.id == 1 }.isNotEmpty()
                allHabits.size == theOnlyHabit && thereIsIdEqualOne
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                false
            )
    }

    //--------------------------------------------------------------------------------------------
    fun newHabit(name: String, url: Int, priority: Int, repeatDays: WeekList) =
        // скорее всего newHabit должен создаваться тут или в каком-нибудь UserCase
        repository.newHabit(name, url, priority, repeatDays)

    suspend fun insertHabit(habit: Habit) =
        repository.insertHabits(habit)

    companion object {
        const val theOnlyHabit = 1
    }
}
