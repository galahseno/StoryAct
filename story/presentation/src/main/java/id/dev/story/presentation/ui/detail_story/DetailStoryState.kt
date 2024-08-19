package id.dev.story.presentation.ui.detail_story

data class DetailStoryState(
    val createdAt: String = "",
    val description: String = "",
    val id: String = "",
    val lat: Double? = null,
    val lon: Double? = null,
    val name: String = "",
    val photoUrl: String = "",
    val location: String? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true
)
