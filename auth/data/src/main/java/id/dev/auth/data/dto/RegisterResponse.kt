package id.dev.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val error: Boolean,
    val message: String
)
