package id.dev.story.data.di

import id.dev.story.data.StoryRepositoryImpl
import id.dev.story.data.source.StoryRemoteDataSourceImpl
import id.dev.story.domain.source.StoryRemoteDataSource
import id.dev.story.domain.source.StoryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val storyDataModule = module {
    singleOf(::StoryRemoteDataSourceImpl).bind<StoryRemoteDataSource>()
    singleOf(::StoryRepositoryImpl).bind<StoryRepository>()
}