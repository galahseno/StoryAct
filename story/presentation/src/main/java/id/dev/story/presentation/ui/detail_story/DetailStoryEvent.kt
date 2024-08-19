package id.dev.story.presentation.ui.detail_story

import id.dev.core.presentation.ui.UiText

interface DetailStoryEvent {
    data class SuccessAddOrFavorite(val isFavorite: Boolean): DetailStoryEvent
    data class Error(val error: UiText): DetailStoryEvent
}