package com.pethabittracker.gora.data.usecase

import com.pethabittracker.gora.data.mapper.toData
import com.pethabittracker.gora.data.mapper.toDomain
import com.pethabittracker.gora.data.models.HabitEntity
import com.pethabittracker.gora.domain.models.CalendarData
import com.pethabittracker.gora.domain.models.WeekList
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import com.pethabittracker.gora.domain.repositories.HabitRepository
import java.time.LocalDate

class NewHabitUseCase(
    private val calendarRepository: CalendarDataRepository,
    private val habitRepository: HabitRepository
) {

    suspend operator fun invoke(name: String, url: Int, priority: Int, repeatDays: WeekList) {

        val today = LocalDate.now().toString()
        val newHabit = HabitEntity(
            name = name,
            urlImage = url,
            priority = priority,
            repeatDays = repeatDays.toData(),
        ).toDomain()

        habitRepository.insertHabits(newHabit)

        // обновить список имен всех привычек
        val todayProgressCalendar = calendarRepository.getCalendarData(today)
        calendarRepository.insertCalendarData(
            CalendarData(
                date = todayProgressCalendar.date,
                namesHabitsFulfilled = todayProgressCalendar.namesHabitsFulfilled,
                namesAllHabits = todayProgressCalendar.namesAllHabits.plus(newHabit.name),
                areAllFulfilled = todayProgressCalendar.areAllFulfilled
            )
        )
    }
}