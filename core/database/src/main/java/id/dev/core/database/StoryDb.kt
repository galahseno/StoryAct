package id.dev.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.dev.core.database.dao.FavoriteDao
import id.dev.core.database.dao.RemoteKeyDao
import id.dev.core.database.dao.StoryDao
import id.dev.core.database.entity.FavoriteEntity
import id.dev.core.database.entity.RemoteKeyEntity
import id.dev.core.database.entity.StoryEntity

@Database(
    version = 1,
    entities = [StoryEntity::class, RemoteKeyEntity::class, FavoriteEntity::class]
)
abstract class StoryDb : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun remoteKeysDao(): RemoteKeyDao
}