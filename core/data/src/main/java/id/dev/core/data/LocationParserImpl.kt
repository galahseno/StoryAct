package id.dev.core.data

import android.content.Context
import android.location.Geocoder
import android.os.Build
import id.dev.core.domain.story.LocationParser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.util.Locale

class LocationParserImpl(
    private val context: Context
) : LocationParser {
    override suspend fun parseLocation(lat: Double?, lon: Double?): Flow<String?> {
        return channelFlow {
            if (lat == null || lon == null) trySend(null)
            else {
                val geocoder = Geocoder(context, Locale.ROOT)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(lat, lon, 1) { addressList ->

                        val adminArea = addressList.firstOrNull()?.adminArea
                        val countryName = addressList.firstOrNull()?.countryName

                        val result = listOfNotNull(
                            adminArea.takeIf { !it.isNullOrEmpty() },
                            countryName.takeIf { !it.isNullOrEmpty() }
                        ).joinToString(", ")

                        trySend(result)
                    }
                } else {
                    val address = geocoder.getFromLocation(lat, lon, 1)?.toList()

                    address?.let { addressList ->
                        val adminArea = addressList.firstOrNull()?.adminArea
                        val countryName = addressList.firstOrNull()?.countryName

                        val result = listOfNotNull(
                            adminArea.takeIf { !it.isNullOrEmpty() },
                            countryName.takeIf { !it.isNullOrEmpty() }
                        ).joinToString(", ")

                        trySend(result)
                    }
                }
                awaitClose()
            }
        }
    }
}


