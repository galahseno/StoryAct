package id.dev.story.presentation.di

import id.dev.story.presentation.ui.detail_story.DetailStoryViewModel
import id.dev.story.presentation.ui.stories.StoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val storyViewModelModule = module {
    viewModelOf(::StoryViewModel)
    viewModelOf(::DetailStoryViewModel)
}