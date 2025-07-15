package com.astronomy.stellar_view.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

/**
 * Actividad de splash que muestra la pantalla de carga inicial de la aplicación.
 *
 * Maneja la transición desde la pantalla de inicio hacia la actividad principal
 * con un tiempo de espera definido y gestión segura del ciclo de vida.
 *
 * @since 1.1.5
 */
class SplashActivity : AppCompatActivity() {

    companion object {
        /**
         * Tiempo de duración del splash en milisegundos (3 segundos)
         */
        private const val SPLASH_DURATION_MS = 3000L
    }

    /**
     * Handler para gestionar el delay de navegación
     */
    private var splashHandler: Handler? = null

    /**
     * Runnable que ejecuta la navegación
     */
    private var navigationRunnable: Runnable? = null

    /**
     * Indica si ya se navegó a la actividad principal para evitar múltiples navegaciones
     */
    private var hasNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeSplash()
    }

    override fun onPause() {
        super.onPause()
        // Si la actividad se pausa, marcar como navegada para evitar navegación doble
        hasNavigated = true
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanupResources()
    }

    /**
     * Inicializar la lógica del splash screen
     */
    private fun initializeSplash() {
        splashHandler = Handler(Looper.getMainLooper())

        navigationRunnable = Runnable {
            // Navegar solo si no se ha navegado ya y la actividad sigue activa
            if (!hasNavigated && !isFinishing) {
                navigateToMainActivity()
            }
        }

        // Programar la navegación después del tiempo especificado
        splashHandler?.postDelayed(navigationRunnable!!, SPLASH_DURATION_MS)
    }

    /**
     * Navegar a la actividad principal y finalizar esta actividad
     */
    private fun navigateToMainActivity() {
        if (hasNavigated) return

        hasNavigated = true

        val intent = Intent(this, MainActivity::class.java).apply {
            // Limpiar el stack de actividades para que el usuario no pueda volver al splash
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)

        // Finalizar esta actividad
        finish()

        // Agregar transición suave
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * Limpiar recursos para prevenir memory leaks
     */
    private fun cleanupResources() {
        navigationRunnable?.let { runnable ->
            splashHandler?.removeCallbacks(runnable)
        }
        splashHandler = null
        navigationRunnable = null
    }
}