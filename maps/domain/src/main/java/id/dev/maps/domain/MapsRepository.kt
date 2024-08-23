package id.dev.maps.domain

import id.dev.core.domain.ThemesInfo
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface MapsRepository {
    suspend fun getStories(): Result<StoriesDomain, DataError.Network>
    fun getThemes(): Flow<ThemesInfo?>
}