package com.astronomy.stellar_view.di

import com.astronomy.stellar_view.data.FirebaseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de inyección de dependencias de Dagger Hilt que proporciona servicios
 * relacionados con Firebase para toda la aplicación.
 *
 * Este módulo está instalado en [SingletonComponent], garantizando que las
 * dependencias de Firebase mantengan el mismo ciclo de vida que la aplicación
 * y se reutilicen eficientemente.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Proporciona una instancia singleton de [FirebaseHandler] para manejar
     * todas las operaciones de Firebase en la aplicación.
     *
     * El [FirebaseHandler] centraliza las siguientes operaciones:
     * - Autenticación de usuarios
     * - Gestión de puntuaciones en Firestore
     * - Sincronización de datos del usuario
     * - Operaciones CRUD en la base de datos remota
     *
     * Al ser un Singleton, garantiza:
     * - Una sola conexión activa con Firebase
     * - Reutilización eficiente de recursos
     * - Consistencia en el estado de autenticación
     * - Mejor rendimiento al evitar múltiples instancias
     *
     * @return Una instancia singleton de [FirebaseHandler] lista para usar
     */
    @Provides
    @Singleton
    fun provideFirebaseHandler(): FirebaseHandler {
        return FirebaseHandler()
    }
}