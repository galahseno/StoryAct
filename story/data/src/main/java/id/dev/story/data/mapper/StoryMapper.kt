package id.dev.story.data.mapper

import id.dev.core.database.entity.StoryEntity
import id.dev.core.domain.story.StoryDomain
import id.dev.story.data.dto.stories.StoriesResponse
import id.dev.story.data.dto.Story
import id.dev.story.data.dto.detail_story.DetailStoryResponse
import id.dev.story.domain.detail_story.DetailStoryDomain
import id.dev.story.domain.stories.StoriesDomain

fun StoryDomain.toStoryEntity(location: String?): StoryEntity {
    return StoryEntity(
        id = id,
        name = name,
        description = description,
        photoUrl = photoUrl,
        createdAt = createdAt,
        lat = lat,
        lon = lon,
        location = location
    )
}

fun Story.toStoryDomain(): StoryDomain {
    return StoryDomain(
        id = id,
        name = name,
        description = description,
        photoUrl = photoUrl,
        createdAt = createdAt,
        lat = lat,
        lon = lon,
        location = null
    )
}

fun StoriesResponse.toStoriesDomain(): StoriesDomain {
    return StoriesDomain(
        error = error,
        listStory = listStory.map { it.toStoryDomain() },
        message = message
    )
}

fun DetailStoryResponse.toDetailStoryDomain(): DetailStoryDomain {
    return DetailStoryDomain(
        error = error,
        message = message,
        story = story.toStoryDomain()
    )
}