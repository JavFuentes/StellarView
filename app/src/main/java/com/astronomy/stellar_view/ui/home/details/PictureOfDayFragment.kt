package com.astronomy.stellar_view.ui.home.details

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.astronomy.stellar_view.ui.base.BaseFragment
import com.astronomy.stellar_view.databinding.FragmentPictureOfDayBinding
import com.astronomy.stellar_view.ui.home.HomeFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PictureOfDayFragment : BaseFragment<FragmentPictureOfDayBinding>() {

    private val viewModel: PictureOfDayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener la fecha seleccionada desde las preferencias compartidas y cargar la foto correspondiente
        val prefSelectedDate: SharedPreferences =
            requireActivity().getSharedPreferences(HomeFragment.SELECTED_DATE, Context.MODE_PRIVATE)
        if (prefSelectedDate.getString(HomeFragment.SELECTED_DATE, null) != null) {
            val dateString = prefSelectedDate.getString(HomeFragment.SELECTED_DATE, null).toString()
            viewModel.getPhotoBasedOnDate(dateString)
        }
        observePhoto()
        observeUiEvent()
    }

    private fun observePhoto() {
        viewModel.photoBasedOnDate.observe(viewLifecycleOwner) { photo ->
            // Actualizar la interfaz de usuario con los datos de la foto
            binding.tvTitle.text = photo.title
            binding.tvContent.text = photo.explanation
            binding.tvDate.text = photo.date
            binding.imgApod.load(photo.url)
            binding.btnAddFavoritesFromToday.setOnClickListener {
                // Manejar el evento de clic en el botón de agregar a favoritos
                viewModel.onEvent(PictureOfDayEvent.AddFavoritesClicked(photo))
            }
        }
    }

    private fun observeUiEvent() {
        // Observar los eventos relacionados con la interfaz de usuario
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.todayPhotoEvent.collect { event ->
                when (event) {
                    is PictureOfDayViewModel.UiTodayPhotoEvent.ShowSnackbar -> {
                        // Mostrar una Snackbar con el mensaje proporcionado
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                    is PictureOfDayViewModel.UiTodayPhotoEvent.ShowProgressBar -> {
                        // Mostrar u ocultar la barra de progreso según el estado proporcionado
                        binding.pbLoading.isVisible = event.isLoading
                    }
                }
            }
        }
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPictureOfDayBinding.inflate(inflater, container, false)
}
