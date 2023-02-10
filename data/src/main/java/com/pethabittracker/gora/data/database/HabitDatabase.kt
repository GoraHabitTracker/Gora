package com.pethabittracker.gora.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pethabittracker.gora.data.models.CalendarDataEntity
import com.pethabittracker.gora.data.models.HabitEntity

@Database(entities = [HabitEntity::class, CalendarDataEntity::class], version = 1)
internal abstract class HabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    abstract fun calendarDataDao(): CalendarDataDao

}
