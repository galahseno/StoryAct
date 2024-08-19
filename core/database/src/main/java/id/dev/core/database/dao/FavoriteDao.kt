package id.dev.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import id.dev.core.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT EXISTS(SELECT * FROM favorite_table WHERE id = :id)")
    fun checkFavoriteById(id: String): Flow<Boolean>

    @Upsert
    suspend fun upsertFavorite(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorite_table")
    fun getFavorite(): Flow<List<FavoriteEntity>>

    @Query("DELETE FROM favorite_table WHERE id = :id")
    suspend fun deleteFavorite(id: String)
}