package id.dev.profile.presentation

import android.content.res.Configuration
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.design_system.StoryActTheme
import id.dev.core.presentation.design_system.components.StoryActActionButton
import id.dev.core.presentation.ui.ObserveAsEvents
import id.dev.core.presentation.ui.formatNumber
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreenRoot(
    onLogoutSuccess: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ProfileEvent.LogoutSuccess -> {
                Toast.makeText(
                    context,
                    R.string.you_are_logged_out,
                    Toast.LENGTH_LONG
                ).show()
                onLogoutSuccess()
            }
        }
    }

    ProfileScreenRootScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is ProfileAction.OnChangeThemes -> {
                    if (action.value) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun ProfileScreenRootScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ProfileHeader(
            isLoading = state.isLoading,
            onAction = onAction
        )
        Text(
            text = state.name.replaceFirstChar { it.uppercase() },
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = state.email,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProfileFooter()
        Spacer(modifier = Modifier.height(8.dp))
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.dark_mode),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            trailingContent = {
                Switch(
                    checked = state.isDarkMode,
                    thumbContent = {
                        AnimatedContent(
                            targetState = state.isDarkMode,
                            label = "dark_mode_anim"
                        ) {
                            Icon(
                                imageVector = if (it) Icons.Rounded.Check
                                else Icons.Rounded.Clear,
                                contentDescription = stringResource(id = R.string.check),
                                tint = MaterialTheme.colorScheme.background
                            )
                        }

                    },
                    onCheckedChange = {
                        onAction(ProfileAction.OnChangeThemes(it))
                    }
                )
            }
        )
    }
}

@Composable
private fun ProfileHeader(
    isLoading: Boolean,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.35f)
    ) {
        Image(
            painter = painterResource(id = id.dev.core.presentation.design_system.R.drawable.profile),
            contentDescription = stringResource(id = R.string.profile),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
            contentScale = ContentScale.FillBounds
        )
        StoryActActionButton(
            text = stringResource(id = R.string.logout),
            isLoading = isLoading,
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .align(Alignment.BottomCenter),
            enabled = !isLoading,
            onClick = {
                onAction(ProfileAction.OnLogoutClick)
            }
        )
    }
}

@Composable
fun ProfileFooter(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileStatus(
            stringId = R.string.post
        )
        Spacer(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight(0.07f)
                .background(MaterialTheme.colorScheme.onBackground)
        )
        ProfileStatus(
            stringId = R.string.follower
        )
        Spacer(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight(0.07f)
                .background(MaterialTheme.colorScheme.onBackground)
        )
        ProfileStatus(
            stringId = R.string.following
        )
    }
}

@Composable
private fun ProfileStatus(
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val totalCount = rememberSaveable {
        (1..50000).random()
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = totalCount.formatNumber(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(id = stringId),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileScreenRootScreenPreview() {
    StoryActTheme {
        ProfileScreenRootScreen(
            state = ProfileState(
                name = "reviewer",
                email = "reviewer@rev.id"
            ),
            onAction = {}
        )
    }
}