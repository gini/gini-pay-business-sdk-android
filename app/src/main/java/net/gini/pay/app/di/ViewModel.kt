package net.gini.pay.app.di

import net.gini.pay.app.MainViewModel
import net.gini.pay.app.review.ReviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ReviewViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
}