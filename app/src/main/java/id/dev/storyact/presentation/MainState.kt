package id.dev.storyact.presentation

data class MainState(
    val isDarkMode: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isCheckingAuth: Boolean = false,
)
