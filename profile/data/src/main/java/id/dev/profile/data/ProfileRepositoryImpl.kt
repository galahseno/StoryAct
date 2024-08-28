package id.dev.profile.data

import androidx.room.withTransaction
import id.dev.core.database.StoryDb
import id.dev.core.domain.AuthInfo
import id.dev.core.domain.SessionStorage
import id.dev.core.domain.ThemesInfo
import id.dev.core.network.invalidateBearerTokens
import id.dev.profile.domain.ProfileRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage,
    private val storyDb: StoryDb
): ProfileRepository {

    override suspend fun logout() {
        httpClient.invalidateBearerTokens()
        sessionStorage.setAuth(null)
        storyDb.withTransaction {
            storyDb.apply {
                storyDao().deleteAll()
                remoteKeysDao().deleteAll()
                favoriteDao().deleteAll()
            }
        }
    }

    override suspend fun getAuthInfo(): AuthInfo? {
        return sessionStorage.getAuth()
    }

    override fun getThemes(): Flow<ThemesInfo?> {
        return sessionStorage.getThemes()
    }

    override suspend fun setThemes(info: ThemesInfo?) {
        sessionStorage.setThemes(info)
    }
}