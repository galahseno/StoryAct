package id.dev.core.presentation.design_system.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.design_system.StoryActTheme

@Composable
fun StoryActFab(
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onFabClick,
        shape = CircleShape,
        modifier = modifier
            .size(80.dp)
            .offset(y = 45.dp),
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        Image(
            painter = painterResource(id = id.dev.core.presentation.design_system.R.drawable.app_icon),
            contentDescription = null,
            modifier = Modifier.size(45.dp)
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StoryActFabPreview() {
    StoryActTheme {
        StoryActFab(
            onFabClick = { /*TODO*/ },
            modifier = Modifier
                .offset(y = (-45).dp)
        )
    }
}