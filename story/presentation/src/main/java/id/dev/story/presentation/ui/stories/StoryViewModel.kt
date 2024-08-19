@file:OptIn(ExperimentalCoroutinesApi::class)

package id.dev.story.presentation.ui.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import id.dev.story.domain.source.StoryRepository
import id.dev.story.presentation.mapper.StoryFilter
import id.dev.story.presentation.mapper.toStoryUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class StoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StoryState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<StoryEvent>()
    val events = _eventChannel.receiveAsFlow()

    val story: Flow<PagingData<StoryUi>> = _state.flatMapLatest { filterState ->
        storyRepository
            .getStories(
                location = when (filterState.selectedFilter) {
                    StoryFilter.LOCATION -> 1
                    StoryFilter.ALL -> 0
                }
            )
            .cachedIn(viewModelScope).map {
                it.map { story ->
                    story.toStoryUi()
                }
            }
    }

    fun onAction(action: StoryAction) {
        when (action) {
            is StoryAction.OnFilterChange -> {
                _state.update {
                    it.copy(
                        selectedFilter = action.filter
                    )
                }
            }

            is StoryAction.OnFavoriteClick -> {

            }
        }
    }
}