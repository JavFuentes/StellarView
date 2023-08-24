package com.astronomy.stellar_view.domain.repository

import com.astronomy.stellar_view.data.remote.APODApi
import com.astronomy.stellar_view.domain.model.Photo
import com.astronomy.stellar_view.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryImpl(
    private val api: APODApi
) : HomeRepository {

    override fun getTodayPhoto(): Flow<Resource<Photo>> = flow {
        // Emite el estado de carga mientras se realiza la solicitud
        emit(Resource.Loading())

        try {
            // Emite el resultado exitoso con los datos convertidos al modelo de dominio
            emit(Resource.Success(api.getTodayPhoto().toPhotoDomain()))
        } catch (e: Exception) {
            // Emite el estado de error en caso de excepción con el mensaje correspondiente
            emit(Resource.Error(message = e.message))
        }
    }

    override fun getPhotoBasedOnDate(date: String): Flow<Resource<Photo>> = flow {
        // Emite el estado de carga mientras se realiza la solicitud
        emit(Resource.Loading())

        try {
            // Emite el resultado exitoso con los datos convertidos al modelo de dominio
            emit(Resource.Success(api.getPhotoBasedOnDate(date).toPhotoDomain()))
        } catch (e: Exception) {
            // Emite el estado de error en caso de excepción con el mensaje correspondiente
            emit(Resource.Error(message = e.message))
        }
    }
}
