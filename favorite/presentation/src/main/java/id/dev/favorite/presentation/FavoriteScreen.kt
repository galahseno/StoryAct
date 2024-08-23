package id.dev.favorite.presentation

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.design_system.StoryActTheme
import id.dev.core.presentation.ui.ObserveAsEvents
import id.dev.core.presentation.ui.story.StoryUi
import id.dev.favorite.presentation.components.FavoriteItem
import id.dev.favorite.presentation.components.SwipeToDeleteContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreenRoot(
    onBackClick: () -> Unit,
    viewModel: FavoriteViewModel = koinViewModel()
) {
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is FavoriteEvent.SuccessDeleteFavorite -> {
                Toast.makeText(context,
                    R.string.success_delete_favorite,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    FavoriteScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is FavoriteAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun FavoriteScreen(
    state: FavoriteState,
    onAction: (FavoriteAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FavoriteActionBar(
            onAction = onAction
        )
        if (state.isLoading) {
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
            if (state.favoriteList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.no_favorite),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp)
                ) {
                    items(state.favoriteList, key = { it.id }) {
                        SwipeToDeleteContainer(
                            item = it,
                            onDelete = { storyUi ->
                                onAction(FavoriteAction.OnFavoriteItemDismiss(storyUi.id))
                            },
                            content = { storyUi ->
                                FavoriteItem(
                                    storyUi = storyUi,
                                    modifier = Modifier
                                        .padding(bottom = 16.dp),
                                )
                            }
                        )

                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteActionBar(
    onAction: (FavoriteAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = {
                onAction(FavoriteAction.OnBackClick)
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 48.dp)
        ) {
            Text(
                text = stringResource(id = R.string.favorite),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FavoriteScreenPreview() {
    StoryActTheme {
        FavoriteScreen(
            state = FavoriteState(
                isLoading = false,
                favoriteList = listOf(
                    StoryUi(
                        createdAt = "2024-08-13T10:02:18.598Z",
                        description = "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        id = "id",
                        lat = 1.0,
                        location = "Indonesia",
                        lon = 1.0,
                        name = "don Joe",
                        photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/small/avatar/dos-c4f65110288cc4fd8d67920393071e5420240717200127.png"
                    ),
                    StoryUi(
                        createdAt = "2024-08-13T10:02:18.598Z",
                        description = "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        id = "id22",
                        lat = 1.0,
                        location = "Indonesia",
                        lon = 1.0,
                        name = "don Joe",
                        photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/small/avatar/dos-c4f65110288cc4fd8d67920393071e5420240717200127.png"
                    )
                )
            ),
            onAction = {}
        )
    }
}