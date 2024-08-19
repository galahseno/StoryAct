package id.dev.post_story.presentation

import id.dev.core.presentation.ui.UiText

interface PostStoryEvent {
    data class Error(val error: UiText) : PostStoryEvent
    data object PostSuccess: PostStoryEvent
}