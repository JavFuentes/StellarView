package com.astronomy.stellar_view.interfaces

import android.content.Context

// Define una interfaz que garantiza que cualquier clase que la implemente
// será capaz de reproducir un efecto de sonido.
interface SoundPlayable {
    fun playSoundEffect(context: Context)
}
