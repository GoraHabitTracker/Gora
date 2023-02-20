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

    suspend operator fun invoke(habit: Habit, priority: Int) {

        val today = LocalDate.now()

        val currentCalendarData =
            repository.findCurrentCalendarData(habit.name, today.toString())

        val updateCalendarData = CalendarData(
            id = currentCalendarData.id,
            name = currentCalendarData.name,
            date = today,
            state = priority
        )

        repository.updateCalendarData(updateCalendarData)
    }
}
