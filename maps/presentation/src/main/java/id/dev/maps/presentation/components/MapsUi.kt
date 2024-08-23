package id.dev.maps.presentation.components

data class MapsUi(
    val id: String = "",
    val lat: Double? = null,
    val lon: Double? = null,
    val name: String = "",
    val description: String = "",
    val location: String? = null,
    val isSelected: Boolean = false,
)
