package id.dev.story.presentation.ui.stories

import id.dev.story.presentation.mapper.StoryFilter

interface StoryAction {
    data class OnFilterChange(val filter: StoryFilter) : StoryAction
    data class OnFavoriteClick(val storyUi: StoryUi) : StoryAction
    data class OnStoryClick(val id: String) : StoryAction
}