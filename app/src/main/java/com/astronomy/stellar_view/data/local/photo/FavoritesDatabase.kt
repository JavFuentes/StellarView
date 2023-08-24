package com.astronomy.stellar_view.data.local.photo

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *   Esta clase actúa como una base de datos SQLite utilizando Room.
 *   Puede tener varias entidades y sus correspondientes DAO.
 *   Aquí se pueden agregar métodos y configuraciones adicionales relacionadas con la base de datos,
 *   como migraciones, inicializaciones, etc.
 *   Room generará automáticamente la implementación de la clase derivada de esta clase abstracta y
 *   proporcionará métodos para interactuar con la base de datos.
**/

// Define la anotación @Database para marcar esta clase como una base de datos de Room
@Database(
    entities = [PhotoEntity::class], // Especifica las entidades (tablas) que formarán parte de la base de datos
    version = 1 // Especifica la versión de la base de datos
)
// Declara una clase abstracta que extiende RoomDatabase, que servirá como la clase principal de la base de datos
abstract class FavoritesDatabase : RoomDatabase() {
    abstract val dao: FavoritesDao // Declara una propiedad abstracta para obtener el DAO (Objeto de Acceso a Datos)

}
