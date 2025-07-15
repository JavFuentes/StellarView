package com.astronomy.stellar_view.data.mapper

import com.astronomy.stellar_view.data.local.photo.PhotoEntity
import com.astronomy.stellar_view.domain.model.Photo


/**
 * Las funciones de extensión son una característica de Kotlin que te permite extender una clase
 * con nueva funcionalidad sin tener que heredar de la clase.
 * Puedes pensar en ellas como una forma de añadir métodos a las clases existentes.
 *
 * El repository aprovecha estas funciones para  para convertir una Photo de dominio a una PhotoEntity
 * antes de insertarla en tu base de datos. Y también para realizar el proceso inverso.
 * */

// Función de extensión que convierte una instancia de PhotoEntity a Photo del dominio
fun PhotoEntity.toPhotoDomain(): Photo {
    return Photo(
        date = date, // Asigna el valor del campo 'date' de PhotoEntity al campo 'date' de Photo
        explanation = explanation, // Asigna el valor del campo 'explanation' de PhotoEntity al campo 'explanation' de Photo
        hdurl = hdurl, // Asigna el valor del campo 'hdurl' de PhotoEntity al campo 'hdurl' de Photo
        title = title, // Asigna el valor del campo 'title' de PhotoEntity al campo 'title' de Photo
        url = url // Asigna el valor del campo 'url' de PhotoEntity al campo 'url' de Photo
    )
}

// Función de extensión que convierte una instancia de Photo a PhotoEntity de la capa de datos
fun Photo.toPhotoEntity(): PhotoEntity {
    return PhotoEntity(
        date = date, // Asigna el valor del campo 'date' de Photo al campo 'date' de PhotoEntity
        explanation = explanation, // Asigna el valor del campo 'explanation' de Photo al campo 'explanation' de PhotoEntity
        hdurl = hdurl, // Asigna el valor del campo 'hdurl' de Photo al campo 'hdurl' de PhotoEntity
        title = title, // Asigna el valor del campo 'title' de Photo al campo 'title' de PhotoEntity
        url = url // Asigna el valor del campo 'url' de Photo al campo 'url' de PhotoEntity
    )
}
