package id.dev.favorite.domain

import id.dev.core.domain.story.StoryDomain
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorite(): Flow<List<StoryDomain>>
    suspend fun deleteFavorite(id: String)
}