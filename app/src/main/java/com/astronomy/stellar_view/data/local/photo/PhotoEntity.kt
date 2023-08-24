package com.astronomy.stellar_view.data.local.photo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  La anotación @Entity designa esta data class como una entidad en la base de datos SQLite.
 *  El parámetro tableName define el nombre de la tabla donde se almacenarán las instancias de esta
 *  entidad en la base de datos.
 */

@Entity(tableName = "favorite_photos_db")
data class PhotoEntity(
    // @PrimaryKey define que este campo es la clave primaria en la tabla de la base de datos.
    // En este caso, la fecha se usa como clave primaria.
    @PrimaryKey
    val date: String,

    // Los siguientes campos representan las columnas en la tabla de la base de datos.
    // Cada instancia de PhotoEntity tendrá estos valores almacenados en la tabla correspondiente de la base de datos.
    val explanation: String,
    val hdurl: String?, // Esta y la próxima URL pueden ser nulas, lo que indica que el campo puede estar vacío en la base de datos.
    val title: String,
    val url: String?
)
