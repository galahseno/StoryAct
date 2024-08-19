@file:OptIn(ExperimentalFoundationApi::class)

package id.dev.post_story.presentation

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import id.dev.core.presentation.ui.UiText
import id.dev.core.presentation.ui.asUiText
import id.dev.post_story.domain.PostStoryRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostStoryViewModel(
    private val postStoryRepository: PostStoryRepository
) : ViewModel() {

    var state by mutableStateOf(PostStoryState())
        private set

    private val _eventChannel = Channel<PostStoryEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        state.description.textAsFlow()
            .onEach { description ->
                state = state.copy(
                    isValidDescription = description.isNotEmpty(),
                    canUpload = description.isNotEmpty() && state.selectedImagePath != Uri.EMPTY
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: PostStoryAction) {
        when (action) {
            is PostStoryAction.OnCancelCameraAction -> {
                state = state.copy(
                    imagePath = null,
                    canOpenCamera = false,
                    selectedImagePath = Uri.EMPTY
                )
            }

            is PostStoryAction.OnTakeCameraAction -> {
                state = state.copy(
                    canOpenCamera = false,
                    selectedImagePath = action.capturedImagePath
                )
            }

            is PostStoryAction.OnOpenCameraAction -> {
                state = state.copy(
                    imagePath = action.imageUri,
                    canOpenCamera = true,
                )
            }

            is PostStoryAction.OnLocationChange -> {
                postStoryRepository.getLocation()
                    .distinctUntilChanged()
                    .onEach {
                        state = if (action.isLocationOn) {
                            state.copy(
                                lat = it.lat,
                                lon = it.long,
                                location = it.location
                            )
                        } else {
                            state.copy(
                                lat = null,
                                lon = null,
                                location = null
                            )
                        }
                    }.launchIn(viewModelScope)

                state = state.copy(
                    isLocationOn = action.isLocationOn
                )
            }

            is PostStoryAction.OnSelectGalleryAction -> {
                state = state.copy(
                    selectedImagePath = action.selectedImagePath,
                )
            }

            is PostStoryAction.OnUploadClick -> {
                state = state.copy(
                    isLoading = true
                )
                viewModelScope.launch {
                    val result = postStoryRepository.postStory(
                        description = state.description.text.toString(),
                        imagePath = state.selectedImagePath.toString(),
                        lat = state.lat,
                        lon = state.lon
                    )
                    when (result) {
                        is Result.Error -> {
                            state = state.copy(
                                isLoading = false
                            )

                            when (result.error) {
                                DataError.Network.BAD_REQUEST -> {
                                    _eventChannel.send(
                                        PostStoryEvent.Error(
                                            result.message?.let { UiText.DynamicString(it) }
                                                ?: UiText.StringResource(id.dev.core.presentation.ui.R.string.error_server_error)
                                        )
                                    )
                                }

                                else -> _eventChannel.send(PostStoryEvent.Error(result.error.asUiText()))
                            }
                        }

                        is Result.Success -> {
                            _eventChannel.send(PostStoryEvent.PostSuccess)
                        }
                    }
                }
            }
        }
    }
}