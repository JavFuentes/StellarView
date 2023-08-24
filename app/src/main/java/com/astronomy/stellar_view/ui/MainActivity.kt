package com.astronomy.stellar_view.ui

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.astronomy.stellar_view.R
import com.astronomy.stellar_view.interfaces.SoundPlayable
import com.astronomy.stellar_view.databinding.ActivityMainBinding
import com.astronomy.stellar_view.ui.settings.SettingsFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

// Anotación para inyectar dependencias con Dagger Hilt
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SoundPlayable {

    // Variables para el manejo del enlace de datos y la configuración de la barra de acción
    private var activityBinding: ActivityMainBinding? = null

    // Mantener una referencia a la última callback
    private var backCallback: OnBackPressedCallback? = null

    private val binding get() = activityBinding!!
    private lateinit var appBarConfiguration: AppBarConfiguration

    // IDs de los destinos donde se deshabilitará el botón de retroceso
    private val blockedDestinations = listOf(
        R.id.triviaGameFragment,
        R.id.triviaResultFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbarTitle.text = getString(R.string.title_home)

        // Actualizar la vista en función del modo de visualización (día/noche)
        updateModeView()

        // Configuración de las vistas de navegación
        val drawerView: NavigationView = binding.navView

        // Configuración del controlador de navegación
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.favoritesFragment,
                R.id.settingsFragment,
                R.id.triviaCategoriesFragment,
                R.id.apodFragment,
                R.id.pictureOfDayFragment,
                R.id.triviaGameFragment,
                R.id.triviaResultFragment,
                R.id.newsFragment
            ),
            binding.drawerLayout
        )

        // Configurar la barra de acción y las vistas de navegación con el controlador de navegación
        setupActionBarWithNavController(navController, appBarConfiguration)

        drawerView.setupWithNavController(navController)

        // Configuración de la acción de abrir y cerrar el menú de navegación
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        //toggle.syncState()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            backCallback?.remove() // Remover la última callback antes de agregar una nueva

            backCallback = object : OnBackPressedCallback(blockedDestinations.contains(destination.id)) {
                override fun handleOnBackPressed() {
                    // No hacer nada
                }
            }
            onBackPressedDispatcher.addCallback(this, backCallback!!)

            // Llama a la función para actualizar el título de la toolbar
            updateToolbarTitle(destination.id)
        }

        // Manejar las acciones de los elementos del menú de navegación aquí
        drawerView.setNavigationItemSelectedListener { menuItem ->

            // Reproduce el sonido al hacer clic en un elemento del menú
            playSoundEffect(this)

            when (menuItem.itemId) {
                R.id.nav_drawer_home -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.nav_drawer_apod -> {
                    navController.navigate(R.id.apodFragment)
                }
                R.id.nav_drawer_favorites -> {
                    navController.navigate(R.id.favoritesFragment)
                }
                R.id.nav_drawer_trivia -> {
                    navController.navigate(R.id.triviaCategoriesFragment)
                }
                R.id.nav_drawer_settings -> {
                    navController.navigate(R.id.settingsFragment)
                }
//                R.id.nav_drawer_news -> {
//                    navController.navigate(R.id.newsFragment)
//                                        }
                R.id.nav_drawer_spacial_weather -> {
                    showComingSoonSnackbar()
                }
            }

            // Cerrar el menú de navegación después de seleccionar un elemento
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    // Manejar la selección de elementos de la barra de opciones
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {

            // Reproduce el sonido al hacer clic en el ícono de la hamburguesa
            playSoundEffect(this)

            // Abrir o cerrar el menú de navegación al seleccionar el botón de inicio
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Actualizar la vista en función del modo de visualización (día/noche)
    private fun updateModeView() {
        // Recuperar la preferencia del usuario para el modo de visualización
        val prefDarkMode: SharedPreferences =
            getSharedPreferences(SettingsFragment.NIGHT_MODE, Context.MODE_PRIVATE)
        val isNightModeOn = prefDarkMode.getBoolean(SettingsFragment.NIGHT_MODE, false)
        if (isNightModeOn) {
            // Si el modo nocturno está activado, establecer el modo nocturno
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            // Si el modo nocturno no está activado, establecer el modo diurno
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    // Navegar hacia arriba en la pila de navegación
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.mainContainer)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    // Limpiar la referencia al enlace de datos y liberar el mediaPlayer cuando la actividad se destruye
    override fun onDestroy() {
        super.onDestroy()
        activityBinding = null
    }

    // Cerrar el menú de navegación al pulsar el botón de retroceso
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //Snackbar que se mostrará por defecto en los items con funcionalidad aún por implementar
    private fun showComingSoonSnackbar() {
        Snackbar.make(binding.root, getString(R.string.coming_soon_alert), Snackbar.LENGTH_SHORT).show()
    }

    //Reproduce el efecto de sonido
    override fun playSoundEffect(context: Context) {
        val soundEffect = MediaPlayer.create(context, R.raw.select_sfx)
        soundEffect.start()
        soundEffect.setOnCompletionListener { mediaPlayer -> mediaPlayer.release() }
    }

    //Actualiiza el título del toolbar en función del fragmento activo
    private fun updateToolbarTitle(destinationId: Int) {
        val title = when (destinationId) {
            R.id.homeFragment -> getString(R.string.title_home)
            R.id.apodFragment -> getString(R.string.title_apod)
            R.id.favoritesFragment -> getString(R.string.title_favorites)
            R.id.triviaCategoriesFragment -> getString(R.string.title_astronomical_trivia)
            R.id.settingsFragment -> getString(R.string.title_settings)
            R.id.pictureOfDayFragment -> getString(R.string.title_apod)
            R.id.triviaGameFragment -> getString(R.string.title_astronomical_trivia)
            R.id.triviaResultFragment -> getString(R.string.title_astronomical_trivia)

            else -> getString(R.string.app_name)
        }
        binding.toolbarTitle.text = title
    }
}
