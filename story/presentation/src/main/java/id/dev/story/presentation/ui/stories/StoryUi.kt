package id.dev.story.presentation.ui.stories

data class StoryUi(
    val createdAt: String = "",
    val description: String = "",
    val id: String = "",
    val lat: Double? = null,
    val lon: Double? = null,
    val name: String = "",
    val photoUrl: String = "",
    val location: String? = null
)
