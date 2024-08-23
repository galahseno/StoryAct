package id.dev.favorite.favorite_feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitcompat.SplitCompat
import id.dev.core.presentation.design_system.StoryActTheme
import id.dev.favorite.data.di.favoriteDataModule
import id.dev.favorite.favorite_feature.di.favoriteActivityModule
import id.dev.favorite.presentation.FavoriteScreenRoot
import id.dev.favorite.presentation.di.favoriteViewModelModule
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadKoinModules(
            listOf(
                favoriteActivityModule,
                favoriteDataModule,
                favoriteViewModelModule,
            )
        )
        SplitCompat.installActivity(this)

        setContent {
            val favoriteActivityViewModel = koinViewModel<FavoriteActivityViewModel>()

            StoryActTheme(
                favoriteActivityViewModel.state.isDarkMode
            ) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "favorite_dashboard"
                ) {
                    composable("favorite_dashboard") {
                        FavoriteScreenRoot(
                            onBackClick = { finish() }
                        )
                    }
                }
            }
        }
    }
}