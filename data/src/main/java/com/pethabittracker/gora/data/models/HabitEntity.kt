package com.pethabittracker.gora.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "url_image")
    val urlImage: Int,
    val priority: Int
    @Embedded
    val repeatDays: WeekListEntity,
)

@Entity
data class WeekListEntity(
    val monday: Boolean,
    val thursday: Boolean,
    val wednesday: Boolean,
    val tuesday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean
)
