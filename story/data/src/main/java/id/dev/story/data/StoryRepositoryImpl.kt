@file:OptIn(ExperimentalPagingApi::class)

package id.dev.story.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import id.dev.core.database.StoryDb
import id.dev.core.database.entity.FavoriteEntity
import id.dev.core.domain.LocationParser
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import id.dev.core.domain.util.map
import id.dev.story.data.mapper.toStoryDomain
import id.dev.story.domain.detail_story.DetailStoryDomain
import id.dev.story.domain.source.StoryRemoteDataSource
import id.dev.story.domain.source.StoryRepository
import id.dev.story.domain.stories.StoryDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class StoryRepositoryImpl(
    private val storyDb: StoryDb,
    private val remoteDataSource: StoryRemoteDataSource,
    private val locationParser: LocationParser
) : StoryRepository {

    override fun getStories(location: Int): Flow<PagingData<StoryDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3
            ),
            remoteMediator = StoryRemoteMediator(
                storyDb,
                remoteDataSource,
                location,
                locationParser
            ),
            pagingSourceFactory = {
                storyDb.storyDao().getAllStories()
            }
        ).flow.map { pagingData ->
            pagingData.map { story ->
                story.toStoryDomain()
            }
        }
    }

    override suspend fun getStoryById(id: String): Result<DetailStoryDomain, DataError.Network> {
        return remoteDataSource.getStoryById(id).map {
            DetailStoryDomain(
                error = it.error,
                message = it.message,
                story = it.story.copy(
                    location = locationParser.parseLocation(it.story.lat, it.story.lon).first()
                )
            )
        }
    }

    override fun checkFavoriteById(id: String): Flow<Boolean> {
        return storyDb.favoriteDao().checkFavoriteById(id)
    }

    override suspend fun insertToFavorite(storyDomain: StoryDomain) {
        val favoriteEntity = FavoriteEntity(
            id = storyDomain.id,
            name = storyDomain.name,
            photoUrl = storyDomain.photoUrl,
            location = storyDomain.location,
            lat = storyDomain.lat,
            lon = storyDomain.lon,
            createdAt = storyDomain.createdAt,
            description = storyDomain.description
        )
        storyDb.favoriteDao().upsertFavorite(favoriteEntity)
    }

    override suspend fun deleteFromFavorite(id: String) {
        storyDb.favoriteDao().deleteFavorite(id)
    }
}