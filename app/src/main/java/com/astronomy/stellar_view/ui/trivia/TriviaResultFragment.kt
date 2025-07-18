package com.astronomy.stellar_view.ui.trivia

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.astronomy.stellar_view.R
import com.astronomy.stellar_view.data.FirebaseHandler
import com.astronomy.stellar_view.interfaces.SoundPlayable
import com.astronomy.stellar_view.ui.base.BaseFragment
import com.astronomy.stellar_view.databinding.FragmentResultTriviaBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TriviaResultFragment : BaseFragment<FragmentResultTriviaBinding>() {

    @Inject
    lateinit var firebaseHandler: FirebaseHandler

    // SharedPreferences para obtener la puntuación
    private lateinit var sharedPreferences: SharedPreferences

    // Variable para almacenar la cita actual
    private var currentQuote: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restaura la cita si hay una guardada anteriormente
        currentQuote = savedInstanceState?.getString("quote")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Guarda la cita actual para poder recuperarla
        outState.putString("quote", currentQuote)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa las SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)

        // Obtiene el número de respuestas correctas de los argumentos
        val correctAnswers = arguments?.getInt("correctAnswers", 0)
        binding.tvCorrectAnswers.text = "$correctAnswers / 10"

        val categoryKey = arguments?.getString("categoryKey")
        val starRatingKey = "star_rating_$categoryKey"

        // Establece la imagen de la estrella según la cantidad de respuestas correctas
        when {
            correctAnswers!! >= 10 -> {
                binding.starImage.setImageResource(R.drawable.star_gold)

                // Actualiza solo si la nueva calificación es más alta
                if (sharedPreferences.getInt(starRatingKey, 0) < 3) {
                    sharedPreferences.edit().putInt(starRatingKey, 3).apply()
                }

                // Aquí verificamos y mostramos el anuncio emergente
                checkAndShowRateAppDialog()
            }
            correctAnswers >= 5 -> {
                binding.starImage.setImageResource(R.drawable.star_silver)

                // Actualiza solo si la nueva calificación es más alta
                if (sharedPreferences.getInt(starRatingKey, 0) < 2) {
                    sharedPreferences.edit().putInt(starRatingKey, 2).apply()
                }
            }
            correctAnswers >= 3 -> {
                binding.starImage.setImageResource(R.drawable.star_bronze)

                // Actualiza solo si la nueva calificación es más alta
                if (sharedPreferences.getInt(starRatingKey, 0) < 1) {
                    sharedPreferences.edit().putInt(starRatingKey, 1).apply()
                }
            }
            else -> {
                binding.starImage.setImageResource(R.drawable.star_empty)
            }
        }

        // Obtiene el índice actual de la cita
        val quoteIndexKey = "quote_index"
        var currentQuoteIndex = sharedPreferences.getInt(quoteIndexKey, 0)

        val quotesArray = resources.getStringArray(R.array.scientific_quotes)

        // Si no hay una cita guardada, busca una nueva
        if (currentQuote == null) {
            currentQuote = quotesArray[currentQuoteIndex]

            // Actualiza el índice de la cita para el próximo uso
            currentQuoteIndex = (currentQuoteIndex + 1) % quotesArray.size
            sharedPreferences.edit().putInt(quoteIndexKey, currentQuoteIndex).apply()
        }

        binding.tvQuote.text = currentQuote

        // Configura el botón para jugar de nuevo
        binding.btnAgain.setOnClickListener {
            (activity as? SoundPlayable)?.playSoundEffect(requireContext())
            findNavController().navigate(R.id.action_triviaResultFragment_to_triviaGameFragment)
        }

        // Configura el botón para volver al fragmento de categorías
        binding.btnCategories.setOnClickListener {
            (activity as? SoundPlayable)?.playSoundEffect(requireContext())
            findNavController().navigate(R.id.action_triviaResultFragment_to_triviaCategoriesFragment)
        }

        // Actualiza la puntuación basada en SharedPreferences y la sube a Firebase si es necesario
        val newScore = getUpdatedScoreFromSharedPreferences()
        if (shouldUpdateScore(newScore)) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.let {
                firebaseHandler.updateScoreInFirestore(it.uid, newScore)
                updateScoreInSharedPreferences(newScore)
                Log.d("TriviaResultFragment", "Se realizó una llamada a FireStore")
            }
        }
    }

    // Función para obtener la puntuación actualizada de SharedPreferences
    private fun getUpdatedScoreFromSharedPreferences(): String {
        val categories = listOf("category_1", "category_2", "category_3", "category_4", "category_5", "category_6")
        return categories.joinToString("") {
            val starRatingKey = "star_rating_$it"
            sharedPreferences.getInt(starRatingKey, 0).toString()
        }
    }

    // Determina si es necesario actualizar el puntaje
    private fun shouldUpdateScore(newScore: String): Boolean {
        val lastUploadedScore = sharedPreferences.getString("lastUploadedScore", "")
        return newScore != lastUploadedScore
    }

    // Actualiza el último puntaje subido en SharedPreferences
    private fun updateScoreInSharedPreferences(newScore: String) {
        sharedPreferences.edit().putString("lastUploadedScore", newScore).apply()
    }

    // Muestra un diálogo para solicitar al usuario que califique la aplicación
    private fun showRateAppDialog() {
        val builder = AlertDialog.Builder(requireContext())


        // Infla el layout personalizado
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_rate_app, null)

        builder.setView(customView)

        builder.setPositiveButton(getString(R.string.dialog_rate_now)) { _, _ ->
            openPlayStoreForRating()
        }
        builder.setNegativeButton(getString(R.string.dialog_rate_later)) { dialog, _ ->
            dialog.dismiss()
        }

        // Crea el AlertDialog
        val dialog = builder.create()

        // Establece el fondo de la ventana del diálogo como transparente
        //dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Muestra el AlertDialog
        dialog.show()
    }

    // Abre la Play Store para que el usuario pueda calificar la aplicación
    private fun openPlayStoreForRating() {
        val packageName = requireContext().packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    // Verifica ciertas condiciones y muestra el diálogo para calificar si es necesario
    private fun checkAndShowRateAppDialog() {
        val hasRatedAppBefore = sharedPreferences.getBoolean("hasRatedAppBefore", false)
        val hasWonGoldStarBefore = sharedPreferences.getBoolean("hasWonGoldStarBefore", false)

        if (!hasRatedAppBefore && !hasWonGoldStarBefore) {
            sharedPreferences.edit().putBoolean("hasWonGoldStarBefore", true).apply()
            showRateAppDialog()
        }
    }

    // Función para inicializar el ViewBinding
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentResultTriviaBinding.inflate(inflater, container, false)
}