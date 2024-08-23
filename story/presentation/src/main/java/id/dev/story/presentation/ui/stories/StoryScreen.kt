@file:OptIn(ExperimentalFoundationApi::class)

package id.dev.story.presentation.ui.stories

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import id.dev.core.presentation.design_system.BookmarkIcon
import id.dev.core.presentation.design_system.CommentIcon
import id.dev.core.presentation.design_system.LikeIcon
import id.dev.core.presentation.design_system.LocationIcon
import id.dev.core.presentation.design_system.StoryActTheme
import id.dev.core.presentation.design_system.components.StoryActActionButton
import id.dev.core.presentation.ui.ObserveAsEvents
import id.dev.core.presentation.ui.asUiText
import id.dev.core.presentation.ui.story.StoryUi
import id.dev.core.presentation.ui.story.calculateDurationBetweenNow
import id.dev.core.presentation.ui.story.formatNumber
import id.dev.story.domain.parseError
import id.dev.story.presentation.R
import id.dev.story.presentation.mapper.StoryFilter
import id.dev.story.presentation.ui.components.StoryImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun StoryScreenRoot(
    onStoryClick: (String) -> Unit,
    viewModel: StoryViewModel = koinViewModel()
) {
    val storyPaging = viewModel.story.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is StoryEvent.Error -> {
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_SHORT).show()
            }

            StoryEvent.SuccessAddFavorite -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.success_add_favorite),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    StoryScreen(
        onAction = { action ->
            when (action) {
                is StoryAction.OnStoryClick -> onStoryClick(action.id)
                else -> Unit
            }
            viewModel.onAction(action)
        },
        state = state,
        storyPaging = storyPaging,
    )
}

@Composable
private fun StoryScreen(
    onAction: (StoryAction) -> Unit,
    state: StoryState,
    storyPaging: LazyPagingItems<StoryUi>
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = !listState.isScrollInProgress,
            exit = slideOutVertically() + shrinkVertically() + fadeOut(),
            enter = slideInVertically() + expandVertically() + fadeIn()
        ) {
            StoryFilter(
                selectedFilter = state.selectedFilter,
                onAction = { action ->
                    onAction(action)
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            )
        }
        if (storyPaging.loadState.refresh is LoadState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp)
            ) {
                items(
                    count = storyPaging.itemCount,
                    key = storyPaging.itemKey { it.id }
                ) {
                    storyPaging[it]?.let { storyUi ->
                        StoryItem(
                            modifier = Modifier
                                .padding(bottom = 16.dp),
                            storyUi = storyUi,
                            onAction = onAction
                        )
                    }
                }
                storyPaging.apply {
                    when (loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        is LoadState.Error -> {
                            val error =
                                (storyPaging.loadState.append as? LoadState.Error)?.error?.message

                            item {
                                ErrorState(
                                    message = error?.parseError()?.asUiText()?.asString(),
                                    onClick = { retry() }
                                )
                            }
                        }

                        is LoadState.NotLoading -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorState(
    message: String?,
    onClick: () -> Unit
) {
    Text(
        text = message
            ?: stringResource(id.dev.core.presentation.ui.R.string.error_couldnt_load_item),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(8.dp))
    StoryActActionButton(
        text = stringResource(id = R.string.retry),
        isLoading = false,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
private fun StoryFilter(
    modifier: Modifier = Modifier,
    selectedFilter: StoryFilter,
    onAction: (StoryAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StoryFilter.entries.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onAction(StoryAction.OnFilterChange(filter)) },
                label = {
                    Text(text = stringResource(id = filter.filterName))
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(35.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun StoryItem(
    storyUi: StoryUi,
    onAction: (StoryAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDropdown by remember {
        mutableStateOf(false)
    }

    Box {
        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = {
                showDropdown = false
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .align(Alignment.TopEnd)
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.favorite))
                },
                onClick = {
                    showDropdown = false
                    onAction(StoryAction.OnFavoriteClick(storyUi))
                }
            )
        }

        Card(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .combinedClickable(
                    onClick = {
                        onAction(StoryAction.OnStoryClick(storyUi.id))
                    },
                    onLongClick = {
                        showDropdown = true
                    }
                ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                StoryHeader(name = storyUi.name, createdAt = storyUi.createdAt)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = storyUi.description.replaceFirstChar { it.uppercase() },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(12.dp))
                StoryImage(
                    photoUrl = storyUi.photoUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(15.dp)
                        )
                )
                Spacer(modifier = Modifier.height(12.dp))
                storyUi.location?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
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
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StoryFooter(
                        icon = LikeIcon
                    )
                    StoryFooter(
                        icon = CommentIcon
                    )
                    StoryFooter(
                        icon = BookmarkIcon
                    )
                }
            }
        }
    }
}


@Composable
private fun StoryHeader(
    name: String,
    createdAt: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val avatarPicture = rememberSaveable {
        listOf(
            id.dev.core.presentation.design_system.R.drawable.avatar1,
            id.dev.core.presentation.design_system.R.drawable.avatar2,
            id.dev.core.presentation.design_system.R.drawable.avatar3,
            id.dev.core.presentation.design_system.R.drawable.avatar4,
            id.dev.core.presentation.design_system.R.drawable.avatar5,
        ).random()
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = avatarPicture),
            contentDescription = name,
            modifier = Modifier
                .size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = name.replaceFirstChar { it.uppercase() },
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(150.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = createdAt.calculateDurationBetweenNow(context),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun StoryFooter(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val totalCount = rememberSaveable {
        (1..50000).random()
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = totalCount.formatNumber(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StoryScreenPreview() {
    StoryActTheme {
        StoryItem(
            storyUi = StoryUi(
                createdAt = "2024-08-13T10:02:18.598Z",
                description = "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                id = "id",
                lat = 1.0,
                location = "Indonesia",
                lon = 1.0,
                name = "don Joe",
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/small/avatar/dos-c4f65110288cc4fd8d67920393071e5420240717200127.png"
            ),
            onAction = {}
        )
    }
}