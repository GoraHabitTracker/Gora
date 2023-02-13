package com.pethabittracker.gora.data.utils

import android.content.Context
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*


val rusLocale = Locale("ru","RU")

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, rusLocale)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {

    return getDisplayName(TextStyle.SHORT, rusLocale).let { value ->
        if (uppercase) value.uppercase(rusLocale) else value
    }
}

fun Context.getColorCompat(@ColorRes color: Int) =
    ContextCompat.getColor(this, color)

fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

fun TextView.setBackgroundColorRes(@DrawableRes color: Int) =
    setBackgroundResource(color)
