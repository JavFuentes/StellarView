/*
MIT License

Copyright (c) 2023 Javier Fuentes

Se concede permiso, de forma gratuita, a cualquier persona que obtenga una copia de este software y de los archivos de documentación asociados (el "Software"), para tratar el Software sin restricción, incluyendo, sin limitación, los derechos de uso, copia, modificación, fusión, publicación, distribución, sublicencia y/o venta de copias del Software, y para permitir a las personas a las que se les proporcione el Software hacerlo, sujeto a las siguientes condiciones:

El aviso de derechos de autor anterior y este aviso de permisos se incluirán en todas las copias o partes sustanciales del Software.

EL SOFTWARE SE PROPORCIONA "TAL CUAL", SIN GARANTÍA DE NINGÚN TIPO, EXPRESA O IMPLÍCITA, INCLUYENDO PERO NO LIMITADO A LAS GARANTÍAS DE COMERCIABILIDAD, IDONEIDAD PARA UN PROPÓSITO PARTICULAR Y NO INFRACCIÓN. EN NINGÚN CASO LOS AUTORES O TITULARES DE LOS DERECHOS DE AUTOR SERÁN RESPONSABLES POR CUALQUIER RECLAMO, DAÑOS U OTRAS RESPONSABILIDADES, YA SEA EN UNA ACCIÓN DE CONTRATO, AGRAVIO O DE OTRO MODO, DERIVADAS DE, FUERA DE O EN CONEXIÓN CON EL SOFTWARE O EL USO O OTROS NEGOCIOS EN EL SOFTWARE.

Para obtener más información sobre el autor y sus proyectos, visite http://javierfuentes.dev
*/

package com.astronomy.stellar_view.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Función que proporciona una instancia de SharedPreferences
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext
        appContext: Context
    ): SharedPreferences {
        // Devuelve una instancia de SharedPreferences con el nombre "TriviaPreferences" en modo privado
        return appContext.getSharedPreferences("TriviaPreferences", Context.MODE_PRIVATE)
    }
}
