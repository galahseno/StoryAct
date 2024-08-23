package id.dev.core.data.story

import id.dev.core.database.entity.StoryEntity
import id.dev.core.domain.story.StoryDomain

fun StoryEntity.toStoryDomain(): StoryDomain {
    return StoryDomain(
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