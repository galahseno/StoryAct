package id.dev.storyact

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import id.dev.core.presentation.design_system.StoryActTheme
import id.dev.storyact.navigation.NavigationRoot
import id.dev.storyact.presentation.MainAction
import id.dev.storyact.presentation.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private lateinit var splitInstallManager: SplitInstallManager

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 500L
                    doOnEnd { screen.remove() }
                }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 500L
                    doOnEnd { screen.remove() }
                }

                zoomX.start()
                zoomY.start()
            }
        }
        enableEdgeToEdge()

        splitInstallManager = SplitInstallManagerFactory.create(applicationContext)

        setContent {
            StoryActTheme(
                viewModel.state.isDarkMode
            ) {

                LaunchedEffect(key1 = viewModel.state.isDarkMode) {
                    if (viewModel.state.isDarkMode) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavigationRoot(
                        navController = navController,
                        isLoggedIn = viewModel.state.isLoggedIn,
                        onFavoriteClick = {
                            installOrStartFavoriteFeature()
                        }
                    )
                }
            }
        }
    }

    private fun installOrStartFavoriteFeature() {
        if (splitInstallManager.installedModules.contains("favorite_feature")) {
            Intent()
                .setClassName(
                    packageName,
                    "id.dev.favorite.favorite_feature.FavoriteActivity"
                )
                .also(::startActivity)
            return
        }

        val request = SplitInstallRequest.newBuilder()
            .addModule("favorite_feature")
            .build()
        splitInstallManager
            .startInstall(request)
            .addOnFailureListener {
                Timber.d(it.localizedMessage)
                Toast.makeText(
                    applicationContext,
                    R.string.error_couldnt_load_module,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}