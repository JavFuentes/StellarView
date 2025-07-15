package com.astronomy.stellar_view.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.astronomy.stellar_view.data.local.trivia.QuestionsDao
import com.astronomy.stellar_view.data.local.trivia.QuestionsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de inyección de dependencias de Dagger Hilt que proporciona componentes
 * para el almacenamiento local de preguntas de trivia astronómica usando Room Database.
 *
 * Este módulo está instalado en [SingletonComponent], garantizando que la base de
 * datos de trivia y sus DAOs mantengan el mismo ciclo de vida que la aplicación,
 * asegurando acceso consistente a las preguntas durante toda la sesión del usuario.
 */
@Module
@InstallIn(SingletonComponent::class)
object TriviaDatabaseModule {

    /**
     * Nombre del archivo de base de datos para preguntas de trivia.
     * Se almacena localmente en el dispositivo para acceso offline.
     */
    private const val TRIVIA_DATABASE_NAME = "questions_db.db"

    /**
     * Tag para logging de operaciones de base de datos de trivia.
     */
    private const val DATABASE_LOG_TAG = "TriviaDatabase"

    /**
     * Proporciona una instancia singleton de [com.astronomy.stellar_view.data.local.trivia.QuestionsDatabase] configurada con Room
     * para el almacenamiento local de preguntas de trivia astronómica.
     *
     * Características de la base de datos:
     * - Almacenamiento local de preguntas pre-cargadas
     * - Acceso rápido sin necesidad de conexión a internet
     * - Soporte para múltiples categorías de trivia astronómica
     * - Preguntas en múltiples idiomas (ES, IT, EN)
     *
     * Funcionalidades principales:
     * - Almacenar banco de preguntas por categorías temáticas
     * - Recuperar preguntas aleatorias por categoría e idioma
     * - Gestionar dificultad y metadatos de preguntas
     * - Tracking de preguntas ya respondidas por el usuario
     *
     * Categorías incluidas:
     * - Sistema Solar y planetas
     * - Estrellas y constelaciones
     * - Galaxias y cosmología
     * - Exploración espacial
     * - Astronomía observacional
     * - Historia de la astronomía
     *
     * @param appContext El contexto de la aplicación inyectado por Hilt
     * @return Una instancia singleton de [com.astronomy.stellar_view.data.local.trivia.QuestionsDatabase] configurada y lista para usar
     */
    @Provides
    @Singleton
    fun provideQuestionsDatabase(@ApplicationContext appContext: Context): QuestionsDatabase {
        Log.d(DATABASE_LOG_TAG, "Initializing trivia questions database")

        return Room.databaseBuilder(
            appContext,
            QuestionsDatabase::class.java,
            TRIVIA_DATABASE_NAME
        ).build()
    }

    /**
     * Proporciona una instancia de [com.astronomy.stellar_view.data.local.trivia.QuestionsDao] para realizar operaciones
     * de acceso a datos sobre las preguntas de trivia.
     *
     * Este DAO (Data Access Object) proporciona:
     * - Métodos de consulta optimizados para preguntas
     * - Filtrado por categoría, idioma y dificultad
     * - Selección aleatoria de preguntas no respondidas
     * - Tracking de progreso y estadísticas del usuario
     *
     * Operaciones principales:
     * - Obtener pregunta aleatoria por categoría e idioma
     * - Marcar preguntas como respondidas
     * - Contar preguntas disponibles por categoría
     * - Resetear progreso para nuevas partidas
     * - Consultar estadísticas de rendimiento
     *
     * Beneficios del patrón DAO:
     * - Abstracción de consultas SQL complejas
     * - Type-safety en las operaciones de base de datos
     * - Optimización automática de consultas por Room
     * - Facilita testing con implementaciones mock
     *
     * @param database La instancia de [QuestionsDatabase] inyectada por Hilt
     * @return Una instancia de [com.astronomy.stellar_view.data.local.trivia.QuestionsDao] configurada para operaciones de trivia
     */
    @Provides
    fun provideQuestionsDao(database: QuestionsDatabase): QuestionsDao {
        Log.d(DATABASE_LOG_TAG, "Providing QuestionsDao instance")
        return database.questionsDao()
    }
}