package id.dev.storyact.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import id.dev.auth.presentation.intro.IntroScreenRoot
import id.dev.auth.presentation.login.LoginScreenRoot
import id.dev.auth.presentation.register.RegisterScreenRoot
import id.dev.core.presentation.design_system.components.StoryActBottomBar
import id.dev.core.presentation.design_system.components.StoryActFab
import id.dev.maps.presentation.MapsScreenRoot
import id.dev.post_story.presentation.PostStoryScreenRoot
import id.dev.profile.presentation.ProfileScreenRoot
import id.dev.story.presentation.ui.detail_story.DetailStoryScreenRoot
import id.dev.story.presentation.ui.detail_story.DetailStoryViewModel
import id.dev.story.presentation.ui.stories.StoryScreenRoot
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean,
    onFavoriteClick: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentParentRoute = navBackStackEntry?.destination?.parent?.route
    val currentRoute = navBackStackEntry?.destination?.route

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (currentParentRoute == Screen.Home.route && currentRoute != Screen.DetailStory.route
                && currentRoute != Screen.PostStory.route
            ) {
                StoryActBottomBar(
                    selectedItemIndex = selectedItemIndex,
                    onSelectedItemIndex = {
                        if (it != 2) {
                            selectedItemIndex = it
                        }
                    },
                    onStoryClick = {
                        if (currentRoute == Screen.Story.route) return@StoryActBottomBar
                        navController.navigate(Screen.Story.route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                                inclusive = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    onMapsClick = {
                        if (currentRoute == Screen.Maps.route) return@StoryActBottomBar
                        navController.navigate(Screen.Maps.route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                                inclusive = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    onFavoriteClick = onFavoriteClick,
                    onSettingsClick = {
                        if (currentRoute == Screen.Profile.route) return@StoryActBottomBar
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                                inclusive = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (currentParentRoute == Screen.Home.route && currentRoute != Screen.DetailStory.route
                && currentRoute != Screen.PostStory.route
            ) {
                StoryActFab(
                    onFabClick = { navController.navigate(Screen.PostStory.route) },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Screen.Home.route else Screen.Auth.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            authGraph(navController)
            storyGraph(navController)
        }
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Intro.route,
        route = Screen.Auth.route
    ) {
        composable(route = Screen.Intro.route) {
            IntroScreenRoot(
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                onSignInClick = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }
        composable(route = Screen.SignUp.route) {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.SignUp.route) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }
        composable(Screen.SignIn.route) {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route) {
                        popUpTo(Screen.SignIn.route) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.storyGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = Screen.Story.route,
        route = Screen.Home.route
    ) {
        composable(
            Screen.Story.route,
        ) {
            StoryScreenRoot(
                onStoryClick = {
                    navController.navigate(Screen.DetailStory.createRoute(it))
                }
            )
        }
        composable(
            route = Screen.DetailStory.route,
            arguments = listOf(
                navArgument(Screen.DetailStory.DETAIL_STORY_ARG) {
                    type = NavType.StringType
                    defaultValue = "-1"
                }
            )
        ) {
            val storyId = it.arguments?.getString(Screen.DetailStory.DETAIL_STORY_ARG) ?: "-1"
            val viewModel: DetailStoryViewModel = koinViewModel(
                parameters = { parametersOf(storyId) }
            )

            DetailStoryScreenRoot(
                viewModel = viewModel,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = Screen.Maps.route
        ) {
            MapsScreenRoot()
        }
        composable(
            route = Screen.PostStory.route
        ) {
            PostStoryScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                },
                onPostSuccess = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = Screen.Profile.route
        ) {
            ProfileScreenRoot(
                onLogoutSuccess = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}