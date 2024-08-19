package id.dev.story.domain.stories

data class StoriesDomain(
    val error: Boolean,
    val listStory: List<StoryDomain>,
    val message: String
)
