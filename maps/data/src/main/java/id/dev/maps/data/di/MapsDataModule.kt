package id.dev.maps.data.di

import id.dev.maps.data.MapsRepositoryImpl
import id.dev.maps.domain.MapsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mapsDataModule = module {
    singleOf(::MapsRepositoryImpl).bind<MapsRepository>()
}