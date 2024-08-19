package id.dev.story.data.source

import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import id.dev.core.domain.util.map
import id.dev.core.network.get
import id.dev.story.data.dto.detail_story.DetailStoryResponse
import id.dev.story.data.dto.stories.StoriesResponse
import id.dev.story.data.mapper.toDetailStoryDomain
import id.dev.story.data.mapper.toStoriesDomain
import id.dev.story.domain.detail_story.DetailStoryDomain
import id.dev.story.domain.stories.StoriesDomain
import id.dev.story.domain.source.StoryRemoteDataSource
import io.ktor.client.HttpClient

class StoryRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : StoryRemoteDataSource {
    override suspend fun getStories(
        page: Int,
        size: Int,
        location: Int
    ): Result<StoriesDomain, DataError.Network> {
        return httpClient.get<StoriesResponse>(
            route = "/stories",
            queryParameters = mapOf(
                "page" to page,
                "size" to size,
                "location" to location
            )
        ).map {
            it.toStoriesDomain()
        }
    }

    override suspend fun getStoryById(id: String): Result<DetailStoryDomain, DataError.Network> {
        return httpClient.get<DetailStoryResponse>(
            route = "/stories/$id"
        ).map {
            it.toDetailStoryDomain()
        }
    }
}