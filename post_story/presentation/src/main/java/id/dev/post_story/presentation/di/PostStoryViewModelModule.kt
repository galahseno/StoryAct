package id.dev.post_story.presentation.di

import id.dev.post_story.presentation.PostStoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val postStoryViewModelModule = module {
    viewModelOf(::PostStoryViewModel)
}