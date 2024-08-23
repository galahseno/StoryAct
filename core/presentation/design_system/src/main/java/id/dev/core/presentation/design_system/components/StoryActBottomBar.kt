package id.dev.core.presentation.design_system.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.design_system.BottomBarItem
import id.dev.core.presentation.design_system.FavoriteFilledIcon
import id.dev.core.presentation.design_system.FavoriteOutlinedIcon
import id.dev.core.presentation.design_system.MapFilledIcon
import id.dev.core.presentation.design_system.MapOutlinedIcon
import id.dev.core.presentation.design_system.R
import id.dev.core.presentation.design_system.SettingFilledIcon
import id.dev.core.presentation.design_system.SettingOutlinedIcon
import id.dev.core.presentation.design_system.StoryActTheme
import id.dev.core.presentation.design_system.StoryFilledIcon
import id.dev.core.presentation.design_system.StoryOutlinedIcon

@Composable
fun StoryActBottomBar(
    selectedItemIndex: Int,
    onSelectedItemIndex:(Int) -> Unit,
    onStoryClick: () -> Unit,
    onMapsClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomBarItem(
            title = R.string.story,
            selectedIcon = StoryFilledIcon,
            unselectedIcon = StoryOutlinedIcon,
            hasBadge = false,
        ),
        BottomBarItem(
            title = R.string.maps,
            selectedIcon = MapFilledIcon,
            unselectedIcon = MapOutlinedIcon,
            hasBadge = false,
        ),
        BottomBarItem(
            title = R.string.favorite,
            selectedIcon = FavoriteFilledIcon,
            unselectedIcon = FavoriteOutlinedIcon,
            hasBadge = false,
        ),
        BottomBarItem(
            title = R.string.settings,
            selectedIcon = SettingFilledIcon,
            unselectedIcon = SettingOutlinedIcon,
            hasBadge = true,
        )
    )


    NavigationBar(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),
        tonalElevation = 0.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.primary
                ),
                selected = selectedItemIndex == index,
                onClick = {
                    onSelectedItemIndex(index)
                    when (item.title) {
                        R.string.maps -> onMapsClick()
                        R.string.story -> onStoryClick()
                        R.string.favorite -> onFavoriteClick()
                        R.string.settings -> onSettingsClick()
                    }
                },
                label = {
                    Text(text = stringResource(id = item.title))
                },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.hasBadge) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = stringResource(id = item.title),
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }
            )
            if (item.title == R.string.maps) {
                Spacer(modifier = Modifier.width(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StoryActBottomBarPreview() {
    StoryActTheme {
        StoryActBottomBar(
            selectedItemIndex = 0,
            onSelectedItemIndex = {},
            onMapsClick = {},
            onStoryClick = {},
            onFavoriteClick = {},
            onSettingsClick = {}
        )
    }
}