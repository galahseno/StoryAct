package id.dev.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_table")
data class RemoteKeyEntity(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
