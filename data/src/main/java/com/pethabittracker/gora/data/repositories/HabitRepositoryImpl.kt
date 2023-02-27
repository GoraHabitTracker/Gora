package com.pethabittracker.gora.data.repositories

import com.pethabittracker.gora.data.database.HabitDao
import com.pethabittracker.gora.data.mapper.toData
import com.pethabittracker.gora.data.mapper.toDomain
import com.pethabittracker.gora.data.mapper.toDomainModels
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.models.HabitId
import com.pethabittracker.gora.domain.repositories.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class HabitRepositoryImpl(private val habitDao: HabitDao) : HabitRepository {

    override suspend fun getHabits(id: HabitId): Habit = withContext(Dispatchers.IO) {
        habitDao.getById(id.toDomain()).toDomain()
    }

    override suspend fun insertHabits(habit: Habit) = withContext(Dispatchers.IO) {
        habitDao.insert(habit.toData())
    }

    override suspend fun deleteHabits(habit: Habit) = withContext(Dispatchers.IO) {
        habitDao.deleteHabit(habit.toData())
    }

    override suspend fun getAllHabits(): List<Habit> = withContext(Dispatchers.IO) {
        habitDao.getHabitEntityList().map { it.toDomain() }
    }

    override  fun getFlowAllHabits(): Flow<List<Habit>> {
        return habitDao.getFlowHabitEntityList().map { it.toDomainModels() }
    }

    override suspend fun updateHabit(updatedHabit: Habit) {
        habitDao.update(updatedHabit.toData())
    }
}
