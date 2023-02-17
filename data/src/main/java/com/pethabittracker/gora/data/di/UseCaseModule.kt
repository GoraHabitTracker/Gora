package com.pethabittracker.gora.data.di

import com.pethabittracker.gora.data.usecase.NewHabitUseCase
import com.pethabittracker.gora.data.usecase.UpdateHabitPriorityUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val useCaseModule = module {
    singleOf(::NewHabitUseCase)
    singleOf(::UpdateHabitPriorityUseCase)
}