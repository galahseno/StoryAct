package id.dev.core.domain.story

import kotlinx.coroutines.flow.Flow

interface LocationParser {
    suspend fun parseLocation(lat: Double?, lon: Double?): Flow<String?>
}