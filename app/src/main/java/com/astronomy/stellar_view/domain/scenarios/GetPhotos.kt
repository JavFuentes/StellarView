package com.astronomy.stellar_view.domain.scenarios

import com.astronomy.stellar_view.domain.repository.FavoritesRepository

// Define el caso de uso para obtener una foto de los favoritos.
class GetPhotos(
    private val repository: FavoritesRepository
) {
    operator fun invoke() = repository.getPhotos()
}