package id.dev.story.presentation.ui.detail_story

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.util.Result
import id.dev.core.presentation.ui.asUiText
import id.dev.story.domain.source.StoryRepository
import id.dev.story.domain.stories.StoryDomain
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DetailStoryViewModel(
    private val storyId: String,
    private val storyRepository: StoryRepository
) : ViewModel() {

    var state by mutableStateOf(DetailStoryState())
        private set

    private val _eventChannel = Channel<DetailStoryEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        storyRepository.checkFavoriteById(storyId)
            .distinctUntilChanged()
            .onEach {
                state = state.copy(
                    isFavorite = it
                )
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            when (val result = storyRepository.getStoryById(storyId)) {
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false
                    )
                    _eventChannel.send(DetailStoryEvent.Error(result.error.asUiText()))
                }

                is Result.Success -> {
                    val detailStory = result.data.story

                    state = state.copy(
                        createdAt = detailStory.createdAt,
                        description = detailStory.description,
                        id = detailStory.id,
                        lat = detailStory.lat,
                        lon = detailStory.lon,
                        name = detailStory.name,
                        photoUrl = detailStory.photoUrl,
                        location = detailStory.location,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onAction(action: DetailStoryAction) {
        when (action) {
            is DetailStoryAction.OnFavoriteClick -> {
                viewModelScope.launch {
                    if (state.isFavorite) {
                        storyRepository.deleteFromFavorite(storyId)
                        _eventChannel.send(DetailStoryEvent.SuccessAddOrFavorite(false))
                    } else {
                        val storyDomain = StoryDomain(
                            createdAt = state.createdAt,
                            description = state.description,
                            id = state.id,
                            lat = state.lat,
                            lon = state.lon,
                            name = state.name,
                            photoUrl = state.photoUrl,
                            location = state.location
                        )
                        storyRepository.insertToFavorite(storyDomain)
                        _eventChannel.send(DetailStoryEvent.SuccessAddOrFavorite(true))
                    }
                    state = state.copy(
                        isFavorite = !state.isFavorite
                    )
                }
            }
        }
    }
}