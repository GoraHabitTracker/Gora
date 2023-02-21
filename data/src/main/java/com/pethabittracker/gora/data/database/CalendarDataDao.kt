package com.pethabittracker.gora.data.database

import androidx.room.*
import com.pethabittracker.gora.data.models.CalendarDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CalendarDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(calendarData: CalendarDataEntity)

    @Query("SELECT * FROM CalendarDataEntity WHERE date = :date")
    fun getByDate(date: String): CalendarDataEntity

    @Query("SELECT * FROM CalendarDataEntity")
    fun getCalendarDataEntityList(): List<CalendarDataEntity>

    @Query("SELECT * FROM CalendarDataEntity")
    fun getFlowCalendarDataEntityList(): Flow<List<CalendarDataEntity>>

    @Delete
    fun deleteCalendarData(calendarData: CalendarDataEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(calendarData: CalendarDataEntity)

    @Query("SELECT * FROM CalendarDataEntity WHERE name LIKE :name AND " + "date LIKE :date")
    fun findCurrentCalendarData(name: String, date: String): CalendarDataEntity?
}
