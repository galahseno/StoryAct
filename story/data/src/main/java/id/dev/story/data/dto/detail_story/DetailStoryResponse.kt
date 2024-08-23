package id.dev.story.data.dto.detail_story


import id.dev.story.data.dto.Story
import kotlinx.serialization.Serializable

@Serializable
data class DetailStoryResponse(
    val error: Boolean,
    val message: String,
    val story: Story
)