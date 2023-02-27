package com.pethabittracker.gora.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
internal data class DaysOfWeekEntity(
    @ColumnInfo(name = "mo")
    val monday: Boolean,

    @ColumnInfo(name = "tu")
    val tuesday: Boolean,

    @ColumnInfo(name = "we")
    val wednesday: Boolean,

    @ColumnInfo(name = "th")
    val thursday: Boolean,

    @ColumnInfo(name = "fr")
    val friday: Boolean,

    @ColumnInfo(name = "sa")
    val saturday: Boolean,

    @ColumnInfo(name = "su")
    val sunday: Boolean
)
