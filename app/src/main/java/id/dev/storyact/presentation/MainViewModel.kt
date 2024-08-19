package id.dev.storyact.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.SessionStorage
import id.dev.core.domain.ThemesInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        sessionStorage.getThemes()
            .onEach {
                state = state.copy(
                    isDarkMode = it?.isDarkMode ?: false
                )
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)
            state = state.copy(
                isLoggedIn = sessionStorage.getAuth()?.token?.isNotEmpty() ?: false,
            )
            state = state.copy(isCheckingAuth = false)
        }
    }

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.SetDarkMode -> {
                viewModelScope.launch {
                    sessionStorage.setThemes(
                        ThemesInfo(
                            isDarkMode = action.isDarkMode
                        )
                    )
                }
            }

            is MainAction.SetFavoriteDialogVisibility -> {
                state = state.copy(showFavoriteInstallDialog = action.isVisible)
            }
        }
    }
}