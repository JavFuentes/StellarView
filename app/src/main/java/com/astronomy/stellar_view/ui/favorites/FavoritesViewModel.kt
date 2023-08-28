package com.astronomy.stellar_view.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astronomy.stellar_view.domain.model.Photo
import com.astronomy.stellar_view.domain.scenarios.DeletePhoto
import com.astronomy.stellar_view.domain.scenarios.GetPhotos
import com.astronomy.stellar_view.domain.scenarios.InsertPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    // Inyección de dependencias
    private val deletePhoto: DeletePhoto,  // Caso de uso para eliminar una foto
    private val getPhotos: GetPhotos,  // Caso de uso para recuperar fotos
    private val insertPhoto: InsertPhoto  // Caso de uso para insertar una foto
) : ViewModel() {

    // Estado de las fotos, es un flujo por lo que puede ser recogido y observado para cambios
    // Comenzará de manera perezosa (lazily), es decir, no se iniciará hasta que se recoga (collect)
    val photos = getPhotos().stateIn(
        viewModelScope,  // El ámbito (scope) del ViewModel, se cancelará cuando el ViewModel sea eliminado
        SharingStarted.Lazily,  // Comenzará a compartir el estado cuando se recoga por primera vez y se mantendrá compartiendo hasta que el alcance se cancele
        emptyList()  // Valor inicial
    )

    // Canal interno para eventos de favoritos de la interfaz de usuario, permite enviar eventos a la interfaz de usuario
    private val _favoritesChannel = Channel<UiFavoritesEvent>()

    // Flujo (Flow) a partir del canal de favoritos para que la interfaz de usuario pueda observar y manejar los eventos
    val favoritesEvent = _favoritesChannel.receiveAsFlow()

    // Función para manejar los eventos que vienen de la interfaz de usuario
    fun onEvent(event: FavoritesEvent) {
        when (event) {
            // Cuando se hace clic en el botón de eliminar
            is FavoritesEvent.DeleteButtonClicked -> {

                // Lanzamos una coroutine en el ámbito del ViewModel
                viewModelScope.launch {

                    // Eliminamos la foto
                    deletePhoto(event.photo)

                    // Enviamos un evento para mostrar un mensaje para deshacer la eliminación de la foto
                    _favoritesChannel.send(UiFavoritesEvent.ShowUndoDeletePhotoMessage(event.photo))
                }
            }
            // Cuando una foto eliminada se restaura
            is FavoritesEvent.DeletedPhotoRestored -> {

                // Lanzamos una coroutine en el ámbito del ViewModel
                viewModelScope.launch {

                    // Insertamos la foto de nuevo
                    insertPhoto(event.photo)
                }
            }
        }
    }

    // Eventos de favoritos de la interfaz de usuario
    sealed class UiFavoritesEvent {

        // Evento para mostrar un mensaje para deshacer la eliminación de la foto
        data class ShowUndoDeletePhotoMessage(val photo: Photo): UiFavoritesEvent()
    }
}
