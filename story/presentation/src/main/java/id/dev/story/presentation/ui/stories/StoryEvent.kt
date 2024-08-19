package id.dev.story.presentation.ui.stories

import id.dev.core.presentation.ui.UiText

interface StoryEvent {
    data object SuccessAddFavorite: StoryEvent
    data class Error(val error: UiText): StoryEvent
}