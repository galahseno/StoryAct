package id.dev.maps.data.mapper

import id.dev.maps.data.dto.Story
import id.dev.maps.domain.StoryDomain

fun Story.toStoryDomain(location: String?): StoryDomain {
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
