package com.astronomy.stellar_view.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de inyección de dependencias de Dagger Hilt que proporciona instancias
 * compartidas en toda la aplicación.
 *
 * Este módulo está instalado en [SingletonComponent], lo que significa que las
 * dependencias provistas tendrán el mismo ciclo de vida que la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Nombre del archivo de preferencias compartidas utilizado para almacenar
     * datos persistentes relacionados con el sistema de trivia.
     */
    private const val TRIVIA_PREFERENCES_NAME = "TriviaPreferences"

    /**
     * Proporciona una instancia singleton de SharedPreferences para el almacenamiento
     * persistente de datos de la aplicación.
     *
     * Esta instancia se utiliza principalmente para:
     * - Guardar categorías de trivia persistentes
     * - Almacenar puntuaciones y progreso del usuario
     * - Mantener configuraciones específicas del juego
     *
     * @param appContext El contexto de la aplicación inyectado por Hilt
     * @return Una instancia de SharedPreferences configurada en modo privado
     */
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext appContext: Context
    ): SharedPreferences {
        return appContext.getSharedPreferences(
            TRIVIA_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }
}