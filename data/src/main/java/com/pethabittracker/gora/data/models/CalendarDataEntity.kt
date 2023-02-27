package com.pethabittracker.gora.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_data")
internal data class CalendarDataEntity(
    @PrimaryKey
    val date: String,
    @ColumnInfo(name = "names_of_fulfilled_habits")
    val namesHabitsFulfilled: String,
    @ColumnInfo(name = "names_all_habits")
    val namesAllHabits: String,
    val areAllFulfilled: Boolean
)
