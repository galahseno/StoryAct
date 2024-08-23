package id.dev.favorite.data

import id.dev.core.database.dao.FavoriteDao
import id.dev.core.domain.story.StoryDomain
import id.dev.favorite.domain.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {
    override fun getFavorite(): Flow<List<StoryDomain>> {
        return favoriteDao.getFavorite().map { favoriteList ->
            favoriteList.map { it.toStoryDomain() }
        }
    }

    override suspend fun deleteFavorite(id: String) {
        favoriteDao.deleteFavorite(id)
    }
}