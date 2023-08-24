package com.astronomy.stellar_view.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.astronomy.stellar_view.R
import com.astronomy.stellar_view.interfaces.SoundPlayable
import com.astronomy.stellar_view.ui.base.BaseFragment
import com.astronomy.stellar_view.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    companion object {
        // Constantes para preferencias compartidas
        const val NIGHT_MODE = "nightMode"
        const val DAYS_BACK = "daysBack"
    }

    private var isNightModeOn = false
    private lateinit var daysBackPrefEdit: SharedPreferences.Editor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de preferencias compartidas para DAYS_BACK
        val prefDaysBack: SharedPreferences =
            requireActivity().getSharedPreferences(DAYS_BACK, Context.MODE_PRIVATE)
        daysBackPrefEdit = prefDaysBack.edit()

        val isOneDayAgoOn = prefDaysBack.getInt(DAYS_BACK, 0) == 1
        binding.oneDayAgoSwitch.isChecked = isOneDayAgoOn

        binding.oneDayAgoSwitch.setOnCheckedChangeListener { _, isChecked ->

            // Reproduce el efecto de sonido
            (activity as? SoundPlayable)?.playSoundEffect(requireContext())

            // Actualiza el valor en las preferencias compartidas según el estado del interruptor
            if (isChecked) {
                daysBackPrefEdit.putInt(DAYS_BACK, 1)
            } else {
                daysBackPrefEdit.putInt(DAYS_BACK, 0)
            }
            daysBackPrefEdit.apply()
        }

        // Preferencias compartidas para almacenar el estado del modo nocturno
        val prefDarkMode: SharedPreferences =
            requireActivity().getSharedPreferences(NIGHT_MODE, Context.MODE_PRIVATE)
        val editorDarkMode = prefDarkMode.edit()
        isNightModeOn = prefDarkMode.getBoolean(NIGHT_MODE, false)

        binding.helpIcon.setOnClickListener {

            // Reproduce el efecto de sonido
            (activity as? SoundPlayable)?.playSoundEffect(requireContext())

            // Muestra un cuadro de diálogo de ayuda
            val helpMessage = getString(R.string.help_one_day_ago_message)
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.one_day_ago_alert_dialog))
                .setMessage(helpMessage)
                .setPositiveButton(getString(R.string.ok_alert_dialog)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.apply {

            // Configura la vista según si el modo nocturno está activo o no
            if (isNightModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                enableDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                enableDarkMode.isChecked = false
            }

            // Listener para el cambio de estado del modo nocturno
            enableDarkMode.setOnCheckedChangeListener { _, isChecked ->

                // Reproduce el efecto de sonido
                (activity as? SoundPlayable)?.playSoundEffect(requireContext())

                // Actualiza el estado del modo nocturno en las preferencias compartidas y la vista
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editorDarkMode.putBoolean(NIGHT_MODE, true)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editorDarkMode.putBoolean(NIGHT_MODE, false)
                }
                editorDarkMode.apply()
            }
        }
    }

    // Método para inicializar el ViewBinding
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSettingsBinding.inflate(inflater, container, false)
}
