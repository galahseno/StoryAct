package id.dev.storyact.presentation

interface MainAction {
    data class SetDarkMode(val isDarkMode: Boolean) : MainAction
    data class SetFavoriteDialogVisibility(val isVisible: Boolean) : MainAction
}