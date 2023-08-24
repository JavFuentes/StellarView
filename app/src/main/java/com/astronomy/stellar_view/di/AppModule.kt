package com.astronomy.stellar_view.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Función que proporciona una instancia de SharedPreferences
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext
        appContext: Context
    ): SharedPreferences {
        // Devuelve una instancia de SharedPreferences con el nombre "TriviaPreferences" en modo privado
        return appContext.getSharedPreferences("TriviaPreferences", Context.MODE_PRIVATE)
    }
}
