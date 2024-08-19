package id.dev.favorite.presentation

interface FavoriteAction {
    data object OnBackClick : FavoriteAction
    data class OnFavoriteItemDismiss(val id: String): FavoriteAction
}