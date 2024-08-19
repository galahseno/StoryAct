package id.dev.post_story.domain

import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    fun getLocation(): Flow<LocationDomain>
}