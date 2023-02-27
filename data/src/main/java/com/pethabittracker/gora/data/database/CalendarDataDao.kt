package com.pethabittracker.gora.data.database

import androidx.room.*
import com.pethabittracker.gora.data.models.CalendarDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CalendarDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(calendarData: CalendarDataEntity)

    @Query("SELECT * FROM calendar_data WHERE date = :date")
    fun getByDate(date: String): CalendarDataEntity

    @Query("SELECT * FROM calendar_data")
    fun getCalendarDataEntityList(): List<CalendarDataEntity>

    @Query("SELECT * FROM calendar_data")
    fun getFlowCalendarDataEntityList(): Flow<List<CalendarDataEntity>>

    @Delete
    fun deleteCalendarData(calendarData: CalendarDataEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(calendarData: CalendarDataEntity)
}
