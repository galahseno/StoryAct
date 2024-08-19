package id.dev.favorite.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.favorite.domain.FavoriteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    var state by mutableStateOf(FavoriteState())
        private set

    private val _eventsChannel = Channel<FavoriteEvent>()
    val events = _eventsChannel.receiveAsFlow()

    init {
        favoriteRepository.getFavorite().onEach { favoriteList ->
            state = state.copy(
                isLoading = false,
                favoriteList = favoriteList.map {
                    FavoriteUi(
                        id = it.id,
                        name = it.name,
                        photoUrl = it.photoUrl,
                        location = it.location,
                        lat = it.lat,
                        lon = it.lon,
                        createdAt = it.createdAt,
                        description = it.description
                    )
                }
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: FavoriteAction) {
        when (action) {
            is FavoriteAction.OnFavoriteItemDismiss -> {
                viewModelScope.launch {
                    favoriteRepository.deleteFavorite(action.id)
                    _eventsChannel.send(FavoriteEvent.SuccessDeleteFavorite)
                }
            }
        }
    }
}