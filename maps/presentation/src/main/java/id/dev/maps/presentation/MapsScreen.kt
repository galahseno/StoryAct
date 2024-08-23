package id.dev.maps.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.ui.ObserveAsEvents
import id.dev.maps.presentation.components.LocationMap
import id.dev.maps.presentation.components.MapsUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapsScreenRoot(

    viewModel: MapsViewModel = koinViewModel()
) {
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MapsEvent.Error -> {
                Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }
    }

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
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LocationMap(
            markers = state.listMarker,
            isDarkMode = state.isDarkMode,
            isLoading = state.isLoading,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.suggested_story),
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.SemiBold
        )
        LazyHorizontalGrid(
            modifier = Modifier
                .fillMaxHeight(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            rows = GridCells.Fixed(3)
        ) {
            items(state.listMarker, key = { it.id }) {
                SuggestedStory(
                    mapsUi = it,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            onAction(MapsAction.OnStoryClick(it.id))
                        }
                )
            }
        }
    }
}

@Composable
private fun SuggestedStory(
    mapsUi: MapsUi,
    modifier: Modifier = Modifier
) {
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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = avatarPicture),
            contentDescription = mapsUi.name,
            modifier = Modifier
                .size(48.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = mapsUi.name.replaceFirstChar { it.uppercase() })
    }
}