package id.dev.story.domain.stories

import id.dev.core.domain.story.StoryDomain

data class StoriesDomain(
    val error: Boolean,
    val listStory: List<StoryDomain>,
    val message: String
)
