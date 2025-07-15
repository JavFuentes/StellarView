package com.astronomy.stellar_view.di

import android.app.Application
import androidx.room.Room
import com.astronomy.stellar_view.data.local.photo.FavoritesDatabase
import com.astronomy.stellar_view.domain.repository.FavoritesRepository
import com.astronomy.stellar_view.domain.repository.FavoritesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de inyección de dependencias de Dagger Hilt que proporciona componentes
 * para el almacenamiento local de fotos favoritas usando Room Database.
 *
 * Este módulo está instalado en [SingletonComponent], asegurando que la base de
 * datos local y sus repositorios mantengan el mismo ciclo de vida que la aplicación,
 * garantizando persistencia de datos y rendimiento óptimo.
 */
@Module
@InstallIn(SingletonComponent::class)
object PhotoDatabaseModule {

    /**
     * Nombre de la base de datos local para fotos favoritas.
     * Se utiliza para crear el archivo de base de datos SQLite en el dispositivo.
     */
    private const val FAVORITES_DATABASE_NAME = "favorite_photos_db"

    /**
     * Proporciona una instancia singleton de [FavoritesDatabase] configurada con Room
     * para el almacenamiento persistente de fotos astronómicas favoritas.
     *
     * Características de la base de datos:
     * - Almacenamiento local usando SQLite
     * - Persistencia de fotos marcadas como favoritas
     * - Acceso rápido sin necesidad de conexión a internet
     * - Gestión automática de esquemas y migraciones
     *
     * Funcionalidades principales:
     * - Guardar fotos astronómicas favoritas del usuario
     * - Recuperar lista de favoritos para visualización offline
     * - Eliminar fotos de la lista de favoritos
     * - Búsqueda y filtrado de fotos guardadas
     *
     * @param application La instancia de [Application] inyectada por Hilt
     * @return Una instancia singleton de [FavoritesDatabase] configurada y lista para usar
     */
    @Singleton
    @Provides
    fun provideFavoritesDatabase(application: Application): FavoritesDatabase {
        return Room.databaseBuilder(
            application,
            FavoritesDatabase::class.java,
            FAVORITES_DATABASE_NAME
        ).build()
    }

    /**
     * Proporciona una instancia singleton de [FavoritesRepository] que implementa
     * el patrón Repository para abstraer el acceso a datos de fotos favoritas.
     *
     * Este repositorio centraliza:
     * - Operaciones CRUD sobre fotos favoritas
     * - Lógica de negocio para gestión de favoritos
     * - Transformación entre entidades de base de datos y modelos de dominio
     * - Manejo de errores en operaciones de base de datos
     *
     * Beneficios del patrón Repository:
     * - Abstracción completa de la fuente de datos local
     * - Facilita testing unitario con mocks
     * - Separación clara entre lógica de datos y presentación
     * - Punto único de acceso para operaciones de favoritos
     * - Facilita futuras migraciones o cambios de base de datos
     *
     * Operaciones disponibles:
     * - Agregar foto a favoritos
     * - Eliminar foto de favoritos
     * - Obtener todas las fotos favoritas
     * - Verificar si una foto está en favoritos
     *
     * @param database La instancia de [FavoritesDatabase] inyectada por Hilt
     * @return Una implementación de [FavoritesRepository] configurada con la base de datos
     */
    @Singleton
    @Provides
    fun provideFavoritesRepository(database: FavoritesDatabase): FavoritesRepository {
        return FavoritesRepositoryImpl(database)
    }
}