package id.dev.core.presentation.ui.story

import android.content.Context
import id.dev.core.domain.story.StoryDomain
import id.dev.core.presentation.ui.R
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun String.calculateDurationBetweenNow(context: Context): String {
    if (this.isEmpty()) return context.getString(R.string.error_couldnt_load_created_at)
    val now = Clock.System.now()
    val formatter = DateTimeFormatter.ISO_INSTANT

    val date1Str = this
    val date2Str = formatter.format(now.toJavaInstant())

    val date1 = Instant.parse(date1Str)
    val date2 = Instant.parse(date2Str)

    return when (val duration = date2 - date1) {
        in 0.seconds..59.seconds -> context.getString(R.string.just_now)
        in 1.minutes..59.minutes -> context.getString(R.string.minutes_ago, duration.inWholeMinutes)
        in 1.hours..59.hours -> context.getString(R.string.hours_ago, duration.inWholeHours)
        else -> context.getString(R.string.days_ago, duration.inWholeDays)
    }
}

fun Int.formatNumber(): String {
    return when {
        this >= 1_000_000 -> String.format(Locale.ROOT, "%.1fM", this / 1_000_000.0)
        this >= 1_000 -> String.format(Locale.ROOT, "%.1fK", this / 1_000.0)
        else -> this.toString()
    }
}

fun StoryDomain.toStoryUi(): StoryUi {
    return StoryUi(
        id = id,
        name = name,
        description = description,
        photoUrl = photoUrl,
        createdAt = createdAt,
        lat = lat,
        lon = lon,
        location = location
    )
}