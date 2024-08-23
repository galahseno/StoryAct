package id.dev.story.domain.source

import androidx.paging.PagingData
import id.dev.core.domain.story.StoryDomain
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import id.dev.story.domain.detail_story.DetailStoryDomain
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getStories(location: Int): Flow<PagingData<StoryDomain>>
    suspend fun getStoryById(id: String): Result<DetailStoryDomain, DataError.Network>
    fun checkFavoriteById(id: String): Flow<Boolean>
    suspend fun insertToFavorite(storyDomain: StoryDomain)
    suspend fun deleteFromFavorite(id: String)
}