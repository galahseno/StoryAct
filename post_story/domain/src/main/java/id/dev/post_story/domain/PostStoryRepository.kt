package id.dev.post_story.domain

import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow


interface PostStoryRepository {
    suspend fun postStory(
        description: String,
        imagePath: String,
        lat: Double? = null,
        lon: Double? = null
    ): EmptyResult<DataError.Network>

    fun getLocation(): Flow<LocationDomain>
}