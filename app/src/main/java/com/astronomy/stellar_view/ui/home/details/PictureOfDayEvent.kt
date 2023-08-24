package com.astronomy.stellar_view.ui.home.details

import com.astronomy.stellar_view.domain.model.Photo

/**
 *  PictureOfDayEvent es una clase sellada que representa diferentes eventos que pueden ocurrir
 *  en la interfaz de usuario de la "Imagen del día"
 * */
sealed class PictureOfDayEvent {

    // AddFavoritesClicked representa el evento en el que el usuario hace clic en el botón de agregar a favoritos
    // Este evento lleva la foto que el usuario desea agregar a favoritos
    data class AddFavoritesClicked(val photo: Photo) : PictureOfDayEvent()
}
