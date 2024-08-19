package id.dev.core.presentation.design_system

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = StoryActOrange,
    background = StoryActDarkGray,
    surface = StoryActDarkBlue,
    secondary = StoryActDarkBlue,
    tertiary = StoryActOrange10,
    primaryContainer = StoryActOrange30,
    onPrimary = StoryActWhite,
    onBackground = StoryActWhite,
    onSurface = StoryActGray,
    onSurfaceVariant = StoryActWhite,
    error = StoryActDarkRed,
    errorContainer = StoryActDarkRed5,
    surfaceTint = StoryActBlack
)

private val LightColorScheme = lightColorScheme(
    primary = StoryActOrange,
    background = StoryActWhite,
    surface = StoryActOrange30,
    secondary = StoryActGray,
    tertiary = StoryActOrange10,
    primaryContainer = StoryActOrange30,
    onPrimary = StoryActBlack,
    onBackground = StoryActBlack,
    onSurface = StoryActDarkBlue,
    onSurfaceVariant = StoryActDarkBlue,
    error = StoryActDarkRed,
    errorContainer = StoryActDarkRed5,
    surfaceTint = StoryActWhite
)

@Composable
fun StoryActTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}