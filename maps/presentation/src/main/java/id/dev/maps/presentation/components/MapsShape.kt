package id.dev.maps.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.design_system.StoryActTheme

class MapsShape(
    private val cornerRadius: Dp = 25.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadius = with(density) { cornerRadius.toPx() }

        val path = Path().apply {
            lineTo(
                x = 0f,
                y = size.height - (cornerRadius * 2)
            )
            arcTo(
                rect = Rect(
                    offset = Offset(0f, size.height - cornerRadius * 3),
                    Size(cornerRadius * 2, cornerRadius * 2)
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = -90f,
                forceMoveTo = false
            )
            arcTo(
                rect = Rect(
                    offset = Offset(
                        size.width - cornerRadius * 2,
                        size.height - cornerRadius * 2
                    ),
                    Size(cornerRadius * 2, cornerRadius * 2)
                ),
                startAngleDegrees = -270f,
                sweepAngleDegrees = -90f,
                forceMoveTo = false
            )
            lineTo(
                x = size.width,
                y = 0f
            )
            close()
        }

        return Outline.Generic(path)
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewMapShape() {
    StoryActTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .clip(MapsShape())
                .size(400.dp)
                .background(MaterialTheme.colorScheme.onBackground)
        ) {

        }
    }
}