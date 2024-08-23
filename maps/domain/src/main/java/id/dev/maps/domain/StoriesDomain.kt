package id.dev.maps.domain

data class StoriesDomain(
    val error: Boolean,
    val listStory: List<StoryDomain>,
    val message: String
)
