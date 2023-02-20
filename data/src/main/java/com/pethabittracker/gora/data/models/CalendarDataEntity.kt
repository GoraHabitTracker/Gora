package com.pethabittracker.gora.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index("name", "date", unique = true)
    ]
)
internal data class CalendarDataEntity(
//    @PrimaryKey
//    val date: String,
//    @ColumnInfo(name = "names_of_fulfilled_habits")
//    val namesHabitsFulfilled: String,
//    @ColumnInfo(name = "names_all_habits")
//    val namesAllHabits: String,
//    val areAllFulfilled: Boolean


    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: String,
    val state: Int

)

//@Entity
//internal data class State(
//    val default: Boolean,
//    val Done: Boolean,
//    val Skip: Boolean,
//    val Inactive: Boolean
//)