package id.dev.maps.presentation

import id.dev.maps.presentation.components.MapsUi

data class MapsState(
    val listMarker: List<MapsUi> = listOf(),
    val isLoading: Boolean = true,
    val isDarkMode: Boolean = false,
)
