package id.dev.favorite.data

import id.dev.core.database.entity.FavoriteEntity
import id.dev.favorite.domain.Favorite

fun FavoriteEntity.toFavorite(): Favorite {
    return Favorite(
        id = id,
        name = name,
        description = description,
        photoUrl = photoUrl,
        createdAt = createdAt,
        lat = lat,
        lon = lon,
        location = location
    )
}