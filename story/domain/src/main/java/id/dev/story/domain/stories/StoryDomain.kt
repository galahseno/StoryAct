package id.dev.story.domain.stories

data class StoryDomain(
    val createdAt: String,
    val description: String,
    val id: String,
    val lat: Double?,
    val lon: Double?,
    val name: String,
    val photoUrl: String,
    val location: String?
)
