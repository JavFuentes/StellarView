package com.astronomy.stellar_view.domain.repository

import com.astronomy.stellar_view.data.local.photo.FavoritesDatabase
import com.astronomy.stellar_view.data.local.toPhotoDomain
import com.astronomy.stellar_view.data.local.toPhotoEntity
import com.astronomy.stellar_view.domain.model.Photo
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val db: FavoritesDatabase
) : FavoritesRepository {

    private val dao = db.dao

    // Obtener fotos desde la base de datos y mapearlas al modelo de dominio
    override fun getPhotos() = dao.getPhotos().map { it.map { photos -> photos.toPhotoDomain() } }

    // Insertar una foto en la base de datos
    override suspend fun insertPhoto(photo: Photo) = dao.insertPhoto(photo.toPhotoEntity())

    // Eliminar una foto de la base de datos
    override suspend fun deletePhoto(photo: Photo) = dao.deletePhoto(photo.toPhotoEntity())
}
