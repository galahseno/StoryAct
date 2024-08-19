package id.dev.story.data.dto.stories


import id.dev.story.data.dto.Story
import kotlinx.serialization.Serializable

@Serializable
data class StoriesResponse(
    val error: Boolean,
    val listStory: List<Story>,
    val message: String
)