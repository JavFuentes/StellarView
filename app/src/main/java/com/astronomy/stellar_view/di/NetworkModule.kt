package com.astronomy.stellar_view.di

import com.astronomy.stellar_view.data.remote.APODApi
import com.astronomy.stellar_view.domain.repository.HomeRepository
import com.astronomy.stellar_view.domain.repository.HomeRepositoryImpl
import com.astronomy.stellar_view.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Módulo de inyección de dependencias de Dagger Hilt que proporciona componentes
 * de red y repositorios para la comunicación con APIs externas.
 *
 * Este módulo está instalado en [SingletonComponent], garantizando que las
 * instancias de red se mantengan durante todo el ciclo de vida de la aplicación,
 * optimizando el rendimiento y evitando reconexiones innecesarias.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Proporciona una instancia singleton de [APODApi] configurada con Retrofit
     * para consumir la API de NASA Astronomy Picture of the Day.
     *
     * Configuración incluye:
     * - URL base desde [Constants.BASE_URL]
     * - Conversor Gson para serialización/deserialización JSON
     * - Cliente HTTP optimizado para requests astronómicos
     *
     * Funcionalidades principales:
     * - Obtener imagen astronómica del día
     * - Recuperar metadatos de imágenes espaciales
     * - Gestionar requests con datos astronómicos de NASA
     *
     * @return Una instancia singleton de [APODApi] lista para realizar llamadas HTTP
     */
    @Provides
    @Singleton
    fun provideAPODApi(): APODApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APODApi::class.java)
    }

    /**
     * Proporciona una instancia singleton de [HomeRepository] que implementa
     * el patrón Repository para abstrae el acceso a datos de la sección Home.
     *
     * Este repositorio centraliza:
     * - Lógica de acceso a datos astronómicos
     * - Manejo de estados de carga y errores
     * - Cacheo y gestión de datos offline
     * - Transformación de datos de la API a modelos de dominio
     *
     * Beneficios del patrón Repository:
     * - Separación de responsabilidades entre UI y datos
     * - Facilita testing con mocks
     * - Abstracción de la fuente de datos
     * - Punto único de verdad para datos del Home
     *
     * @param api La instancia de [APODApi] inyectada por Hilt
     * @return Una implementación de [HomeRepository] configurada con la API
     */
    @Provides
    @Singleton
    fun provideHomeRepository(api: APODApi): HomeRepository {
        return HomeRepositoryImpl(api)
    }
}