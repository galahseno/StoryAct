@file:OptIn(ExperimentalFoundationApi::class)

package id.dev.post_story.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.LocationOff
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.SubcomposeAsyncImage
import id.dev.core.presentation.design_system.LocationIcon
import id.dev.core.presentation.design_system.StoryActTheme
import id.dev.core.presentation.design_system.components.StoryActActionButton
import id.dev.core.presentation.design_system.components.StoryActOutlinedActionButton
import id.dev.core.presentation.design_system.components.StoryActTextField
import id.dev.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun PostStoryScreenRoot(
    onBackClick: () -> Unit,
    onPostSuccess: () -> Unit,
    viewModel: PostStoryViewModel = koinViewModel()
) {
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PostStoryEvent.Error -> {
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_SHORT).show()
            }

            PostStoryEvent.PostSuccess -> {
                Toast.makeText(
                    context,
                    R.string.post_story_success,
                    Toast.LENGTH_LONG
                ).show()
                onPostSuccess()
            }
        }
    }

    PostStoryScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is PostStoryAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun PostStoryScreen(
    state: PostStoryState,
    onAction: (PostStoryAction) -> Unit
) {
    val context = LocalContext.current

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                state.imagePath?.let {
                    onAction(PostStoryAction.OnTakeCameraAction(it))
                }
            } else {
                onAction(PostStoryAction.OnCancelCameraAction)
            }
        }

    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val imageUri = getImageUri(context)
            onAction(PostStoryAction.OnOpenCameraAction(imageUri))
        }
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                onAction(PostStoryAction.OnSelectGalleryAction(it))
            }
        }

    val requestLocationPermission =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                    onAction(PostStoryAction.OnLocationChange(true))
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                    onAction(PostStoryAction.OnLocationChange(true))
                }
            }
        }

    LaunchedEffect(state.canOpenCamera) {
        if (state.canOpenCamera) {
            state.imagePath?.let { cameraLauncher.launch(it) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        PostImage(
            imagePath = state.selectedImagePath,
            onAction = onAction
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StoryActOutlinedActionButton(
                text = stringResource(id = R.string.camera),
                isLoading = false,
                onClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) -> {
                            val imageUri = getImageUri(context)
                            onAction(PostStoryAction.OnOpenCameraAction(imageUri))
                        }

                        else -> {
                            requestCameraPermission.launch(Manifest.permission.CAMERA)
                        }
                    }
                },
                modifier = Modifier.weight(0.5f)
            )
            Spacer(modifier = Modifier.weight(0.1f))
            StoryActOutlinedActionButton(
                text = stringResource(id = R.string.gallery),
                isLoading = false,
                onClick = {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.weight(0.5f)
            )
        }
        StoryActTextField(
            state = state.description,
            startIcon = null,
            endIcon = null,
            hint = stringResource(id = R.string.description_hint),
            title = stringResource(id = R.string.description),
            modifier = Modifier
                .padding(16.dp),
            textFieldHeight = 120.dp,
            imeAction = ImeAction.Done,
            lineLimits = TextFieldLineLimits.MultiLine(),
            additionalInfo = stringResource(id = R.string.must_not_be_empty),
        )
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.location),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            trailingContent = {
                Switch(
                    checked = state.isLocationOn,
                    thumbContent = {
                        AnimatedContent(
                            targetState = state.isLocationOn,
                            label = "dark_mode_anim"
                        ) {
                            Icon(
                                imageVector = if (it) Icons.Rounded.LocationOn
                                else Icons.Rounded.LocationOff,
                                contentDescription = stringResource(id = R.string.location),
                                tint = MaterialTheme.colorScheme.background
                            )
                        }

                    },
                    onCheckedChange = {
                        if (it) {
                            requestLocationPermission.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        } else onAction(PostStoryAction.OnLocationChange(false))
                    }
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        state.location?.let {
            AnimatedVisibility(
                visible = state.isLocationOn,
                exit = slideOutVertically() + shrinkVertically() + fadeOut(),
                enter = slideInVertically() + expandVertically() + fadeIn()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = LocationIcon,
                        contentDescription = it,
                        modifier = Modifier
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        StoryActActionButton(
            text = stringResource(id = R.string.upload),
            isLoading = state.isLoading,
            enabled = state.canUpload && !state.isLoading,
            onClick = {
                onAction(PostStoryAction.OnUploadClick)
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun PostImage(
    imagePath: Uri,
    onAction: (PostStoryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        SubcomposeAsyncImage(
            model = if (imagePath != Uri.EMPTY) imagePath.getImageBitmap(context)
            else id.dev.core.presentation.design_system.R.drawable.profile,
            contentDescription = imagePath.toString(),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(300.dp, 350.dp)
                .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = id.dev.core.presentation.ui.R.string.error_couldnt_load_image),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        PostHeader(
            name = stringResource(id = R.string.post_story),
            onAction = onAction
        )
    }
}

@Composable
private fun BoxScope.PostHeader(
    name: String,
    onAction: (PostStoryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .padding(16.dp),
    ) {
        IconButton(
            onClick = {
                onAction(PostStoryAction.OnBackClick)
            },
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBackIosNew,
                contentDescription = stringResource(id = R.string.back),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        Box(
            modifier = Modifier
                .align(CenterVertically)
                .padding(end = 40.dp)
        ) {
            Text(
                text = name.replaceFirstChar { it.uppercase() },
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium.copy(
                    drawStyle = Stroke(
                        width = 2f,
                        cap = StrokeCap.Round
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = name.replaceFirstChar { it.uppercase() },
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.headlineMedium.copy(
                    drawStyle = Fill
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PostStoryScreenPreview() {
    StoryActTheme {
        PostStoryScreen(
            state = PostStoryState(
                imagePath = null,
                location = "Indonesia",
                isLocationOn = true
            ),
            onAction = {}
        )
    }
}