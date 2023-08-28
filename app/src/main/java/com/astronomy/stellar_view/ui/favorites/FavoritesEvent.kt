package com.astronomy.stellar_view.ui.favorites

import com.astronomy.stellar_view.domain.model.Photo

sealed class FavoritesEvent {

    // Esta subclase representa el evento de que el botón de borrar ha sido presionado.
    data class DeleteButtonClicked(val photo: Photo) : FavoritesEvent()

    // Esta subclase representa el evento de que una foto previamente borrada ha sido restaurada.
    data class DeletedPhotoRestored(val photo: Photo): FavoritesEvent()
}
