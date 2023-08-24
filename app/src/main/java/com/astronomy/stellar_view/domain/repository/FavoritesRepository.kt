package com.astronomy.stellar_view.domain.repository

import com.astronomy.stellar_view.domain.model.Photo
import kotlinx.coroutines.flow.Flow

// FavoritesRepository es una interfaz que define las operaciones que pueden realizarse en los favoritos
interface FavoritesRepository {

   /**
    *   getPhotos devuelve un Flow de una lista de Photos.
    *   Un Flow es una característica de las coroutines de Kotlin que permite trabajar con datos de transmisión
    *   que se producen de forma asíncrona.
    *   En este caso, siempre que haya una nueva lista de fotos, el Flow la emitirá.
    */
    fun getPhotos(): Flow<List<Photo>>

   /**
    *   insertPhoto inserta una foto en los favoritos.
    *   Esta función es suspendida porque la inserción puede ser una operación que lleva tiempo
    *   y no debe realizarse en el hilo principal.
    * */
    suspend fun insertPhoto(photo: Photo)

  /**
   *    deletePhoto elimina una foto de los favoritos.
   *    Esta función es suspendida porque la eliminación puede ser una operación que lleva tiempo
   *    y no debe realizarse en el hilo principal.
   */
    suspend fun deletePhoto(photo: Photo)
}
