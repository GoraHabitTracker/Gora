package com.pethabittracker.gora.presentation.extensions

import com.pethabittracker.gora.data.utils.getCurrentDayOfWeek
import com.pethabittracker.gora.domain.models.Habit

fun Habit.filterCurrentDay(): Boolean =
    when (getCurrentDayOfWeek()) {
        "Monday" -> this.repeatDays.monday
        "Thursday" -> this.repeatDays.thursday
        "Wednesday" -> this.repeatDays.wednesday
        "Tuesday" -> this.repeatDays.tuesday
        "Friday" -> this.repeatDays.friday
        "Saturday" -> this.repeatDays.saturday
        else -> this.repeatDays.sunday
    }
