package com.example.pants.di

import com.example.pants.domain.repository.ColorRepository
import com.example.pants.data.remote.repository.ColorRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val productRepositoryModule = module {
    singleOf(::ColorRepositoryImpl) { bind<ColorRepository>() }
}
