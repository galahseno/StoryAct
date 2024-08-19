package id.dev.core.domain

import kotlinx.coroutines.flow.Flow

interface LocationParser {
    suspend fun parseLocation(lat: Double?, lon: Double?): Flow<String?>
}