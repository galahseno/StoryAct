package id.dev.story.presentation.ui.detail_story

interface DetailStoryAction {
    data object OnBackClick : DetailStoryAction
    data object OnFavoriteClick : DetailStoryAction
}