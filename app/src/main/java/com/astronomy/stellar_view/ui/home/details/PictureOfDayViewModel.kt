package com.astronomy.stellar_view.ui.home.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.astronomy.stellar_view.R
import com.astronomy.stellar_view.domain.model.Photo
import com.astronomy.stellar_view.domain.repository.HomeRepository
import com.astronomy.stellar_view.domain.scenarios.InsertPhoto
import com.astronomy.stellar_view.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PictureOfDayViewModel @Inject constructor(
    // Inyección de dependencias
    application: Application,
    private val repository: HomeRepository,
    private val insertPhoto: InsertPhoto
) : AndroidViewModel(application) {

    // LiveData para la foto del día actual
    private val _todayPhoto = MutableLiveData<Photo>()
    val todayPhoto: LiveData<Photo>
        get() = _todayPhoto

    // LiveData para la foto basada en una fecha específica
    private val _photoBasedOnDate = MutableLiveData<Photo>()
    val photoBasedOnDate: LiveData<Photo>
        get() = _photoBasedOnDate

    // Canal para enviar eventos relacionados con la foto del día a la capa de presentación
    private val _todayPhotoChannel = Channel<UiTodayPhotoEvent>()
    val todayPhotoEvent = _todayPhotoChannel.receiveAsFlow()

    init {
        getTodayPhoto()
    }

    // Obtener la foto de hoy
    private fun getTodayPhoto() {
        viewModelScope.launch {
            repository.getTodayPhoto().onEach { event ->
                when (event) {
                    is Resource.Loading -> {
                        _todayPhotoChannel.send(UiTodayPhotoEvent.ShowProgressBar(true)) // Evento: Mostrar barra de progreso
                    }

                    is Resource.Success -> {
                        event.data?.let { photo ->
                            _todayPhoto.postValue(photo)
                            _todayPhotoChannel.send(UiTodayPhotoEvent.ShowProgressBar(false)) // Evento: Ocultar barra de progreso
                        }
                    }

                    is Resource.Error -> {
                        _todayPhotoChannel.send(UiTodayPhotoEvent.ShowSnackbar("${event.message}")) // Evento: Mostrar Snackbar con mensaje de error
                        _todayPhotoChannel.send(UiTodayPhotoEvent.ShowProgressBar(false)) // Evento: Ocultar barra de progreso
                    }
                }
            }.launchIn(this)
        }
    }

    // Obtener la foto basada en la fecha especificada
    internal fun getPhotoBasedOnDate(date: String) {
        viewModelScope.launch {
            repository.getPhotoBasedOnDate(date).onEach { event ->
                when (event) {
                    is Resource.Loading -> {
                        _todayPhotoChannel.send(UiTodayPhotoEvent.ShowProgressBar(true)) // Evento: Mostrar barra de progreso
                    }
                    is Resource.Success -> {
                        event.data?.let { photo ->
                            _photoBasedOnDate.postValue(photo)
                            _todayPhotoChannel.send(UiTodayPhotoEvent.ShowProgressBar(false)) // Evento: Ocultar barra de progreso
                        }
                    }
                    is Resource.Error -> {
                        _todayPhotoChannel.send(UiTodayPhotoEvent.ShowSnackbar("${event.message}")) // Evento: Mostrar Snackbar con mensaje de error
                        _todayPhotoChannel.send(UiTodayPhotoEvent.ShowProgressBar(false)) // Evento: Ocultar barra de progreso
                    }
                }
            }.launchIn(this)
        }
    }

    // Manejar los eventos relacionados con la foto del día
    fun onEvent(event: PictureOfDayEvent) {
        when (event) {
            is PictureOfDayEvent.AddFavoritesClicked -> {
                viewModelScope.launch {
                    insertPhoto(event.photo)
                    val successMessage = getApplication<Application>().resources.getString(R.string.successfully_added_alert)
                    _todayPhotoChannel.send(UiTodayPhotoEvent.ShowSnackbar(successMessage)) // Evento: Mostrar Snackbar con mensaje de éxito
                }
            }
        }
    }

    // Eventos relacionados con la UI para la foto del día
    sealed class UiTodayPhotoEvent {
        data class ShowProgressBar(val isLoading: Boolean) : UiTodayPhotoEvent() // Evento: Mostrar u ocultar la barra de progreso
        data class ShowSnackbar(val message: String) : UiTodayPhotoEvent() // Evento: Mostrar Snackbar con mensaje
    }
}
