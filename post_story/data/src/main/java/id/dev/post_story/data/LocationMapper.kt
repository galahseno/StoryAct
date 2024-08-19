package id.dev.post_story.data

import android.location.Location
import id.dev.post_story.domain.LocationDomain

fun Location.toLocationDomain(): LocationDomain {
    return LocationDomain(
        lat = latitude,
        long = longitude,
        location = null
    )
}