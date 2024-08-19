package id.dev.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import id.dev.core.database.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun upsertAll(remoteKeys: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeyId(id: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys_table")
    suspend fun deleteAll()
}