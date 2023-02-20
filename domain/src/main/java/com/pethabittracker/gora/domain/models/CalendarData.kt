package com.pethabittracker.gora.domain.models

import java.time.LocalDate

data class CalendarData(
    // пока что сохраняем только сделанные привычки
//    val date: LocalDate,
//    val namesHabitsFulfilled: Set<String>,
//    val namesAllHabits: Set<String>,
//    val areAllFulfilled: Boolean


    val id: Int,
    val name: String,
    val date: LocalDate,
    val state: Int
)
