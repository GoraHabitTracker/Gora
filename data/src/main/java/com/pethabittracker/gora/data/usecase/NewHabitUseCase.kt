package com.pethabittracker.gora.data.usecase

import com.pethabittracker.gora.data.mapper.toData
import com.pethabittracker.gora.data.mapper.toDomain
import com.pethabittracker.gora.data.models.HabitEntity
import com.pethabittracker.gora.domain.models.WeekList
import com.pethabittracker.gora.domain.repositories.HabitRepository

class NewHabitUseCase(private val repository: HabitRepository) {

    suspend operator fun invoke(name: String, url: Int, priority: Int, repeatDays: WeekList) {

        val newHabit = HabitEntity(
            name = name,
            urlImage = url,
            priority = priority,
            repeatDays = repeatDays.toData(),
        ).toDomain()

        repository.insertHabits(newHabit)
    }
}