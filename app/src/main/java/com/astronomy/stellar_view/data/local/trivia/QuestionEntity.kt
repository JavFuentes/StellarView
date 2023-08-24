package com.astronomy.stellar_view.data.local.trivia

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  La anotación @Entity designa esta data class como una entidad en la base de datos SQLite.
 *  El parámetro tableName define el nombre de la tabla donde se almacenarán las instancias de esta
 *  entidad en la base de datos.
 */

@Entity(tableName = "questions")
data class QuestionEntity(

    // El campo id_question con @PrimaryKey.
    @PrimaryKey
    val id_question: Int,

    // Los siguientes campos representan las columnas de la tabla en la base de datos.
    val question: String, // columna para la pregunta
    val opt1: String,     // columna para la opción 1 de respuesta (Corresponde a la respuesta correcta)
    val opt2: String,     // columna para la opción 2 de respuesta
    val opt3: String,     // columna para la opción 3 de respuesta
    val opt4: String,     // columna para la opción 4 de respuesta
    val category: String, // columna para la categoría de la pregunta
    val level: Int,       // columna para el nivel de la pregunta
    val language: String  // columna para el idioma de la pregunta
)
