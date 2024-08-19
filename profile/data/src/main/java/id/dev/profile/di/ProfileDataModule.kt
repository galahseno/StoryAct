package id.dev.profile.di

import id.dev.profile.data.ProfileRepositoryImpl
import id.dev.profile.domain.ProfileRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileDataModule = module {
    singleOf(::ProfileRepositoryImpl).bind<ProfileRepository>()
}