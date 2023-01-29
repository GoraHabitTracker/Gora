package com.pethabittracker.gora.domain.repositories

import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.models.HabitId
import com.pethabittracker.gora.domain.models.WeekList
import kotlinx.coroutines.flow.Flow


interface HabitRepository {

    suspend fun getHabits(id: HabitId): Habit

    suspend fun insertHabits(habit: Habit)

    suspend fun deleteHabits(habit: Habit)

    suspend fun getAllHabits(): List<Habit>

    fun getFlowAllHabits(): Flow<List<Habit>>

    fun newHabit(name: String, url: Int, priority: Int, repeatDays: WeekList): Habit      // По-моему этот метод надо удалить из репозитория

    fun updateHabitPriority(habit: Habit, priority: Int)
}
