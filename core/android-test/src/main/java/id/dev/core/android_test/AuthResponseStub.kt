package id.dev.core.android_test

import id.dev.auth.data.dto.LoginResponse
import id.dev.auth.data.dto.LoginResult
import id.dev.auth.data.dto.RegisterResponse

val loginSuccessResponseStub = LoginResponse(
    error = false,
    message = "success",
    loginResult = LoginResult(
        name = "test-name",
        token = "test-token",
        userId = "test-userId"
    )
)

val loginFailResponseStub = LoginResponse(
    error = true,
    message = "Email or Password is wrong",
)

val registerSuccessResponseStub = RegisterResponse(
    error = false,
    message = "Register Success"
)

val registerFailResponseStub = RegisterResponse(
    error = true,
    message = "Register Fail"
)