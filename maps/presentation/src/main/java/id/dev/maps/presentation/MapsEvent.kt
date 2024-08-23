package id.dev.maps.presentation

import id.dev.core.presentation.ui.UiText

interface MapsEvent {
    data class Error(val message: UiText) : MapsEvent
}