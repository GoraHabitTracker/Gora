package com.pethabittracker.gora.domain.repositories

import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.models.HabitId
import kotlinx.coroutines.flow.Flow


interface HabitRepository {

    suspend fun getHabits(id: HabitId): Habit

    suspend fun insertHabits(habit: Habit)

    suspend fun deleteHabits(habit: Habit)

    suspend fun getAllHabits(): List<Habit>

    fun getFlowAllHabits(): Flow<List<Habit>>

    suspend fun updateHabit(updatedHabit: Habit)
}
