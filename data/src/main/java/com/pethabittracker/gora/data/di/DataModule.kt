package com.pethabittracker.gora.data.di

import org.koin.dsl.module

val dataModule = module {
    includes(
        roomModule,
        repositoryModule,
        useCaseModule
    )
}
