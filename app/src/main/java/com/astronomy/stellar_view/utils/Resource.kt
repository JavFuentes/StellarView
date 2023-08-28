package com.astronomy.stellar_view.utils

/**
 *  Resource es una clase sellada que puede representar diferentes estados de una operación,
 *  con diferentes tipos de datos asociados a cada estado.
 * */

sealed class Resource<T>(
    // 'data' es el valor principal que puede existir o no dependiendo del resultado de la operación
    val data: T? = null,
    // 'message' es un mensaje adicional que puede existir en caso de error
    val message: String? = null
) {
    // Loading es un estado de la operación que indica que la operación está en progreso
    // Puede contener datos parciales
    class Loading<T>(data: T? = null): Resource<T>(data)

    // Success es un estado de la operación que indica que la operación se completó con éxito
    // Debe contener los datos resultantes de la operación
    class Success<T>(data: T?): Resource<T>(data)

    // Error es un estado de la operación que indica que la operación terminó en error
    // Puede contener datos parciales y debe contener un mensaje de error
    class Error<T>(data: T? = null, message: String?): Resource<T>(data, message)
}
