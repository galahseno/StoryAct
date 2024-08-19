package id.dev.core.presentation.ui

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun String.calculateDurationBetweenNow(): String {
    val now = Clock.System.now()
    val formatter = DateTimeFormatter.ISO_INSTANT

    val date1Str = this
    val date2Str = formatter.format(now.toJavaInstant())

    val date1 = Instant.parse(date1Str)
    val date2 = Instant.parse(date2Str)

    return when(val duration = date2 - date1) {
        in 0.seconds..59.seconds -> "just now"
        in 1.minutes..59.minutes -> "${duration.inWholeMinutes} minutes ago"
        in 1.hours..59.hours -> "${duration.inWholeHours} hours ago"
        else -> "${duration.inWholeDays} days ago"
    }
}

fun Int.formatNumber(): String {
    return when {
        this >= 1_000_000 -> String.format(Locale.ROOT, "%.1fM", this / 1_000_000.0)
        this >= 1_000 -> String.format(Locale.ROOT, "%.1fK", this / 1_000.0)
        else -> this.toString()
    }
}