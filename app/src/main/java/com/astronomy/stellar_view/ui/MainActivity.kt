package com.astronomy.stellar_view.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.astronomy.stellar_view.R
import com.astronomy.stellar_view.databinding.ActivityMainBinding
import com.astronomy.stellar_view.interfaces.SoundPlayable
import com.astronomy.stellar_view.ui.settings.SettingsFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal que maneja la navegación, temas y efectos de sonido para la app StellarView.
 * Demuestra arquitectura de actividad única con Navigation Component.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SoundPlayable {

    companion object {
        private const val TOOLBAR_TOP_PADDING = 16
        private const val CONTAINER_BOTTOM_MARGIN = 16
    }

    // View binding para acceso seguro a las vistas
    private var activityBinding: ActivityMainBinding? = null
    private val binding get() = activityBinding!!

    // Componentes de navegación
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var backCallback: OnBackPressedCallback? = null

    // Destinos donde el botón de retroceso debe estar deshabilitado para mejor UX
    private val blockedDestinations = setOf(
        R.id.triviaGameFragment,
        R.id.triviaResultFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeBinding()
        configureActionBar()
        setupThemeAndStatusBar()
        setupWindowInsets()
        setupNavigation()
    }

    /**
     * Inicializar view binding y establecer content view
     */
    private fun initializeBinding() {
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * Configurar action bar con título personalizado
     */
    private fun configureActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarTitle.text = getString(R.string.title_home)
    }

    /**
     * Configurar modo de tema y colores del status bar
     */
    private fun setupThemeAndStatusBar() {
        updateModeView()
        setupStatusBar()
    }

    /**
     * Configurar window insets para manejo apropiado de system bars
     */
    private fun setupWindowInsets() {
        // Usar manejo tradicional de system bars (no edge-to-edge)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        // Agregar espaciado visual
        binding.toolbar.setPadding(0, TOOLBAR_TOP_PADDING, 0, 0)

        // Prevenir corte de contenido en la parte inferior
        val layoutParams = binding.mainContainer.layoutParams
                as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
        layoutParams.bottomMargin = CONTAINER_BOTTOM_MARGIN
        binding.mainContainer.layoutParams = layoutParams
    }

    /**
     * Configurar navigation controller y drawer
     */
    private fun setupNavigation() {
        val drawerView: NavigationView = binding.navView

        // Configurar Navigation Controller
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.mainContainer) as NavHostFragment
        val navController = navHostFragment.navController

        // Definir destinos de nivel superior
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

        // Configurar action bar y navigation view con nav controller
        setupActionBarWithNavController(navController, appBarConfiguration)
        drawerView.setupWithNavController(navController)

        // Configurar drawer toggle
        setupDrawerToggle()

        // Configurar listeners de navegación
        setupNavigationListeners(navController, drawerView)
    }

    /**
     * Configurar animación del drawer toggle
     */
    private fun setupDrawerToggle() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
    }

    /**
     * Configurar listeners de destino de navegación y elementos del menú
     */
    private fun setupNavigationListeners(
        navController: androidx.navigation.NavController,
        drawerView: NavigationView
    ) {
        // Manejar cambios de destino
        navController.addOnDestinationChangedListener { _, destination, _ ->
            configureBackButton(destination.id)
            updateToolbarTitle(destination.id)
        }

        // Manejar navegación del drawer
        drawerView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItemSelected(menuItem, navController)
        }
    }

    /**
     * Configurar comportamiento del botón de retroceso para destinos específicos
     */
    private fun configureBackButton(destinationId: Int) {
        backCallback?.remove()

        backCallback = object : OnBackPressedCallback(blockedDestinations.contains(destinationId)) {
            override fun handleOnBackPressed() {
                // Intencionalmente vacío - previene navegación hacia atrás para destinos bloqueados
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback!!)
    }

    /**
     * Manejar selección de elementos del navigation drawer
     */
    private fun handleNavigationItemSelected(
        menuItem: MenuItem,
        navController: androidx.navigation.NavController
    ): Boolean {
        playSoundEffect(this)

        when (menuItem.itemId) {
            R.id.nav_drawer_home -> navController.navigate(R.id.homeFragment)
            R.id.nav_drawer_apod -> navController.navigate(R.id.apodFragment)
            R.id.nav_drawer_favorites -> navController.navigate(R.id.favoritesFragment)
            R.id.nav_drawer_trivia -> navController.navigate(R.id.triviaCategoriesFragment)
            R.id.nav_drawer_settings -> navController.navigate(R.id.settingsFragment)
            R.id.nav_drawer_spacial_weather -> showComingSoonSnackbar()
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * Configurar colores del status bar basado en el tema actual
     */
    private fun setupStatusBar() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // Tema claro - status bar claro con iconos oscuros
                window.statusBarColor = ContextCompat.getColor(this, R.color.color2)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                // Tema oscuro - status bar oscuro con iconos claros
                window.statusBarColor = ContextCompat.getColor(this, R.color.color2)
                window.decorView.systemUiVisibility = 0
            }
        }
    }

    /**
     * Actualizar modo de tema basado en preferencias del usuario
     */
    private fun updateModeView() {
        val prefDarkMode: SharedPreferences = getSharedPreferences(
            SettingsFragment.NIGHT_MODE,
            Context.MODE_PRIVATE
        )
        val isNightModeOn = prefDarkMode.getBoolean(SettingsFragment.NIGHT_MODE, false)

        val nightMode = if (isNightModeOn) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)
        setupStatusBar()
    }

    /**
     * Actualizar título del toolbar basado en el destino actual
     */
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

    /**
     * Mostrar snackbar para funcionalidades próximamente
     */
    private fun showComingSoonSnackbar() {
        Snackbar.make(
            binding.root,
            getString(R.string.coming_soon_alert),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    /**
     * Reproducir efecto de sonido con gestión apropiada de recursos
     */
    override fun playSoundEffect(context: Context) {
        val soundEffect = MediaPlayer.create(context, R.raw.select_sfx)
        soundEffect?.apply {
            start()
            setOnCompletionListener { mediaPlayer ->
                mediaPlayer.release()
            }
        }
    }

    // Métodos del ciclo de vida de la actividad
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupStatusBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            playSoundEffect(this)

            // Toggle drawer
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.mainContainer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityBinding = null
    }
}