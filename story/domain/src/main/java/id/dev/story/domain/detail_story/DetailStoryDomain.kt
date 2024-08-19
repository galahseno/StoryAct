package id.dev.story.domain.detail_story


import id.dev.story.domain.stories.StoryDomain

data class DetailStoryDomain(
    val error: Boolean,
    val message: String,
    val story: StoryDomain
)