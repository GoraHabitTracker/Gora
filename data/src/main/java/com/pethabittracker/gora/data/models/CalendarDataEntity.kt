package com.pethabittracker.gora.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "calendar_data",
    indices = [
        Index("name", "date", unique = true)
    ]
)
internal data class CalendarDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: String,
    val state: Int,     // = Priority
    val clicked: Boolean
)
