package id.dev.favorite.favorite_feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.SessionStorage
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FavoriteActivityViewModel(
    sessionStorage: SessionStorage,
) : ViewModel() {

    var state by mutableStateOf(FavoriteActivityState())
        private set

    init {
        sessionStorage.getThemes()
            .onEach {
                state = state.copy(
                    isDarkMode = it?.isDarkMode ?: false
                )
            }
            .launchIn(viewModelScope)
    }
}