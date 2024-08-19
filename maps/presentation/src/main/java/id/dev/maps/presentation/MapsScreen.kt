package id.dev.maps.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import id.dev.core.presentation.design_system.StoryActTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapsScreenRoot(

    viewModel: MapsViewModel = koinViewModel()
) {
    MapsScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun MapsScreen(
    state: MapsState,
    onAction: (MapsAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Under development",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MapsScreenPreview() {
    StoryActTheme {
        MapsScreen(
            state = MapsState(),
            onAction = {}
        )
    }
}