package id.dev.maps.data

import id.dev.core.domain.story.LocationParser
import id.dev.core.domain.SessionStorage
import id.dev.core.domain.ThemesInfo
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import id.dev.core.domain.util.map
import id.dev.core.network.get
import id.dev.maps.data.dto.StoriesResponse
import id.dev.maps.data.mapper.toStoryDomain
import id.dev.maps.domain.MapsRepository
import id.dev.maps.domain.StoriesDomain
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class MapsRepositoryImpl(
    private val httpClient: HttpClient,
    private val locationParser: LocationParser,
    private val sessionStorage: SessionStorage
) : MapsRepository {
    override suspend fun getStories(): Result<StoriesDomain, DataError.Network> {
        return httpClient.get<StoriesResponse>(
            route = "/stories",
            queryParameters = mapOf(
                "page" to 1,
                "size" to 100,
                "location" to 1
            )
        ).map {
            StoriesDomain(
                error = it.error,
                message = it.message,
                listStory = it.listStory.map { story ->
                    val location = locationParser.parseLocation(story.lat, story.lon).first()
                    story.toStoryDomain(location)
                }
            )
        }
    }

    override fun getThemes(): Flow<ThemesInfo?> {
        return sessionStorage.getThemes()
    }
}