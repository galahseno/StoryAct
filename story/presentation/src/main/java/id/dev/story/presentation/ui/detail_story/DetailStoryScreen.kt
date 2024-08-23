package id.dev.story.presentation.ui.detail_story

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.design_system.FavoriteFilledIcon
import id.dev.core.presentation.design_system.FavoriteOutlinedIcon
import id.dev.core.presentation.design_system.LocationIcon
import id.dev.core.presentation.design_system.StoryActTheme
import id.dev.core.presentation.ui.ObserveAsEvents
import id.dev.core.presentation.ui.story.calculateDurationBetweenNow
import id.dev.story.presentation.R
import id.dev.story.presentation.ui.components.StoryImage

@Composable
fun DetailStoryScreenRoot(
    onBackClick: () -> Unit,
    viewModel: DetailStoryViewModel
) {
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is DetailStoryEvent.Error -> {
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_SHORT).show()
            }

            is DetailStoryEvent.SuccessAddOrFavorite -> {
                Toast.makeText(
                    context, if (event.isFavorite)
                        context.getString(R.string.add_to_favorite) else
                        context.getString(R.string.remove_from_favorite),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    DetailStoryScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is DetailStoryAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun DetailStoryScreen(
    state: DetailStoryState,
    onAction: (DetailStoryAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            StoryImage(
                photoUrl = state.photoUrl,
                modifier = Modifier.fillMaxHeight(0.7f)
            )
            StoryHeader(
                name = state.name,
                isFavorite = state.isFavorite,
                onAction = onAction
            )
            SheetContent(
                state = state
            )
        }
    }
}

@Composable
private fun BoxScope.StoryHeader(
    name: String,
    isFavorite: Boolean,
    onAction: (DetailStoryAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            onClick = {
                onAction(DetailStoryAction.OnBackClick)
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
        ) {
            Text(
                text = name.replaceFirstChar { it.uppercase() },
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium.copy(
                    drawStyle = Stroke(
                        width = 2f,
                        cap = StrokeCap.Round
                    )
                )
            )
            Text(
                text = name.replaceFirstChar { it.uppercase() },
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.headlineMedium.copy(
                    drawStyle = Fill
                )
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        IconButton(
            onClick = {
                onAction(DetailStoryAction.OnFavoriteClick)
            },
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Icon(
                imageVector = if (isFavorite) FavoriteFilledIcon else FavoriteOutlinedIcon,
                contentDescription = stringResource(id = R.string.back),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun BoxScope.SheetContent(
    state: DetailStoryState
) {
    val context = LocalContext.current

    var expand by rememberSaveable {
        mutableStateOf(false)
    }
    var size by rememberSaveable {
        mutableIntStateOf(275)
    }
    val animatedSize by animateDpAsState(targetValue = size.dp, label = "size")

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .height(animatedSize)
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .clickable {
                if (expand) {
                    size = 275
                    expand = false
                } else {
                    size = 550
                    expand = true
                }
            },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Spacer(
                modifier = Modifier
                    .height(4.dp)
                    .fillMaxWidth(0.25f)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.background)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                state.location?.let {
                    Row {
                        Icon(
                            imageVector = LocationIcon,
                            contentDescription = it,
                            modifier = Modifier
                                .size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            modifier = Modifier.width(200.dp),
                            text = it,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                } ?: Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.align(CenterVertically),
                    text = state.createdAt.calculateDurationBetweenNow(context),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.description.replaceFirstChar { it.uppercase() },
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailStoryScreenPreview() {
    StoryActTheme {
        DetailStoryScreen(state = DetailStoryState(
            isLoading = false,
            isFavorite = true,
            name = "don story",
            location = "Indonesia",
            createdAt = "2024-08-13T10:02:18.598Z",
            description = "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        ), onAction = {})
    }
}