/*
MIT License

Copyright (c) 2023 Javier Fuentes

Se concede permiso, de forma gratuita, a cualquier persona que obtenga una copia de este software y de los archivos de documentación asociados (el "Software"), para tratar el Software sin restricción, incluyendo, sin limitación, los derechos de uso, copia, modificación, fusión, publicación, distribución, sublicencia y/o venta de copias del Software, y para permitir a las personas a las que se les proporcione el Software hacerlo, sujeto a las siguientes condiciones:

El aviso de derechos de autor anterior y este aviso de permisos se incluirán en todas las copias o partes sustanciales del Software.

EL SOFTWARE SE PROPORCIONA "TAL CUAL", SIN GARANTÍA DE NINGÚN TIPO, EXPRESA O IMPLÍCITA, INCLUYENDO PERO NO LIMITADO A LAS GARANTÍAS DE COMERCIABILIDAD, IDONEIDAD PARA UN PROPÓSITO PARTICULAR Y NO INFRACCIÓN. EN NINGÚN CASO LOS AUTORES O TITULARES DE LOS DERECHOS DE AUTOR SERÁN RESPONSABLES POR CUALQUIER RECLAMO, DAÑOS U OTRAS RESPONSABILIDADES, YA SEA EN UNA ACCIÓN DE CONTRATO, AGRAVIO O DE OTRO MODO, DERIVADAS DE, FUERA DE O EN CONEXIÓN CON EL SOFTWARE O EL USO O OTROS NEGOCIOS EN EL SOFTWARE.

Para obtener más información sobre el autor y sus proyectos, visite http://javierfuentes.dev
*/

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
