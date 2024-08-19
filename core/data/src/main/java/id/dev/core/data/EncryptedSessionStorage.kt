package id.dev.core.data

import android.content.SharedPreferences
import id.dev.core.data.auth.AuthInfoSerializable
import id.dev.core.data.auth.toAuthInfo
import id.dev.core.data.auth.toAuthInfoSerializable
import id.dev.core.data.themes.ThemesInfoSerializable
import id.dev.core.data.themes.toThemesInfo
import id.dev.core.data.themes.toThemesInfoSerializable
import id.dev.core.domain.AuthInfo
import id.dev.core.domain.SessionStorage
import id.dev.core.domain.ThemesInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences
) : SessionStorage {

    override suspend fun getAuth(): AuthInfo? {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(KEY_AUTH_INFO, null)
            json?.let {
                Json.decodeFromString<AuthInfoSerializable>(it).toAuthInfo()
            }
        }
    }

    override suspend fun setAuth(info: AuthInfo?) {
        withContext(Dispatchers.IO) {
            if (info == null) {
                sharedPreferences.edit().remove(KEY_AUTH_INFO).apply()
                return@withContext
            }

            val json = Json.encodeToString(info.toAuthInfoSerializable())
            sharedPreferences.edit().putString(KEY_AUTH_INFO, json).commit()
        }
    }

    override fun getThemes(): Flow<ThemesInfo?> {
        return callbackFlow {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == KEY_THEMES_INFO) {
                    val json = sharedPreferences.getString(KEY_THEMES_INFO, null)

                    trySend(json?.let {
                        Json.decodeFromString<ThemesInfoSerializable>(it).toThemesInfo()
                    })
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            if (sharedPreferences.contains(KEY_THEMES_INFO)) {
                val json = sharedPreferences.getString(KEY_THEMES_INFO, null)

                send(json?.let {
                    Json.decodeFromString<ThemesInfoSerializable>(it).toThemesInfo()
                })
            }
            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }

    override suspend fun setThemes(info: ThemesInfo?) {
        withContext(Dispatchers.IO) {
            if (info == null) {
                sharedPreferences.edit().remove(KEY_THEMES_INFO).apply()
                return@withContext
            }

            val json = Json.encodeToString(info.toThemesInfoSerializable())
            sharedPreferences.edit().putString(KEY_THEMES_INFO, json).commit()
        }
    }

    companion object {
        private const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
        private const val KEY_THEMES_INFO = "KEY_THEMES_INFO"
    }
}