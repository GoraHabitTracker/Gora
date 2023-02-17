package com.pethabittracker.gora.data.models

import androidx.room.Entity

@Entity
internal data class DaysOfWeekEntity(
    val monday: Boolean,
    val thursday: Boolean,
    val wednesday: Boolean,
    val tuesday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean
)
