package com.astronomy.stellar_view.data.local.photo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    // Obtener todas las fotos favoritas
    @Query("SELECT * FROM favorite_photos_db")
    fun getPhotos(): Flow<List<PhotoEntity>>

    // Eliminar una foto de la lista de favoritos
    @Delete
    suspend fun deletePhoto(photoEntity: PhotoEntity)

    // Insertar o reemplazar una foto en la lista de favoritos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photoEntity: PhotoEntity)
}
