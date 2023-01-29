package com.pethabittracker.gora.presentation.ui.newhabits

import androidx.lifecycle.ViewModel
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.models.WeekList
import com.pethabittracker.gora.domain.repositories.HabitRepository

class NewHabitViewModel(private val repository: HabitRepository) : ViewModel() {

    fun newHabit(name: String, url: String, priority: Int, repeatDays: WeekList) =
        repository.newHabit(name, url, priority, repeatDays)

    suspend fun insertHabit(habit: Habit) =
        repository.insertHabits(habit)
}
