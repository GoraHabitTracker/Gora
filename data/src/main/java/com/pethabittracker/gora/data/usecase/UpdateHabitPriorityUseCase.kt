package com.pethabittracker.gora.data.usecase

import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.repositories.HabitRepository

class UpdateHabitPriorityUseCase(private val repository: HabitRepository) {

    suspend operator fun invoke(habit: Habit, priority: Int) {

        val updatedHabit = Habit(
            id = habit.id,
            name = habit.name,
            urlImage = habit.urlImage,
            priority = priority,
            repeatDays = habit.repeatDays
        )

        repository.updateHabit(updatedHabit)
    }
}