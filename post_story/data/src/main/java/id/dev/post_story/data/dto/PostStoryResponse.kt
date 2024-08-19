package id.dev.post_story.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class PostStoryResponse(
    val error: Boolean,
    val message: String
)