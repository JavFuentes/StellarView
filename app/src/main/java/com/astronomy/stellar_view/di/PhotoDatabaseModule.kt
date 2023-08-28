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

@Module
@InstallIn(SingletonComponent::class)
object PhotoDatabaseModule {

    // Proporciona una instancia de FavoritesDatabase
    @Singleton
    @Provides
    fun provideFavoritesDatabase(application: Application): FavoritesDatabase {
        return Room.databaseBuilder(
            application,
            FavoritesDatabase::class.java,
            "favorite_photos_db"
        ).build()
    }

    // Proporciona una instancia de FavoritesRepository
    @Singleton
    @Provides
    fun provideFavoritesRepository(database: FavoritesDatabase): FavoritesRepository {
        return FavoritesRepositoryImpl(database)
    }
}
