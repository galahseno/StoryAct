package id.dev.auth.domain

import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.EmptyResult
import id.dev.core.domain.util.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String, DataError.Network>
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<String, DataError.Network>
}