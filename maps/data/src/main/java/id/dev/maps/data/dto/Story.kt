package id.dev.maps.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class Story(
    val createdAt: String,
    val description: String,
    val id: String,
    val lat: Double?,
    val lon: Double?,
    val name: String,
    val photoUrl: String
)