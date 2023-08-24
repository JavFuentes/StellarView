package com.astronomy.stellar_view.data.local.trivia

import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Indica a Hilt que esta clase es un módulo que proporcionará dependencias.
@Module
@InstallIn(SingletonComponent::class)
object TriviaDatabaseModule {

    @Provides
    @Singleton
    // La función proporciona una instancia de QuestionsDatabase.
    fun provideDatabase(@ApplicationContext appContext: Context): QuestionsDatabase {

        //Queda un registro de que la base de datos fue creada correctamente
        Log.d("Database", "Database being created")

        return Room.databaseBuilder(
            appContext,
            QuestionsDatabase::class.java,
            "questions_db.db"
        ).build()
    }

    @Provides
    fun provideQuestionsDao(database: QuestionsDatabase): QuestionsDao {

        // La función proporciona una instancia de QuestionsDao.
        return database.questionsDao()
    }
}
