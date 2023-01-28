package com.pethabittracker.gora.domain.models

data class Habit(
    val id: HabitId,
    val name: String,
    val urlImage: Int,
    val priority: Int
)
