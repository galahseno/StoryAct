package id.dev.auth.presentation.login

import id.dev.core.presentation.ui.UiText

interface LoginEvent {
    data class Error(val error: UiText): LoginEvent
    data object LoginSuccess: LoginEvent
}