package id.dev.maps.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class MapsViewModel(

) : ViewModel() {

    var state by mutableStateOf(MapsState())
        private set

    private val _eventsChannel = Channel<MapsEvent>()
    val event = _eventsChannel.receiveAsFlow()

    fun onAction(action: MapsAction) {
        when (action) {

        }
    }
}