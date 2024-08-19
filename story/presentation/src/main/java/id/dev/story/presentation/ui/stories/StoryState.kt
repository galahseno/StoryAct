package id.dev.story.presentation.ui.stories

import id.dev.story.presentation.mapper.StoryFilter

data class StoryState(
    val selectedFilter: StoryFilter = StoryFilter.ALL,
)
