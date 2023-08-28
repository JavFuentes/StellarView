package com.astronomy.stellar_view.domain.scenarios

import com.astronomy.stellar_view.domain.model.Photo
import com.astronomy.stellar_view.domain.repository.FavoritesRepository

// Define el caso de uso para insertar una foto en los favoritos.
class InsertPhoto(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke(photo: Photo) = repository.insertPhoto(photo)
}
