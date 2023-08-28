package com.astronomy.stellar_view.data.remote

import com.astronomy.stellar_view.utils.Constants.API_KEY
import com.astronomy.stellar_view.domain.model.PhotoDetails
import retrofit2.http.GET
import retrofit2.http.Query

// APODApi es una interfaz de servicio de Retrofit que define los endpoints de la API a los que puedes hacer llamadas
interface APODApi {

    /**
     *  El endpoint para obtener la foto del día:
     * @GET define el tipo de solicitud HTTP a hacer (en este caso, GET).
     * El valor que toma es el endpoint al que debe hacer la llamada.
     * Esta función es suspendida porque las llamadas a la API son operaciones de red que pueden
     * tomar tiempo y no deben hacerse en el hilo principal.
     */

    @GET("planetary/apod?api_key=${API_KEY}")
    suspend fun getTodayPhoto(): PhotoDetails

    /**
    *  El endpoint para obtener una foto basada en la fecha:
    *  @Query es una anotación que define un parámetro de consulta para la URL del endpoint.
    *  API_KEY es el valor por defecto para el parámetro de consulta apiKey, por lo que no tienes
     * que pasar ese parámetro al hacer la llamada a esta función a menos que quieras usar una key diferente.
    */

    @GET("planetary/apod")
    suspend fun getPhotoBasedOnDate(
        @Query("date") date: String,
        @Query("api_key") apiKey: String = API_KEY
    ): PhotoDetails
}
