package id.dev.profile.presentation

data class ProfileState(
    val isLoading: Boolean = false,
    val isDarkMode: Boolean = false,
    val name: String = "",
    val email: String = ""
)
