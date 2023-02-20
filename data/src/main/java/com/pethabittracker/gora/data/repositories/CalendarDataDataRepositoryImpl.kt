package com.pethabittracker.gora.data.repositories

import com.pethabittracker.gora.data.database.CalendarDataDao
import com.pethabittracker.gora.data.mapper.toData
import com.pethabittracker.gora.data.mapper.toDomain
import com.pethabittracker.gora.data.mapper.toDomainModels
import com.pethabittracker.gora.domain.models.CalendarData
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class CalendarDataDataRepositoryImpl(
    private val dao: CalendarDataDao
) : CalendarDataRepository {

    override suspend fun insertCalendarData(calendarData: CalendarData) {
        withContext(Dispatchers.IO) { dao.insert(calendarData.toData()) }
    }

    override suspend fun getCalendarData(date: String): CalendarData {
        return withContext(Dispatchers.IO) { dao.getByDate(date).toDomain() }
    }

    override suspend fun deleteCalendarData(calendarData: CalendarData) {
        return withContext(Dispatchers.IO) { dao.deleteCalendarData(calendarData.toData()) }
    }

    override suspend fun getAllCalendarData(): List<CalendarData> {
        return withContext(Dispatchers.IO) { dao.getCalendarDataEntityList().toDomainModels() }
    }

    override suspend fun getFlowCalendarData(): Flow<List<CalendarData>> {
        return withContext(Dispatchers.IO) {
            dao.getFlowCalendarDataEntityList().map { it.toDomainModels() }
        }
    }

    override suspend fun updateCalendarData(updatedCalendarData: CalendarData) {
        withContext(Dispatchers.IO) { dao.update(updatedCalendarData.toData()) }
    }

    override suspend fun findCurrentCalendarData(name: String, date: String): CalendarData {
        return  withContext(Dispatchers.IO) { dao.findCurrentCalendarData(name, date).toDomain() }
    }
}
