package id.dev.widget.data

import id.dev.core.data.story.toStoryDomain
import id.dev.core.database.dao.StoryDao
import id.dev.core.domain.story.StoryDomain
import id.dev.widget.domain.WidgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WidgetRepositoryImpl(
    private val storyDao: StoryDao
) : WidgetRepository {
    override fun getAllStories(): Flow<List<StoryDomain>> {
        return storyDao.getAllStoriesWidget().map {
            it.map { storyEntity ->
                storyEntity.toStoryDomain()
            }
        }
    }
}