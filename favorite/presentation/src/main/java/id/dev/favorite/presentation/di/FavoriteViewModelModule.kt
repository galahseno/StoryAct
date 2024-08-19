package id.dev.favorite.presentation.di

import id.dev.favorite.presentation.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val favoriteViewModelModule = module {
    viewModelOf(::FavoriteViewModel)
}