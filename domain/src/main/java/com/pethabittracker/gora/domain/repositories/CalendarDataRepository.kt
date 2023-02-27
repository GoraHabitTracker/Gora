package com.pethabittracker.gora.domain.repositories

import com.pethabittracker.gora.domain.models.CalendarData
import kotlinx.coroutines.flow.Flow

interface CalendarDataRepository {

    suspend fun insertCalendarData(calendarData: CalendarData)

    suspend fun getCalendarData(date: String): CalendarData

    suspend fun deleteCalendarData(calendarData: CalendarData)

    suspend fun getAllCalendarData(): List<CalendarData>

   fun getFlowCalendarData(): Flow<List<CalendarData>>

    suspend fun updateCalendarData(updatedCalendarData: CalendarData)

    suspend fun findCurrentCalendarData(name: String, date: String): CalendarData?
}
