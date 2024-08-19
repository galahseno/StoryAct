package id.dev.post_story.data.di

import id.dev.post_story.data.LocationObserverImpl
import id.dev.post_story.data.PostStoryImpl
import id.dev.post_story.domain.LocationObserver
import id.dev.post_story.domain.PostStoryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val postStoryDataModule = module {
    singleOf(::PostStoryImpl).bind<PostStoryRepository>()
    singleOf(::LocationObserverImpl).bind<LocationObserver>()
}