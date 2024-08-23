@file:OptIn(ExperimentalFoundationApi::class)

package id.dev.auth.presentation.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.auth.domain.AuthRepository
import id.dev.auth.domain.UserDataValidator
import id.dev.auth.presentation.R
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import id.dev.core.presentation.ui.UiText
import id.dev.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val _eventChannel = Channel<LoginEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        combine(state.email.textAsFlow(), state.password.textAsFlow()) { email, password ->
            state = state.copy(
                canLogin = userDataValidator.isValidEmail(
                    email = email.toString().trim()
                ) && (password.length >= UserDataValidator.MIN_PASSWORD_LENGTH)
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            LoginAction.OnTogglePasswordVisibility -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            else -> Unit
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)
            val result = authRepository.login(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(isLoggingIn = false)

            when (result) {
                is Result.Error -> {
                    when(result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            _eventChannel.send(
                                LoginEvent.Error(
                                    result.message?.let { UiText.DynamicString(it) }
                                        ?: UiText.StringResource(R.string.error_email_password_incorrect)
                                )
                            )
                        }
                        DataError.Network.BAD_REQUEST -> {
                            _eventChannel.send(
                                LoginEvent.Error(
                                    result.message?.let { UiText.DynamicString(it) }
                                        ?: UiText.StringResource(id.dev.core.presentation.ui.R.string.error_server_error)
                                )
                            )
                        }
                        else -> _eventChannel.send(LoginEvent.Error(result.error.asUiText()))
                    }
                }

                is Result.Success -> {
                    _eventChannel.send(LoginEvent.LoginSuccess)
                }
            }
        }
    }
}