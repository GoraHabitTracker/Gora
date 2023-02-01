package com.pethabittracker.gora.presentation.ui.newhabits

import androidx.lifecycle.ViewModel
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.models.WeekList
import com.pethabittracker.gora.domain.repositories.HabitRepository

class NewHabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    fun newHabit(name: String, url: Int, priority: Int, repeatDays: WeekList) =
        // скорее всего newHabit должен создаваться тут или в каком-нибудь UserCase
        repository.newHabit(name, url, priority, repeatDays)

    suspend fun insertHabit(habit: Habit) =
        repository.insertHabits(habit)
}
