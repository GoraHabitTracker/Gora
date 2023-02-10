package com.pethabittracker.gora.data.di

import com.pethabittracker.gora.data.repositories.CalendarDataDataRepositoryImpl
import com.pethabittracker.gora.data.repositories.HabitRepositoryImpl
import com.pethabittracker.gora.domain.repositories.CalendarDataRepository
import com.pethabittracker.gora.domain.repositories.HabitRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val repositoryModule = module {

    singleOf(::HabitRepositoryImpl) {
        bind<HabitRepository>()
    }

    singleOf(::CalendarDataDataRepositoryImpl) {
        bind<CalendarDataRepository>()
    }
}
