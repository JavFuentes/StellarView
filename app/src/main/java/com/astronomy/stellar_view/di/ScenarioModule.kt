package com.astronomy.stellar_view.di

import com.astronomy.stellar_view.domain.repository.FavoritesRepository
import com.astronomy.stellar_view.domain.scenarios.DeletePhoto
import com.astronomy.stellar_view.domain.scenarios.GetPhotos
import com.astronomy.stellar_view.domain.scenarios.InsertPhoto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Módulo de inyección de dependencias de Dagger Hilt que proporciona casos de uso
 * (Use Cases/Scenarios) siguiendo los principios de Clean Architecture.
 *
 * Este módulo está instalado en [ViewModelComponent], lo que significa que las
 * instancias de casos de uso tienen el mismo ciclo de vida que los ViewModels,
 * optimizando el uso de memoria y garantizando limpieza automática.
 *
 * Los casos de uso encapsulan la lógica de negocio específica de la aplicación
 * y actúan como intermediarios entre los ViewModels y los repositorios.
 */
@Module
@InstallIn(ViewModelComponent::class)
object ScenarioModule {

    /**
     * Proporciona una instancia de [GetPhotos] para recuperar fotos favoritas.
     *
     * Este caso de uso encapsula la lógica para:
     * - Obtener la lista completa de fotos favoritas del usuario
     * - Transformar datos del repositorio a modelos de presentación
     * - Manejar estados de carga y errores
     * - Aplicar filtros o ordenamientos si es necesario
     *
     * Usado principalmente en:
     * - Pantalla de favoritos para mostrar la galería
     * - Verificación de existencia de fotos específicas
     * - Sincronización de datos entre pantallas
     *
     * @param repository La instancia de [FavoritesRepository] inyectada por Hilt
     * @return Una instancia de [GetPhotos] configurada con el repositorio
     */
    @Provides
    fun provideGetPhotosUseCase(repository: FavoritesRepository): GetPhotos {
        return GetPhotos(repository)
    }

    /**
     * Proporciona una instancia de [InsertPhoto] para agregar fotos a favoritos.
     *
     * Este caso de uso encapsula la lógica para:
     * - Validar datos de la foto antes de insertar
     * - Prevenir duplicados en la lista de favoritos
     * - Transformar modelos de dominio a entidades de base de datos
     * - Manejar errores de inserción y conflictos
     * - Ejecutar validaciones de negocio específicas
     *
     * Usado principalmente en:
     * - Botón "Agregar a favoritos" en detalles de foto
     * - Funcionalidad de marcado rápido desde listas
     * - Importación masiva de favoritos
     *
     * @param repository La instancia de [FavoritesRepository] inyectada por Hilt
     * @return Una instancia de [InsertPhoto] configurada con el repositorio
     */
    @Provides
    fun provideInsertPhotoUseCase(repository: FavoritesRepository): InsertPhoto {
        return InsertPhoto(repository)
    }

    /**
     * Proporciona una instancia de [DeletePhoto] para eliminar fotos de favoritos.
     *
     * Este caso de uso encapsula la lógica para:
     * - Validar que la foto existe antes de eliminar
     * - Confirmar permisos de eliminación del usuario
     * - Manejar errores de eliminación y casos edge
     * - Limpiar recursos asociados si es necesario
     * - Actualizar contadores y estadísticas
     *
     * Usado principalmente en:
     * - Botón "Eliminar de favoritos" en detalles de foto
     * - Funcionalidad de eliminación desde la galería de favoritos
     * - Limpieza masiva de favoritos
     * - Gestión de espacio de almacenamiento
     *
     * @param repository La instancia de [FavoritesRepository] inyectada por Hilt
     * @return Una instancia de [DeletePhoto] configurada con el repositorio
     */
    @Provides
    fun provideDeletePhotoUseCase(repository: FavoritesRepository): DeletePhoto {
        return DeletePhoto(repository)
    }
}