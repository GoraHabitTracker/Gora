package com.pethabittracker.gora.data.mapper

import com.pethabittracker.gora.data.models.CalendarDataEntity
import com.pethabittracker.gora.domain.models.CalendarData
import java.time.LocalDate

internal fun List<CalendarDataEntity>.toDomainModels(): List<CalendarData> = map {
    it.toDomain()
}

internal fun CalendarDataEntity.toDomain(): CalendarData {
    return CalendarData(
        date = LocalDate.parse(date),
        namesHabitsFulfilled = namesHabitsFulfilled.toListString(),
        namesAllHabits = namesAllHabits.toListString(),
        areAllFulfilled = areAllFulfilled
    )
}

internal fun CalendarData.toData(): CalendarDataEntity {
    return CalendarDataEntity(
        date = date.toString(),
        namesHabitsFulfilled = namesHabitsFulfilled.toString(),
        namesAllHabits = namesAllHabits.toString(),
        areAllFulfilled = areAllFulfilled
    )
}

internal fun String.toListString(): Set<String> {
    val delimiter = ", "
    val namesList: MutableSet<String> = mutableSetOf()
    if (this == "[]") return namesList      // вынести в константы

    this
        .trim('[', ']')
        .split(delimiter)
        .forEach {
            namesList.add(it.trim())
        }
    return namesList
}
