package di

import org.koin.dsl.module

fun appModule() = listOf(httpClientModule, repositoryModule, viewModel)