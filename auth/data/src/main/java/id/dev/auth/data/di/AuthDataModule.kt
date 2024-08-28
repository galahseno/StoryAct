package id.dev.auth.data.di

import id.dev.auth.data.AuthRepositoryImpl
import id.dev.auth.data.EmailPatternValidator
import id.dev.auth.domain.AuthRepository
import id.dev.auth.domain.PatternValidator
import id.dev.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}