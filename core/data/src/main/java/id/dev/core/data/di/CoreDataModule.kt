package id.dev.core.data.di

import id.dev.core.data.EncryptedSessionStorage
import id.dev.core.data.LocationParserImpl
import id.dev.core.domain.SessionStorage
import id.dev.core.domain.story.LocationParser
import id.dev.core.network.HttpClientFactory
import io.ktor.client.engine.android.Android
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build(Android.create())
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
    singleOf(::LocationParserImpl).bind<LocationParser>()
}