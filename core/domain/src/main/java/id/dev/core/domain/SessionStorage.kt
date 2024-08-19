package id.dev.core.domain

import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    suspend fun getAuth(): AuthInfo?
    suspend fun setAuth(info: AuthInfo?)
    fun getThemes(): Flow<ThemesInfo?>
    suspend fun setThemes(info: ThemesInfo?)
}