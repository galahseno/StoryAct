package id.dev.story.domain.source

import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import id.dev.story.domain.detail_story.DetailStoryDomain
import id.dev.story.domain.stories.StoriesDomain

interface StoryRemoteDataSource {
    suspend fun getStories(page: Int, size: Int, location: Int):  Result<StoriesDomain, DataError.Network>
    suspend fun getStoryById(id: String): Result<DetailStoryDomain, DataError.Network>
}