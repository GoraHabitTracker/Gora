package com.pethabittracker.gora.domain.models

data class WeekList(
    var monday: Boolean,
    var thursday: Boolean,
    var wednesday: Boolean,
    var tuesday: Boolean,
    var friday: Boolean,
    var saturday: Boolean,
    var sunday: Boolean
)
