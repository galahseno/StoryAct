package id.dev.favorite.domain

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorite(): Flow<List<Favorite>>
    suspend fun deleteFavorite(id: String)
}