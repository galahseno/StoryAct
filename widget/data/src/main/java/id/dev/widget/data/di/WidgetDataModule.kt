package id.dev.widget.data.di

import id.dev.widget.data.WidgetRepositoryImpl
import id.dev.widget.domain.WidgetRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val widgetDataModule = module {
    singleOf(::WidgetRepositoryImpl).bind<WidgetRepository>()
}