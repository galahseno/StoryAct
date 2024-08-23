package id.dev.maps.presentation

interface MapsAction {
    data class OnStoryClick(val id: String) : MapsAction
}