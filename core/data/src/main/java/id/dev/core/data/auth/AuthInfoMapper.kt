package id.dev.core.data.auth

import id.dev.core.domain.AuthInfo

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(
        token = token,
        email = email,
        userId = userId,
        name = name
    )
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(
        token = token,
        email = email,
        userId = userId,
        name = name
    )
}