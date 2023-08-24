package com.astronomy.stellar_view.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    // Constante para el tiempo de espera (3000 ms = 3 segundos)
    private val SPLASH_TIME_OUT:Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crea un nuevo temporizador
        Handler(Looper.getMainLooper()).postDelayed({
            // Este método se ejecutará una vez transcurrido el tiempo de espera.
            // Comienza tu aplicación principal
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)

            // Cierra esta actividad
            finish()
        }, SPLASH_TIME_OUT)
    }
}