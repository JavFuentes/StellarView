package com.astronomy.stellar_view.domain.repository

import com.astronomy.stellar_view.domain.model.Photo
import com.astronomy.stellar_view.utils.Resource
import kotlinx.coroutines.flow.Flow

// HomeRepository es una interfaz que define las operaciones que pueden realizarse en la pantalla de inicio
interface HomeRepository {

    /**
    *  getTodayPhoto devuelve un Flow que representa la foto del día:
    *  Un Flow es una característica de las coroutines de Kotlin que permite trabajar con datos de transmisión
    *  que se producen de forma asíncrona.
    *  Resource es una clase utilitaria que representa un valor que puede existir o no,
    *  y puede llevar un mensaje de error asociado.
    * */
    fun getTodayPhoto(): Flow<Resource<Photo>>

    /**
     *  getPhotoBasedOnDate devuelve un Flow que representa una foto específica basada en la fecha proporcionada
     *  Nuevamente, el Flow emitirá un Resource, que puede contener una foto, un error, o ambos
     */
    fun getPhotoBasedOnDate(date: String): Flow<Resource<Photo>>

}
