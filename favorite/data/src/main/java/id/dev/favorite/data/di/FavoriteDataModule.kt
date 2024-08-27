package id.dev.favorite.data.di

import id.dev.favorite.data.FavoriteRepositoryImpl
import id.dev.favorite.domain.FavoriteRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val favoriteDataModule = module {
    singleOf(::FavoriteRepositoryImpl).bind<FavoriteRepository>()
}