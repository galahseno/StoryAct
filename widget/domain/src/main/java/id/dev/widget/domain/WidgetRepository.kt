package id.dev.widget.domain

import id.dev.core.domain.story.StoryDomain
import kotlinx.coroutines.flow.Flow

interface WidgetRepository {
    fun getAllStories(): Flow<List<StoryDomain>>
}