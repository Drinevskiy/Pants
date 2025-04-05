package com.example.pants.di

import com.example.pants.presentation.common.SharedGameViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    viewModelOf(::SharedGameViewModel)
}
