package com.astronomy.stellar_view.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.astronomy.stellar_view.R
import com.astronomy.stellar_view.interfaces.SoundPlayable
import com.astronomy.stellar_view.ui.base.BaseFragment
import com.astronomy.stellar_view.databinding.FragmentHomeBinding
import com.astronomy.stellar_view.ui.home.details.PictureOfDayViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * Fragmento principal de la aplicación StellarView que actúa como pantalla de inicio.
 *
 * Características principales:
 * - Muestra la imagen astronómica del día (APOD)
 * - Proporciona acceso rápido a secciones principales (Trivia, Noticias)
 * - Muestra el progreso del usuario en las categorías de trivia mediante estrellas
 * - Gestiona navegación hacia otras pantallas de la aplicación
 *
 * Este fragmento implementa el patrón MVVM y utiliza:
 * - ViewBinding para acceso seguro a las vistas
 * - ViewModel para manejo de datos y estados
 * - Navigation Component para navegación entre fragmentos
 * - SharedPreferences para persistencia de datos locales
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        /**
         * Clave para almacenar la fecha seleccionada en SharedPreferences.
         */
        const val SELECTED_DATE = "selectedDate"

        /**
         * Clave para almacenar el número de días hacia atrás para la fecha APOD.
         */
        const val DAYS_BACK = "daysBack"

        /**
         * Nombre del archivo SharedPreferences para datos generales de la app.
         */
        private const val SHARED_PREFS_NAME = "MySharedPrefs"

        /**
         * Formato de fecha estándar para las consultas APOD (YYYY-MM-DD).
         */
        private const val DATE_FORMAT = "%04d-%02d-%02d"

        /**
         * Prefijo para las claves de calificación por estrellas en SharedPreferences.
         */
        private const val STAR_RATING_PREFIX = "star_rating_"
    }

    /**
     * ViewModel que controla los datos de la imagen astronómica del día.
     */
    private val viewModel: PictureOfDayViewModel by viewModels()

    /**
     * SharedPreferences para almacenar datos generales de la aplicación.
     */
    private lateinit var sharedPreferences: SharedPreferences

    /**
     * Editor para modificar las preferencias de fecha seleccionada.
     */
    private lateinit var selectedDatePrefEdit: SharedPreferences.Editor

    /**
     * Editor para modificar las preferencias de días hacia atrás.
     */
    private lateinit var daysBackPrefEdit: SharedPreferences.Editor

    /**
     * Fecha actual formateada para consultas APOD.
     */
    private lateinit var today: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initializeSharedPreferences()
        setupDateCalculation()
        setupClickListeners()
        loadTodayPhoto()
        observeViewModelData()
    }

    /**
     * Inicializa las SharedPreferences y sus editores para manejo de datos locales.
     */
    private fun initializeSharedPreferences() {
        val prefSelectedDate: SharedPreferences =
            requireActivity().getSharedPreferences(SELECTED_DATE, Context.MODE_PRIVATE)
        val prefDaysBack: SharedPreferences =
            requireActivity().getSharedPreferences(DAYS_BACK, Context.MODE_PRIVATE)

        selectedDatePrefEdit = prefSelectedDate.edit()
        daysBackPrefEdit = prefDaysBack.edit()
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Calcula la fecha para mostrar la imagen APOD basándose en los días hacia atrás configurados.
     * Permite al usuario ver imágenes de días anteriores si está configurado.
     */
    private fun setupDateCalculation() {
        val prefDaysBack: SharedPreferences =
            requireActivity().getSharedPreferences(DAYS_BACK, Context.MODE_PRIVATE)

        val daysBack = prefDaysBack.getInt(DAYS_BACK, 0)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -daysBack)

        today = String.format(
            Locale.getDefault(),
            DATE_FORMAT,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Guarda la fecha seleccionada para uso en otros fragmentos
        selectedDatePrefEdit.putString(SELECTED_DATE, today).apply()
    }

    /**
     * Configura los listeners de click para los elementos principales de la interfaz.
     * Cada sección principal tiene navegación hacia su fragmento correspondiente.
     */
    private fun setupClickListeners() {
        binding.apply {
            // Configura imagen por defecto mientras carga
            imgTodayApod.setImageResource(R.drawable.default_image)

            // Navegación a detalles de la imagen astronómica del día
            cvToday.setOnClickListener {
                playSelectSound()
                findNavController().navigate(R.id.action_homeFragment_to_pictureOfDayFragment)
            }

            // Navegación a las categorías de trivia astronómica
            cvTrivia.setOnClickListener {
                playSelectSound()
                findNavController().navigate(R.id.action_homeFragment_to_triviaCategoriesFragment)
            }

            // Navegación a noticias astronómicas
            cvNews.setOnClickListener {
                playSelectSound()
                findNavController().navigate(R.id.action_homeFragment_to_newsFragment)
            }
        }
    }

    /**
     * Inicia la carga de la imagen astronómica del día basándose en la fecha calculada.
     */
    private fun loadTodayPhoto() {
        viewModel.getPhotoBasedOnDate(today.trim())
    }

    /**
     * Configura la observación de datos del ViewModel para actualizar la UI.
     */
    private fun observeViewModelData() {
        observePhoto()
        observeUiEvent()
    }

    /**
     * Observa los cambios en la imagen astronómica del día y actualiza la interfaz.
     * Carga la imagen usando Coil y actualiza el título correspondiente.
     */
    private fun observePhoto() {
        viewModel.photoBasedOnDate.observe(viewLifecycleOwner) { photo ->
            binding.apply {
                imgTodayTitle.text = photo.title
                imgTodayApod.load(photo.url)
            }
        }
    }

    /**
     * Observa eventos de la UI del ViewModel para manejar estados de carga y errores.
     * Gestiona la visualización de mensajes de error y indicadores de progreso.
     */
    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.todayPhotoEvent.collect { event ->
                when (event) {
                    is PictureOfDayViewModel.UiTodayPhotoEvent.ShowSnackbar -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                    is PictureOfDayViewModel.UiTodayPhotoEvent.ShowProgressBar -> {
                        binding.pbLoading.isVisible = event.isLoading
                    }
                }
            }
        }
    }

    /**
     * Reproduce el efecto de sonido de selección para mejorar la experiencia de usuario.
     */
    private fun playSelectSound() {
        (activity as? SoundPlayable)?.playSoundEffect(requireContext())
    }

    /**
     * Maneja las opciones del menú superior, incluyendo la funcionalidad de refrescar.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                viewModel.getPhotoBasedOnDate(today.trim())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Actualiza las calificaciones por estrellas de las categorías de trivia cuando el fragmento se reanuda.
     * Muestra el progreso visual del usuario en cada categoría mediante diferentes tipos de estrellas.
     */
    override fun onResume() {
        super.onResume()
        updateAllStarRatings()
    }

    /**
     * Actualiza todas las calificaciones por estrellas de las categorías de trivia.
     */
    private fun updateAllStarRatings() {
        val categories = listOf(
            "category_1" to binding.starRatingCategory1,
            "category_2" to binding.starRatingCategory2,
            "category_3" to binding.starRatingCategory3,
            "category_4" to binding.starRatingCategory4,
            "category_5" to binding.starRatingCategory5,
            "category_6" to binding.starRatingCategory6
        )

        categories.forEach { (categoryKey, imageView) ->
            setStarRatingForCategory(categoryKey, imageView, sharedPreferences)
        }
    }

    /**
     * Establece la imagen de calificación por estrellas para una categoría específica.
     *
     * Sistema de calificación:
     * - 0: Estrella vacía (sin progreso)
     * - 1: Estrella de bronce (3-4 respuestas correctas)
     * - 2: Estrella de plata (5-9 respuestas correctas)
     * - 3: Estrella de oro (10 respuestas correctas - perfecto)
     *
     * @param categoryKey Identificador único de la categoría de trivia
     * @param imageView Vista de imagen donde mostrar la calificación
     * @param sharedPreferences Preferencias donde se almacena el progreso del usuario
     */
    private fun setStarRatingForCategory(
        categoryKey: String,
        imageView: ImageView,
        sharedPreferences: SharedPreferences
    ) {
        val starRatingKey = "$STAR_RATING_PREFIX$categoryKey"
        val rating = sharedPreferences.getInt(starRatingKey, 0)

        val starDrawableId = when (rating) {
            1 -> R.drawable.star_bronze  // Bronce: Buen progreso
            2 -> R.drawable.star_silver  // Plata: Muy buen progreso
            3 -> R.drawable.star_gold    // Oro: Perfección alcanzada
            else -> R.drawable.star_empty // Vacía: Sin progreso aún
        }

        imageView.setImageResource(starDrawableId)
    }

    /**
     * Inicializa el ViewBinding para acceso seguro a las vistas del fragmento.
     */
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)
}