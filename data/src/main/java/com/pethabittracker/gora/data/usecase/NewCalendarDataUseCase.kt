package com.pethabittracker.gora.data.usecase

import com.pethabittracker.gora.data.mapper.toDomain
import com.pethabittracker.gora.data.models.CalendarDataEntity
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import java.time.LocalDate

class NewCalendarDataUseCase(
    private val repository: CalendarDataRepository
) {

    suspend operator fun invoke(habit: Habit, priority: Int) {

        val today = LocalDate.now().toString()

        val currentCalendarData =
            repository.findCurrentCalendarData(habit.name, today)

        val newCalendarData = CalendarDataEntity(
            name = habit.name,
            date = today,
            state = priority
        ).toDomain()

        if (currentCalendarData!=newCalendarData){
            repository.insertCalendarData(newCalendarData)
        }


    }
}
