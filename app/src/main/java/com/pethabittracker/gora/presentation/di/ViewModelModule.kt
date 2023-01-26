package com.pethabittracker.gora.presentation.di

import com.pethabittracker.gora.presentation.ui.home.HomeViewModel
import com.pethabittracker.gora.presentation.ui.newhabits.NewHabitViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::HomeViewModel)
    viewModelOf(::NewHabitViewModel)
//    viewModelOf(::CalendarViewModel)
//    viewModelOf(::achievementsViewModel)
//    viewModelOf(::settingsViewModel)
//    viewModelOf(::new_habitViewModel)

}