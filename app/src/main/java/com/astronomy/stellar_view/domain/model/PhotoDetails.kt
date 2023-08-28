package com.astronomy.stellar_view.domain.model

// PhotoDetails es una data class que representa la información detallada de una foto
data class PhotoDetails(
    // Derechos de autor de la foto
    val copyright: String,
    // La fecha de la foto
    val date: String,
    // La explicación o descripción de la foto
    val explanation: String,
    // La URL de la foto en alta definición, si está disponible. Este campo es opcional, por lo que puede ser null
    val hdurl: String?,
    // El tipo de medio de la foto (por ejemplo, imagen o vídeo)
    val mediaType: String,
    // La versión del servicio que proporcionó la foto
    val serviceVersion: String,
    // El título de la foto
    val title: String,
    // La URL de la foto. Este campo es opcional, por lo que puede ser null
    val url: String?
) {

    // Función que convierte un objeto PhotoDetails en un objeto Photo
    fun toPhotoDomain(): Photo {
        return Photo(
            date = date,
            explanation = explanation,
            hdurl = hdurl,
            title = title,
            url = url
        )
    }
}
