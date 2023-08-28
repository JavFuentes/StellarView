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

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    // Constantes para las SharedPreferences
    companion object {
        const val SELECTED_DATE = "selectedDate"
        const val DAYS_BACK = "daysBack"
    }

    // ViewModel que controla los datos del PictureOfDay
    private val viewModel: PictureOfDayViewModel by viewModels()

    // Preferencias y editores para almacenar y editar datos localmente
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var selectedDatePrefEdit: SharedPreferences.Editor
    private lateinit var daysBackPrefEdit: SharedPreferences.Editor
    private lateinit var today: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        // Obtiene las SharedPreferences
        val prefSelectedDate: SharedPreferences =
            requireActivity().getSharedPreferences(SELECTED_DATE, Context.MODE_PRIVATE)
        val prefDaysBack: SharedPreferences =
            requireActivity().getSharedPreferences(DAYS_BACK, Context.MODE_PRIVATE)

        // Inicializa los editores para las SharedPreferences
        selectedDatePrefEdit = prefSelectedDate.edit()
        daysBackPrefEdit = prefDaysBack.edit()
        sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)

        binding.apply {
            // Configura la imagen por defecto
            imgTodayApod.setImageResource(R.drawable.default_image)

            // Calcula la fecha basada en cuántos días atrás se configuró
            val daysBack = prefDaysBack.getInt(DAYS_BACK, 0)
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, -daysBack)

            val date = String.format(
                Locale.getDefault(),
                "%04d-%02d-%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Asigna la fecha actual
            today = date

            // Guarda la fecha seleccionada en las SharedPreferences
            selectedDatePrefEdit.putString(SELECTED_DATE, date).apply()

            // Click listener para el card view del APOD
            cvToday.setOnClickListener {
                playSelectSound() // Reproduce un sonido
                findNavController().navigate(R.id.action_homeFragment_to_pictureOfDayFragment) // Navega a otro fragmento
            }

            // Click listeners para el card view de la trivia
            cvTrivia.setOnClickListener {
                playSelectSound()
                findNavController().navigate(R.id.action_homeFragment_to_triviaCategoriesFragment)
            }

            // Click listeners para el card view de las noticias
            cvNews.setOnClickListener {
                playSelectSound()
                findNavController().navigate(R.id.action_homeFragment_to_newsFragment)
            }
        }

        // Obtiene la foto basada en la fecha y observa los cambios
        viewModel.getPhotoBasedOnDate(today.trim())
        observePhoto()
        observeUiEvent()
    }

    // Observa la foto del día y la muestra en la interfaz
    private fun observePhoto() {
        viewModel.photoBasedOnDate.observe(viewLifecycleOwner) { photo ->
            binding.imgTodayTitle.text = photo.title
            binding.imgTodayApod.load(photo.url)
        }
    }

    // Observa eventos de la interfaz y realiza acciones correspondientes
    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.todayPhotoEvent.collect { event ->
                when (event) {
                    is PictureOfDayViewModel.UiTodayPhotoEvent.ShowSnackbar -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show() // Muestra un Snackbar
                    }
                    is PictureOfDayViewModel.UiTodayPhotoEvent.ShowProgressBar -> {
                        binding.pbLoading.isVisible = event.isLoading // Muestra/Oculta la barra de progreso
                    }
                }
            }
        }
    }

    // Función para reproducir sonido
    private fun playSelectSound(){
        (activity as? SoundPlayable)?.playSoundEffect(requireContext())
    }

    // Opciones del menú superior
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                viewModel.getPhotoBasedOnDate(today.trim()) // Refresca la imagen del día
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        // Establece las valoraciones por estrellas para cada categoría basándose en SharedPreferences
        setStarRatingForCategory("category_1", binding.starRatingCategory1, sharedPreferences)
        setStarRatingForCategory("category_2", binding.starRatingCategory2, sharedPreferences)
        setStarRatingForCategory("category_3", binding.starRatingCategory3, sharedPreferences)
        setStarRatingForCategory("category_4", binding.starRatingCategory4, sharedPreferences)
        setStarRatingForCategory("category_5", binding.starRatingCategory5, sharedPreferences)
        setStarRatingForCategory("category_6", binding.starRatingCategory6, sharedPreferences)
    }

    // Función para establecer la valoración por estrellas en la interfaz
    private fun setStarRatingForCategory(categoryKey: String, imageView: ImageView, sharedPreferences: SharedPreferences) {
        val starRatingKey = "star_rating_$categoryKey"

        // Selecciona el recurso drawable basándose en el valor guardado en SharedPreferences
        val starDrawableId = when (sharedPreferences.getInt(starRatingKey, 0)) {
            1 -> R.drawable.star_bronze
            2 -> R.drawable.star_silver
            3 -> R.drawable.star_gold
            else -> R.drawable.star_empty
        }

        // Establece la imagen de la clasificación por estrellas
        imageView.setImageResource(starDrawableId)
    }

    // Inicializa el binding para este fragmento
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)
}
