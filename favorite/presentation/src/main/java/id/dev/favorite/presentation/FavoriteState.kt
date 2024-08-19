package id.dev.favorite.presentation

data class FavoriteState(
    val favoriteList: List<FavoriteUi> = emptyList(),
    val isLoading: Boolean = true
)

data class FavoriteUi(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val photoUrl: String = "",
    val createdAt: String = "",
    val lat: Double? = null,
    val lon: Double? = null,
    val location: String? = null,
)

