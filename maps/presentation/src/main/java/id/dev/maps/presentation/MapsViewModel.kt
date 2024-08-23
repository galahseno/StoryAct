package id.dev.maps.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.util.Result
import id.dev.core.presentation.ui.asUiText
import id.dev.maps.domain.MapsRepository
import id.dev.maps.presentation.components.MapsUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MapsViewModel(
    private val mapsRepository: MapsRepository
) : ViewModel() {

    var state by mutableStateOf(MapsState())
        private set

    private val _eventsChannel = Channel<MapsEvent>()
    val events = _eventsChannel.receiveAsFlow()

    init {
        mapsRepository.getThemes()
            .onEach {
                state = state.copy(
                    isDarkMode = it?.isDarkMode ?: false
                )
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            when (val result = mapsRepository.getStories()) {
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false
                    )
                    _eventsChannel.send(MapsEvent.Error(result.error.asUiText()))
                }

                is Result.Success -> {
                    val detailStory = result.data.listStory

                    state = state.copy(
                        listMarker = detailStory.map {
                            MapsUi(
                                id = it.id,
                                name = it.name,
                                lat = it.lat,
                                lon = it.lon,
                                description = it.description,
                                location = it.location
                            )
                        },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onAction(action: MapsAction) {
        when (action) {
            is MapsAction.OnStoryClick -> {
                state = state.copy(
                    listMarker = state.listMarker.map {
                        it.copy(
                            isSelected = it.id == action.id
                        )
                    }
                )
            }
        }
    }
}