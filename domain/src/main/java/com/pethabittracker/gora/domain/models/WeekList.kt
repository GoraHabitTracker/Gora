package com.pethabittracker.gora.domain.models

data class WeekList(
    val monday: Boolean,
    val thursday: Boolean,
    val wednesday: Boolean,
    val tuesday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean
)