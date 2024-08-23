package id.dev.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.ThemesInfo
import id.dev.profile.domain.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private val _eventChannel = Channel<ProfileEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        profileRepository.getThemes()
            .onEach {
                state = state.copy(
                    isDarkMode = it?.isDarkMode ?: false
                )
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            profileRepository.getAuthInfo()?.let {
                state = state.copy(
                    name = it.name,
                    email = it.email
                )
            }
        }
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnChangeThemes -> {
                viewModelScope.launch {
                    profileRepository.setThemes(
                        ThemesInfo(
                            isDarkMode = action.value
                        )
                    )
                }
                state = state.copy(
                    isDarkMode = action.value
                )
            }

            is ProfileAction.OnLogoutClick -> {
                state = state.copy(isLoading = true)
                viewModelScope.launch {
                    delay(1500) // Simulate logout
                    profileRepository.logout()
                    _eventChannel.send(ProfileEvent.LogoutSuccess)
                    state = state.copy(isLoading = false)
                }
            }
        }
    }
}