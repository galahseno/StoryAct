package id.dev.core.presentation.design_system

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarItem(
    val title: BottomBarTitle,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasBadge: Boolean,
)

enum class BottomBarTitle {
    MAPS,
    STORY,
    FAVORITE,
    SETTINGS
}
