package com.pethabittracker.gora.presentation.ui.calendar

import androidx.lifecycle.ViewModel
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import com.pethabittracker.gora.presentation.models.Priority
import java.time.LocalDate

class CalendarViewModel(
    private val calendarRepository: CalendarDataRepository
) : ViewModel() {

    suspend fun getDoneList(): List<LocalDate> {
        return calendarRepository.getAllCalendarData()
            .filter { item -> item.state == Priority.Done.value }.map { item -> item.date }
    }

    suspend fun getDefaultList(): List<LocalDate> {
        return calendarRepository.getAllCalendarData()
            .filter { item -> item.state == Priority.Default.value }.map { item -> item.date }
    }
    suspend fun getInactiveList(): List<LocalDate> {
        return calendarRepository.getAllCalendarData()
            .filter { item -> item.state == Priority.Inactive.value }.map { item -> item.date }
    }
    suspend fun getSkipList(): List<LocalDate> {
        return calendarRepository.getAllCalendarData()
            .filter { item -> item.state == Priority.Skip.value }.map { item -> item.date }
    }
}
