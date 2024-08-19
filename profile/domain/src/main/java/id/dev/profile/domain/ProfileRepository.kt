package id.dev.profile.domain

import id.dev.core.domain.AuthInfo
import id.dev.core.domain.ThemesInfo
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun logout()
    suspend fun getAuthInfo(): AuthInfo?
    fun getThemes(): Flow<ThemesInfo?>
    suspend fun setThemes(info: ThemesInfo?)
}