@file:OptIn(ExperimentalPagingApi::class)

package id.dev.story.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.dev.core.database.StoryDb
import id.dev.core.database.entity.RemoteKeyEntity
import id.dev.core.database.entity.StoryEntity
import id.dev.core.domain.story.LocationParser
import id.dev.core.domain.util.Result
import id.dev.story.data.mapper.toStoryEntity
import id.dev.story.domain.source.StoryRemoteDataSource
import kotlinx.coroutines.flow.first

class StoryRemoteMediator(
    private val storyDb: StoryDb,
    private val remoteDataSource: StoryRemoteDataSource,
    private val location: Int,
    private val locationParser: LocationParser
) : RemoteMediator<Int, StoryEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            when (val result = remoteDataSource.getStories(page, state.config.pageSize, location)) {
                is Result.Error -> {
                    MediatorResult.Error(Throwable(result.toString()))
                }

                is Result.Success -> {
                    val data = result.data.listStory
                    val endOfPaginationReached = data.isEmpty()

                    storyDb.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            storyDb.remoteKeysDao().deleteAll()
                            storyDb.storyDao().deleteAll()
                        }
                        val prevKey = if (page == 1) null else page - 1
                        val nextKey = if (endOfPaginationReached) null else page + 1
                        val keys = data.map {
                            RemoteKeyEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                        }
                        val storiesEntity = data.map {
                            val location = locationParser.parseLocation(it.lat, it.lon).first()
                            it.toStoryEntity(location)
                        }
                        storyDb.remoteKeysDao().upsertAll(keys)
                        storyDb.storyDao().upsertStories(storiesEntity)
                    }
                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
            }
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storyDb.remoteKeysDao().getRemoteKeyId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storyDb.remoteKeysDao().getRemoteKeyId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storyDb.remoteKeysDao().getRemoteKeyId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}