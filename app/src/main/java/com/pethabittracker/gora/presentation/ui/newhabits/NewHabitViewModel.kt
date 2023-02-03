package com.pethabittracker.gora.presentation.ui.newhabits

import androidx.lifecycle.ViewModel
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.domain.models.WeekList
import com.pethabittracker.gora.domain.repositories.HabitRepository
import com.pethabittracker.gora.data.usecase.NewHabitUseCase

class NewHabitViewModel(
private val newHabitUseCase: NewHabitUseCase
) : ViewModel() {

   suspend fun newHabit(name: String, url: Int, priority: Int, repeatDays: WeekList) =
        newHabitUseCase.invoke(name, url, priority, repeatDays)

}
