package com.astronomy.stellar_view.di

import com.astronomy.stellar_view.domain.repository.FavoritesRepository
import com.astronomy.stellar_view.domain.scenarios.DeletePhoto
import com.astronomy.stellar_view.domain.scenarios.GetPhotos
import com.astronomy.stellar_view.domain.scenarios.InsertPhoto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ScenarioModule {

    // Provee una instancia de GetPhotos
    @Provides
    fun provideGetPhotosUseCase(repository: FavoritesRepository): GetPhotos {
        return GetPhotos(repository)
    }

    // Provee una instancia de InsertPhoto
    @Provides
    fun provideInsertPhotoUseCase(repository: FavoritesRepository): InsertPhoto {
        return InsertPhoto(repository)
    }

    // Provee una instancia de DeletePhoto
    @Provides
    fun provideDeletePhotoUseCase(repository: FavoritesRepository): DeletePhoto {
        return DeletePhoto(repository)
    }
}
