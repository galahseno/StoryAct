package id.dev.favorite.data

import id.dev.core.database.dao.FavoriteDao
import id.dev.favorite.domain.Favorite
import id.dev.favorite.domain.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {
    override fun getFavorite(): Flow<List<Favorite>> {
        return favoriteDao.getFavorite().map { favoriteList ->
            favoriteList.map { it.toFavorite() }
        }
    }

    override suspend fun deleteFavorite(id: String) {
        favoriteDao.deleteFavorite(id)
    }
}