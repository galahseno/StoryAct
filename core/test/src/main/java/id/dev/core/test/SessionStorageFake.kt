package id.dev.core.test

import id.dev.core.domain.AuthInfo
import id.dev.core.domain.SessionStorage
import id.dev.core.domain.ThemesInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SessionStorageFake : SessionStorage {

    private var authInfo: AuthInfo? = null
    private var themesInfo: ThemesInfo? = null

    override suspend fun getAuth(): AuthInfo? {
        return authInfo
    }

    override suspend fun setAuth(info: AuthInfo?) {
        authInfo = info
    }

    override fun getThemes(): Flow<ThemesInfo?> {
        return flowOf(themesInfo)
    }

    override suspend fun setThemes(info: ThemesInfo?) {
        themesInfo = info
    }
}