package id.dev.auth.data

import id.dev.auth.domain.AuthRemoteDataSource
import id.dev.auth.domain.AuthRepository
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String, DataError.Network> {
        return authRemoteDataSource.login(email, password)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<String, DataError.Network> {
       return authRemoteDataSource.register(name, email, password)
    }
}