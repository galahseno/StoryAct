package id.dev.favorite.presentation

import id.dev.core.presentation.ui.story.StoryUi

data class FavoriteState(
    val favoriteList: List<StoryUi> = emptyList(),
    val isLoading: Boolean = true
)

