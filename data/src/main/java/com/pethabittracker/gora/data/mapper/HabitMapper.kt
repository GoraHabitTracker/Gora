package com.pethabittracker.gora.data.mapper

import com.pethabittracker.gora.data.models.DaysOfWeekEntity
import com.pethabittracker.gora.data.models.HabitEntity
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.models.HabitId
import com.pethabittracker.gora.domain.models.WeekList

internal fun List<HabitEntity>.toDomainModels(): List<Habit> = map {
    it.toDomain()
}

internal fun HabitId.toDomain(): Int {
    return id
}

internal fun HabitEntity.toDomain(): Habit {
    return Habit(
        id = HabitId(id),
        name = name,
        urlImage = urlImage,
        priority = priority,
        repeatDays = repeatDays.toDomain()
    )
}

internal fun Habit.toData(): HabitEntity {
    return HabitEntity(
        id = id.toDomain(),
        name = name,
        urlImage = urlImage,
        priority = priority,
        repeatDays = repeatDays.toData()
    )
}

internal fun DaysOfWeekEntity.toDomain(): WeekList {
    return WeekList(
        monday = monday,
        tuesday = tuesday,
        wednesday = wednesday,
        thursday = thursday,
        friday = friday,
        saturday = saturday,
        sunday = sunday
    )
}

internal fun WeekList.toData(): DaysOfWeekEntity {
    return DaysOfWeekEntity(
        monday = monday,
        tuesday = tuesday,
        wednesday = wednesday,
        thursday = thursday,
        friday = friday,
        saturday = saturday,
        sunday = sunday
    )
}