package id.dev.profile.presentation

interface ProfileAction {
    data object OnLogoutClick : ProfileAction
    data class OnChangeThemes(val value: Boolean) : ProfileAction
}