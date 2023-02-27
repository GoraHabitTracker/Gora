package com.pethabittracker.gora.data.usecase

import com.pethabittracker.gora.data.mapper.toDomain
import com.pethabittracker.gora.data.models.CalendarDataEntity
import com.pethabittracker.gora.domain.models.CalendarData
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import com.pethabittracker.gora.domain.repositories.HabitRepository
import java.time.LocalDate

class UpdateCalendarDataUseCase(
    private val repository: CalendarDataRepository
) {

    suspend operator fun invoke(habit: Habit, priority: Int, clicked: Boolean) {

        val today = LocalDate.now()

        if (repository.getAllCalendarData().isNotEmpty()) {
            val currentCalendarData =
                repository.findCurrentCalendarData(habit.name, today.toString())
            if (currentCalendarData != null) {
                val updateCalendarData = CalendarData(
                    id = currentCalendarData.id,
                    name = currentCalendarData.name,
                    date = today,
                    state = priority,
                    clicked = clicked
                )

                repository.updateCalendarData(updateCalendarData)
            }

        }

    }
}