package com.astronomy.stellar_view.data.local.trivia

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *  La anotación @Database identifica una clase como una base de datos Room
 *  con una tabla (entidad) de la clase QuestionEntity.
 */
@Database(entities = [QuestionEntity::class], version = 1, exportSchema = true)
abstract class QuestionsDatabase : RoomDatabase() {

    // Función abstracta que actúa como un método de acceso a la DAO QuestionsDao.
    abstract fun questionsDao(): QuestionsDao
}
