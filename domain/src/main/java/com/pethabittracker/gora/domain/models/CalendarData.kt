package com.pethabittracker.gora.domain.models

import java.time.LocalDate

data class CalendarData(
    // пока что сохраняем только сделанные привычки
    val date: LocalDate,
    val namesHabitsFulfilled: Set<String>,
    val namesAllHabits: Set<String>
)
