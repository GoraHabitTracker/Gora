package com.pethabittracker.gora.data.mapper

import com.pethabittracker.gora.data.R
import com.pethabittracker.gora.data.models.CalendarDataEntity
import com.pethabittracker.gora.domain.models.CalendarData
import java.time.LocalDate

internal fun List<CalendarDataEntity>.toDomainModels(): List<CalendarData> = map {
    it.toDomain()
}

//internal fun CalendarDataEntity.toDomain(): CalendarData {
//    return CalendarData(
//        date = LocalDate.parse(date),
//        namesHabitsFulfilled = namesHabitsFulfilled.toListString(),
//        namesAllHabits = namesAllHabits.toListString(),
//        areAllFulfilled = areAllFulfilled
//    )
//}
//
//internal fun CalendarData.toData(): CalendarDataEntity {
//    return CalendarDataEntity(
//        date = date.toString(),
//        namesHabitsFulfilled = namesHabitsFulfilled.toString(),
//        namesAllHabits = namesAllHabits.toString(),
//        areAllFulfilled = areAllFulfilled
//    )
//}

internal fun CalendarDataEntity.toDomain(): CalendarData {
    return CalendarData(
        id = id,
        name = name,
        date = LocalDate.parse(date),
        state = state
    )
}

internal fun CalendarData.toData(): CalendarDataEntity {
    return CalendarDataEntity(
        id = id,
        name = name,
        date = date.toString(),
        state = state
    )
}

internal fun String.toListString(): Set<String> {
    val delimiter = R.string.comma_space.toString()
    val namesList: MutableSet<String> = mutableSetOf()
    if (this == R.string.square_brackets.toString()) return namesList      // вынести в константы

    this
        .trim(R.string.square_brackets_open.toChar(), R.string.square_brackets_close.toChar())
        .split(delimiter)
        .forEach {
            namesList.add(it.trim())
        }
    return namesList
}
