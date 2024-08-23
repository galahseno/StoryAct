package id.dev.story.presentation.mapper

import androidx.annotation.StringRes
import id.dev.story.presentation.R

enum class StoryFilter(@StringRes val filterName: Int) {
    ALL(R.string.all_stories),
    LOCATION(R.string.location_only)
}