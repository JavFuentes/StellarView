package com.astronomy.stellar_view.domain.model

/**
 *  Photo es una data class, que es una clase especial en Kotlin diseñada para contener solo datos.
 *  Se utiliza para representar la información de una foto en el dominio de la aplicación, es decir,
 *  en el código que no está directamente relacionado con la base de datos o la API.
 */

data class Photo(
    // La fecha a la que corresponde la foto
    val date: String,
    // La explicación o descripción de la foto
    val explanation: String,
    // La URL de la foto en alta definición, si está disponible. Este campo es opcional, por lo que puede ser null
    val hdurl: String?,
    // El título de la foto
    val title: String,
    // La URL de la foto. Este campo es opcional, por lo que puede ser null
    val url: String?
)
