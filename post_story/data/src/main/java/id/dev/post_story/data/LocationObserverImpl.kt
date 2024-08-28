package id.dev.post_story.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import id.dev.post_story.domain.LocationDomain
import id.dev.post_story.domain.LocationObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationObserverImpl(
    private val context: Context
) : LocationObserver {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    override fun getLocation(): Flow<LocationDomain> {
        return callbackFlow {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                close()
                return@callbackFlow
            } else {
                client.lastLocation.addOnSuccessListener {
                    it?.let { location ->
                        trySend(location.toLocationDomain())
                    }
                }

                val request = LocationRequest
                    .Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                    .build()

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        result.locations.lastOrNull()?.let { location ->
                            trySend(location.toLocationDomain())
                        }
                    }
                }

                client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

                awaitClose {
                    client.removeLocationUpdates(locationCallback)
                }
            }
        }
    }
}