package id.dev.maps.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import id.dev.core.presentation.design_system.LocationIcon
import id.dev.maps.presentation.MapsAction
import id.dev.maps.presentation.R

@Composable
fun LocationMap(
    markers: List<MapsUi>,
    isDarkMode: Boolean,
    isLoading: Boolean,
    onAction: (MapsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        // position to indonesia
        position = CameraPosition.fromLatLngZoom(LatLng(-2.2436434, 113.096937), 3.4f)
    }

    val mapStyle = rememberSaveable {
        MapStyleOptions.loadRawResourceStyle(
            context,
            if (isDarkMode) R.raw.maps_dark_style else R.raw.maps_light_style
        )
    }

    LaunchedEffect(markers) {
        markers.firstOrNull { it.isSelected }?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        it.lat ?: 0.0,
                        it.lon ?: 0.0
                    ), 10f
                )
            )
        }
    }

    Box(
        modifier = modifier
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .clip(MapsShape()),
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
            properties = MapProperties(
                mapStyleOptions = mapStyle
            ),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                onAction(MapsAction.OnStoryClick("-1"))
            }
        ) {
            markers.forEach { story ->
                val icon = remember(story.isSelected) {
                    bitmapDescriptorFromVector(
                        context,
                        if (story.isSelected) id.dev.core.presentation.design_system.R.drawable.marker_active
                        else id.dev.core.presentation.design_system.R.drawable.marker
                    )
                }

                val markerState = rememberMarkerState(
                    position = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
                )

                LaunchedEffect(key1 = story.isSelected) {
                    if (story.isSelected) {
                        markerState.showInfoWindow()
                    }
                }
                Marker(
                    state = markerState,
                    title = story.name,
                    snippet = story.description,
                    onClick = {
                        it.showInfoWindow()
                        onAction(MapsAction.OnStoryClick(story.id))
                        true
                    },
                    icon = icon
                )
            }
        }
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (markers.firstOrNull { it.isSelected } != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = LocationIcon,
                        contentDescription = stringResource(id = R.string.location),
                        modifier = Modifier
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Location",
                        modifier = Modifier
                    )
                    Text(
                        text = markers.firstOrNull { it.isSelected }?.location ?: "",
                        modifier = Modifier
                    )
                }
            }
        }

    }
}