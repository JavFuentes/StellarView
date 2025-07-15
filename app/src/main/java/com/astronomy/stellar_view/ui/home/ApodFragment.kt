package com.astronomy.stellar_view.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.astronomy.stellar_view.R
import com.astronomy.stellar_view.interfaces.SoundPlayable
import com.astronomy.stellar_view.ui.base.BaseFragment
import com.astronomy.stellar_view.databinding.FragmentApodBinding
import com.astronomy.stellar_view.ui.home.details.PictureOfDayViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ApodFragment : BaseFragment<FragmentApodBinding>() {

    companion object {
        const val SELECTED_DATE = "selectedDate"
        const val DAYS_BACK = "daysBack"
    }

    private val viewModel: PictureOfDayViewModel by viewModels()
    private lateinit var selectedDatePrefEdit: SharedPreferences.Editor
    private lateinit var daysBackPrefEdit: SharedPreferences.Editor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val prefSelectedDate: SharedPreferences =
            requireActivity().getSharedPreferences(SELECTED_DATE, Context.MODE_PRIVATE)
        val prefDaysBack: SharedPreferences =
            requireActivity().getSharedPreferences(DAYS_BACK, Context.MODE_PRIVATE)

        selectedDatePrefEdit = prefSelectedDate.edit()
        daysBackPrefEdit = prefDaysBack.edit()

        binding.apply {
            imgTodayApod.setImageResource(R.drawable.default_image)

            val savedDate = prefSelectedDate.getString(SELECTED_DATE, null)
            val date: String = savedDate ?: run {
                val daysBack = prefDaysBack.getInt(DAYS_BACK, 0)
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DATE, -daysBack)
                String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }


            selectedDateTextView.text = date
            selectedDatePrefEdit.putString(SELECTED_DATE, date).apply()

            cvToday.setOnClickListener {
                (activity as? SoundPlayable)?.playSoundEffect(requireContext())
                val action = ApodFragmentDirections.actionApodFragmentToPictureOfDayFragment()
                findNavController().navigate(action)
            }

            btnSelectDate.setOnClickListener {
                (activity as? SoundPlayable)?.playSoundEffect(requireContext())
                val newFragment = DatePickerFragment()
                newFragment.show(activity?.supportFragmentManager!!, "datePicker")
            }
        }

        viewModel.getPhotoBasedOnDate(binding.selectedDateTextView.text.trim().toString())
        observePhoto()
        observeUiEvent()
    }

    // Observa los cambios en la foto del día y actualiza la vista acorde a ello.
    private fun observePhoto() {
        viewModel.photoBasedOnDate.observe(viewLifecycleOwner) { photo ->
            binding.imgTodayTitle.text = photo.title
            binding.imgTodayApod.load(photo.url)
        }
    }

    // Observa los eventos de la interfaz del usuario y reacciona a ellos.
    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.todayPhotoEvent.collect { event ->
                when (event) {
                    // Muestra un Snackbar con un mensaje.
                    is PictureOfDayViewModel.UiTodayPhotoEvent.ShowSnackbar -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                    // Muestra u oculta la barra de progreso dependiendo de si está cargando o no.
                    is PictureOfDayViewModel.UiTodayPhotoEvent.ShowProgressBar -> {
                        binding.pbLoading.isVisible = event.isLoading
                    }
                }
            }
        }
    }

    // Método para inflar el menú de opciones.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_menu, menu)
    }

    // Maneja las selecciones de los ítems del menú.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Refrescar la imagen del día.
            R.id.refresh -> {
                viewModel.getPhotoBasedOnDate(binding.selectedDateTextView.text.trim().toString())
                true
            }
            // Mostrar un cuadro de diálogo de ayuda.
            R.id.help -> {
                showHelpDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Método para mostrar un cuadro de diálogo con información de ayuda.
    private fun showHelpDialog() {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.apod_help_title))
            .setMessage(R.string.apod_description)
            .setPositiveButton(R.string.ok_alert_dialog) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Método para inicializar el binding de este fragmento.
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentApodBinding.inflate(inflater, container, false)

    // Método para actualizar la fecha mostrada en la vista.
    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c[Calendar.YEAR] = year
        c[Calendar.MONTH] = month
        c[Calendar.DAY_OF_MONTH] = dayOfMonth

        val monthString =
            if ((month + 1) / 10 >= 1) (month + 1).toString() else "0" + (month + 1)
        val dayOfMonthString =
            if (dayOfMonth / 10 >= 1) dayOfMonth.toString() else "0$dayOfMonth"
        val mCurrentDateFormatInput = "$year-$monthString-$dayOfMonthString"

        binding.apply {
            selectedDateTextView.text = mCurrentDateFormatInput
        }

        // Guardar la fecha actualizada en las preferencias.
        selectedDatePrefEdit.putString(SELECTED_DATE, mCurrentDateFormatInput).apply()

        // Solicitar la foto del día basado en la nueva fecha seleccionada.
        viewModel.getPhotoBasedOnDate(binding.selectedDateTextView.text.trim().toString())
    }
}
