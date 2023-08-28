/*
MIT License

Copyright (c) 2023 Javier Fuentes

Se concede permiso, de forma gratuita, a cualquier persona que obtenga una copia de este software y de los archivos de documentación asociados (el "Software"), para tratar el Software sin restricción, incluyendo, sin limitación, los derechos de uso, copia, modificación, fusión, publicación, distribución, sublicencia y/o venta de copias del Software, y para permitir a las personas a las que se les proporcione el Software hacerlo, sujeto a las siguientes condiciones:

El aviso de derechos de autor anterior y este aviso de permisos se incluirán en todas las copias o partes sustanciales del Software.

EL SOFTWARE SE PROPORCIONA "TAL CUAL", SIN GARANTÍA DE NINGÚN TIPO, EXPRESA O IMPLÍCITA, INCLUYENDO PERO NO LIMITADO A LAS GARANTÍAS DE COMERCIABILIDAD, IDONEIDAD PARA UN PROPÓSITO PARTICULAR Y NO INFRACCIÓN. EN NINGÚN CASO LOS AUTORES O TITULARES DE LOS DERECHOS DE AUTOR SERÁN RESPONSABLES POR CUALQUIER RECLAMO, DAÑOS U OTRAS RESPONSABILIDADES, YA SEA EN UNA ACCIÓN DE CONTRATO, AGRAVIO O DE OTRO MODO, DERIVADAS DE, FUERA DE O EN CONEXIÓN CON EL SOFTWARE O EL USO O OTROS NEGOCIOS EN EL SOFTWARE.

Para obtener más información sobre el autor y sus proyectos, visite http://javierfuentes.dev
*/

package com.astronomy.stellar_view.data.local.trivia

import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Indica a Hilt que esta clase es un módulo que proporcionará dependencias.
@Module
@InstallIn(SingletonComponent::class)
object TriviaDatabaseModule {

    @Provides
    @Singleton
    // La función proporciona una instancia de QuestionsDatabase.
    fun provideDatabase(@ApplicationContext appContext: Context): QuestionsDatabase {

        //Queda un registro de que la base de datos fue creada correctamente
        Log.d("Database", "Database being created")

        return Room.databaseBuilder(
            appContext,
            QuestionsDatabase::class.java,
            "questions_db.db"
        ).build()
    }

    @Provides
    fun provideQuestionsDao(database: QuestionsDatabase): QuestionsDao {

        // La función proporciona una instancia de QuestionsDao.
        return database.questionsDao()
    }
}
