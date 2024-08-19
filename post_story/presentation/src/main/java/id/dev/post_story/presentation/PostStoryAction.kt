package id.dev.post_story.presentation

import android.net.Uri

interface PostStoryAction {
    data class OnOpenCameraAction(val imageUri: Uri) : PostStoryAction
    data class OnTakeCameraAction(val capturedImagePath: Uri) : PostStoryAction
    data object OnCancelCameraAction: PostStoryAction
    data class OnSelectGalleryAction(val selectedImagePath: Uri) : PostStoryAction
    data class OnLocationChange(val isLocationOn: Boolean): PostStoryAction
    data object OnUploadClick: PostStoryAction
    data object OnBackClick: PostStoryAction
}