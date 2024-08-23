@file:OptIn(ExperimentalCoroutinesApi::class)

package id.dev.post_story.data

import android.content.Context
import android.net.Uri
import id.dev.core.domain.story.LocationParser
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.EmptyResult
import id.dev.core.domain.util.Result
import id.dev.core.domain.util.asEmptyDataResult
import id.dev.core.network.post
import id.dev.post_story.data.dto.PostStoryResponse
import id.dev.post_story.domain.LocationDomain
import id.dev.post_story.domain.LocationObserver
import id.dev.post_story.domain.PostStoryRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class PostStoryImpl(
    private val locationObserver: LocationObserver,
    private val locationParser: LocationParser,
    private val httpClient: HttpClient,
    private val context: Context
) : PostStoryRepository {
    override suspend fun postStory(
        description: String,
        imagePath: String,
        lat: Double?,
        lon: Double?
    ): EmptyResult<DataError.Network> {
        val imageUri = Uri.parse(imagePath).uriToFile(context)
        val result = httpClient.post<MultiPartFormDataContent, PostStoryResponse>(
            route = "/stories",
            body = MultiPartFormDataContent(
                formData {
                    append("description", description)
                    lat?.let { append("lat", it) }
                    lon?.let { append("lon", it) }
                    append("photo", imageUri.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/*")
                        append(HttpHeaders.ContentDisposition, "filename=${imageUri.name}")
                    })
                }
            )
        )
        when (result) {
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.BAD_REQUEST -> {
                        val response: PostStoryResponse? = result.message?.let {
                            Json.decodeFromString(it)
                        }
                        return Result.Error(DataError.Network.BAD_REQUEST, response?.message)
                    }

                    else -> return result
                }
            }

            is Result.Success -> return result.asEmptyDataResult()
        }
    }

    override fun getLocation(): Flow<LocationDomain> {
        return locationObserver.getLocation().flatMapLatest {
            locationParser.parseLocation(it.lat, it.long).map { location ->
                LocationDomain(
                    lat = it.lat,
                    long = it.long,
                    location = location
                )
            }
        }
    }
}