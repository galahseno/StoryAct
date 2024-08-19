package id.dev.story.domain

import id.dev.core.domain.util.DataError

fun String.parseError(): DataError.Network {
    val regex = """Error\(error=(\w+), message=.*\)""".toRegex()
    val matchResult = regex.find(this) ?: return DataError.Network.UNKNOWN
    val errorType = matchResult.groupValues[1]
    println(matchResult.groupValues)
    return when (errorType) {
        "NO_INTERNET" -> DataError.Network.NO_INTERNET
        "REQUEST_TIMEOUT" -> DataError.Network.REQUEST_TIMEOUT
        "SERVER_ERROR" -> DataError.Network.SERVER_ERROR
        "TOO_MANY_REQUESTS" -> DataError.Network.TOO_MANY_REQUESTS
        "SERIALIZATION" -> DataError.Network.SERIALIZATION
        else -> DataError.Network.UNKNOWN
    }
}