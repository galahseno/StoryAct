@file:OptIn(ExperimentalFoundationApi::class)

package id.dev.post_story.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldState

data class PostStoryState(
    val description: TextFieldState = TextFieldState(),
    val isValidDescription: Boolean = false,
    val imagePath: Uri? = null,
    val selectedImagePath: Uri = Uri.EMPTY,
    val canOpenCamera: Boolean = false,
    val isLocationOn: Boolean = false,
    val isLoading: Boolean = false,
    val canUpload: Boolean = false,
    val location: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
)
