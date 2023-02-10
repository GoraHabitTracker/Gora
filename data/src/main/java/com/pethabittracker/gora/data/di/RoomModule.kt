package com.pethabittracker.gora.data.di

import androidx.room.Room
import com.pethabittracker.gora.data.database.HabitDatabase
import org.koin.dsl.module

internal val roomModule = module {
    single {
        Room.databaseBuilder(
            get(),
            HabitDatabase::class.java,
            "database"
        )
           // .allowMainThreadQueries()
            .build()
    }

    single { get<HabitDatabase>().habitDao() }
    single { get<HabitDatabase>().calendarDataDao() }
}
