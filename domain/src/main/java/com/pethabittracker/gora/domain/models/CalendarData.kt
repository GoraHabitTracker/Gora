package com.pethabittracker.gora.domain.models

import java.time.LocalDate

data class CalendarData(
    val id: Int,
    val name: String,
    val date: LocalDate,
    val state: Int,
    val clicked: Boolean
)
