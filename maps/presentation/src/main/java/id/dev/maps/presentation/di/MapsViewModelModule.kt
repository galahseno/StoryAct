package id.dev.maps.presentation.di

import id.dev.maps.presentation.MapsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val mapsViewModelModule = module {
    viewModelOf(::MapsViewModel)
}