package com.astronomy.stellar_view.ui.home

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.NavHostFragment
import com.astronomy.stellar_view.interfaces.SoundPlayable
import java.util.*

// Clase DatePickerFragment que extiende DialogFragment y implementa OnDateSetListener para recibir la fecha seleccionada
class DatePickerFragment : DialogFragment(), OnDateSetListener {

    companion object {
        const val DAYS_BACK = "daysBack"
    }

    // Método para crear el diálogo de selección de fecha
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)

        val prefDaysBack: SharedPreferences =
            requireActivity().getSharedPreferences(DAYS_BACK, Context.MODE_PRIVATE)

        val daysBack = prefDaysBack.getInt(DAYS_BACK, 0)

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - daysBack * 24 * 60 * 60 * 1000L

        val minDate = Calendar.getInstance()
        minDate.set(1995, 5, 16)
        datePickerDialog.datePicker.minDate = minDate.timeInMillis

        return datePickerDialog
    }

    // Método llamado cuando el usuario selecciona una fecha
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {

        (activity as? SoundPlayable)?.playSoundEffect(requireContext())

        // Mostramos un mensaje en el log
        Log.i("DatePickerFragment", "inside onDateSet")

        // Obtenemos el NavHostFragment
        val navHostFragment: NavHostFragment =
            activity?.supportFragmentManager?.fragments?.get(0) as NavHostFragment

        // Obtenemos el fragmento HomeFragment
        val fragmentDemo: ApodFragment = navHostFragment.childFragmentManager.fragments[0] as ApodFragment

        // Actualizamos la fecha en HomeFragment
        fragmentDemo.updateDate(year, month, day)
    }
}
