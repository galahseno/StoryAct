package id.dev.favorite.data

import id.dev.core.database.entity.FavoriteEntity
import id.dev.core.domain.story.StoryDomain

fun FavoriteEntity.toStoryDomain(): StoryDomain {
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