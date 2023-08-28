package com.astronomy.stellar_view.data.local.trivia

import androidx.room.Dao
import androidx.room.Query

/**
 *  Definiendo la interfaz del Objeto de Acceso a Datos (DAO) para la tabla de preguntas.
 *  El DAO es una parte de la biblioteca Room que proporciona una capa de abstracción sobre SQLite
 *  para permitir un acceso más robusto a la base de datos mientras se aprovecha todo el poder de SQLite.
 *
 */

@Dao
interface QuestionsDao {

    /**
     *  Definiendo una función suspendida que permite pausar y reanudar, permitiendo que tareas de larga duración
     *  como las operaciones de la base de datos se ejecuten sin bloquear el hilo principal.

     *  Esta función seleccionará aleatoriamente una pregunta de la base de datos donde
     *  la categoría y el idioma coincidan con los parámetros dados.

     *  La anotación Query define la sentencia SQL para la función.
     */

    @Query("SELECT * FROM questions WHERE category = :category AND language = :language AND id_question NOT IN (:answeredQuestionIds) ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuestion(category: String, language: String, answeredQuestionIds: List<Int>): QuestionEntity

}
