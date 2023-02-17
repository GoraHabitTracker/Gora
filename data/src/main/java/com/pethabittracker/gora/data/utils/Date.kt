package com.pethabittracker.gora.data.utils

import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getCurrentDayOfWeek(): String {
    val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("EEEE")
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    return LocalDate.now().format(formatter)
}

fun getCurrentDate(): String {
    val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy")
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    return LocalDate.now().format(formatter)
}
