package id.dev.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import id.dev.core.database.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM story_table")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Query("SELECT * FROM story_table")
    fun getAllStoriesWidget(): Flow<List<StoryEntity>>

    @Upsert
    suspend fun upsertStories(stories: List<StoryEntity>)

    @Query("DELETE FROM story_table")
    suspend fun deleteAll()

}