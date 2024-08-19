package id.dev.profile.presentation.di

import id.dev.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val profileViewModelModule = module {
    viewModelOf(::ProfileViewModel)
}