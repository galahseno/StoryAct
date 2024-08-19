package id.dev.favorite.domain

data class Favorite(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double?,
    val lon: Double?,
    val location: String?
)
