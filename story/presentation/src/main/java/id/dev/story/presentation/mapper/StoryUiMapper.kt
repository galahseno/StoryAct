package id.dev.story.presentation.mapper

import id.dev.story.domain.stories.StoryDomain
import id.dev.story.presentation.ui.stories.StoryUi
import java.util.Locale

fun StoryDomain.toStoryUi(): StoryUi {
    return StoryUi(
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