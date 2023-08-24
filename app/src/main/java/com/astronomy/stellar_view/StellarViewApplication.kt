package com.astronomy.stellar_view

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import java.io.FileOutputStream


/**
 *  La anotación @HiltAndroidApp se usa en la clase de aplicación para habilitar la inyección de dependencias
 *  en todo el proyecto a través de Hilt
 */
@HiltAndroidApp
class StellarViewApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Llama a la función que copia la base de datos de la carpeta 'assets' a la carpeta de bases de datos de la aplicación.
        copyDatabaseFromAssets(this)
    }

    /**
     * Copia una base de datos SQLite de la carpeta de 'assets' a la carpeta de bases de datos de la aplicación.
     *
     * @param context Contexto de la aplicación.
     */

    private fun copyDatabaseFromAssets(context: Context) {
        // Consigue el archivo de la base de datos en la carpeta de bases de datos de la aplicación.
        val dbFile = context.getDatabasePath("questions_db.db")

        // Comprueba si el archivo de la base de datos ya existe.
        if (!dbFile.exists()) {
            // Abre el archivo de la base de datos en la carpeta 'assets'.
            val inputStream = context.assets.open("questions_db.db")

            // Consigue el camino completo al archivo de la base de datos en la carpeta de bases de datos de la aplicación.
            val outputFilePath = dbFile.path

            // Crea un nuevo stream de salida para escribir los datos al archivo de la base de datos.
            val outputStream = FileOutputStream(outputFilePath)

            // Copia los datos del stream de entrada al stream de salida.
            inputStream.copyTo(outputStream)

            // Cierra los streams de entrada y salida.
            inputStream.close()
            outputStream.flush()
            outputStream.close()

            // Escribe un mensaje en el log para indicar que la base de datos ha sido copiada con éxito.
            Log.d("StellarViewApplication", "Database copied successfully")
        }
    }
}
