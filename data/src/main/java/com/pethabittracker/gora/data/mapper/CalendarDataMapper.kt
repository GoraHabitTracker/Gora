package com.pethabittracker.gora.data.mapper

import com.pethabittracker.gora.data.models.CalendarDataEntity
import com.pethabittracker.gora.domain.models.CalendarData
import java.time.LocalDate

internal fun List<CalendarDataEntity>.toDomainModels(): List<CalendarData> = map {
    it.toDomain()
}
internal fun CalendarDataEntity.toDomain(): CalendarData {
    return CalendarData(
        id = id,
        name = name,
        date = LocalDate.parse(date),
        state = state,
        clicked = clicked
    )
}

internal fun CalendarData.toData(): CalendarDataEntity {
    return CalendarDataEntity(
        id = id,
        name = name,
        date = date.toString(),
        state = state,
        clicked = clicked
    )
}