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

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Provee una instancia de APODApi
    @Provides
    @Singleton
    fun provideHomeApi(): APODApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APODApi::class.java)
    }

    // Provee una instancia de HomeRepository
    @Provides
    @Singleton
    fun provideHomeRepository(api: APODApi): HomeRepository {
        return HomeRepositoryImpl(api)
    }
}
