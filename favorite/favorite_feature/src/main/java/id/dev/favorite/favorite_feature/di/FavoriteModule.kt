package id.dev.favorite.favorite_feature.di

import id.dev.favorite.favorite_feature.FavoriteActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val favoriteActivityModule = module {
    viewModelOf(::FavoriteActivityViewModel)
}