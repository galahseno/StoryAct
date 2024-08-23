package id.dev.widget.presentation.ui

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.Alignment
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import id.dev.core.presentation.ui.story.StoryUi
import id.dev.core.presentation.ui.story.calculateDurationBetweenNow

@Composable
fun StoryContent(
    storyUi: StoryUi,
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current
    var storyImage by remember(storyUi.photoUrl) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(storyUi.photoUrl) {
        storyImage = context.parseImageUrl(storyUi.photoUrl)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        storyImage?.let {
            Image(
                provider = ImageProvider(it),
                contentDescription = "Image from Picsum Photos",
                contentScale = ContentScale.FillBounds,
                modifier = GlanceModifier.size(24.dp).cornerRadius(8.dp)
            )
        } ?: CircularProgressIndicator(
            color = GlanceTheme.colors.onBackground
        )
        Spacer(modifier = GlanceModifier.width(24.dp))
        Text(
            text = storyUi.name.replaceFirstChar { it.uppercase() },
            maxLines = 1,
            style = TextStyle(
                color = GlanceTheme.colors.onBackground
            )
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Text(
            text = storyUi.createdAt.calculateDurationBetweenNow(context),
            maxLines = 1,
            style = TextStyle(
                color = GlanceTheme.colors.onBackground
            )
        )
    }
}