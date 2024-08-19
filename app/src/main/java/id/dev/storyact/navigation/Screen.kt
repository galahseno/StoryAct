package id.dev.storyact.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Intro : Screen("auth/intro")
    data object SignIn : Screen("auth/Sign_in")
    data object SignUp : Screen("auth/Sign_up")

    data object Home : Screen("home")
    data object Story : Screen("home/story")
    data object DetailStory : Screen("home/story/{storyId}") {
        const val DETAIL_STORY_ARG = "storyId"
        fun createRoute(storyId: String) = "home/story/$storyId"
    }
    data object Maps : Screen("home/maps")
    data object Profile : Screen("home/profile")
    data object PostStory : Screen("home/post_story")
    data object Favorite : Screen("home/favorite")
}