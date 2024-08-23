package id.dev.maps.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class StoriesResponse(
    val error: Boolean,
    val listStory: List<Story>,
    val message: String
)