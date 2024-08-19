package id.dev.core.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    val token: String,
    val email: String,
    val userId: String,
    val name: String
)