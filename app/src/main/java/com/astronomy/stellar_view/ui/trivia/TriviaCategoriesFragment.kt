package com.astronomy.stellar_view.ui.trivia

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.astronomy.stellar_view.R
import com.astronomy.stellar_view.interfaces.SoundPlayable
import com.astronomy.stellar_view.ui.base.BaseFragment
import com.astronomy.stellar_view.databinding.FragmentCategoriesTriviaBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TriviaCategoriesFragment : BaseFragment<FragmentCategoriesTriviaBinding>() {
    // SharedPreferences para almacenar la puntuación
    private lateinit var sharedPreferences: SharedPreferences

    // Inicializar SharedPreferences en la creación del fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Establece el listener de click para cada categoría y navega al juego de trivia correspondiente
        binding.category1.setOnClickListener {
            navigateToTriviaGameFragment("category_1")
        }

        binding.category2.setOnClickListener {
            navigateToTriviaGameFragment("category_2")
        }

        binding.category3.setOnClickListener {
            navigateToTriviaGameFragment("category_3")
        }

        binding.category4.setOnClickListener {
            navigateToTriviaGameFragment("category_4")
        }

        binding.category5.setOnClickListener {
            navigateToTriviaGameFragment("category_5")
        }

        binding.category6.setOnClickListener {
            navigateToTriviaGameFragment("category_6")
        }
    }

    // Actualiza las clasificaciones por estrellas para cada categoría en la reanudación del fragmento
    override fun onResume() {
        super.onResume()
        setStarRatingForCategory("category_1", binding.starRatingCategory1, sharedPreferences)
        setStarRatingForCategory("category_2", binding.starRatingCategory2, sharedPreferences)
        setStarRatingForCategory("category_3", binding.starRatingCategory3, sharedPreferences)
        setStarRatingForCategory("category_4", binding.starRatingCategory4, sharedPreferences)
        setStarRatingForCategory("category_5", binding.starRatingCategory5, sharedPreferences)
        setStarRatingForCategory("category_6", binding.starRatingCategory6, sharedPreferences)
    }

    // Establece la clasificación por estrellas para una categoría específica, basándose en el valor guardado en SharedPreferences
    private fun setStarRatingForCategory(categoryKey: String, imageView: ImageView, sharedPreferences: SharedPreferences) {
        val starRatingKey = "star_rating_$categoryKey"

        val starDrawableId = when (sharedPreferences.getInt(starRatingKey, 0)) {
            1 -> R.drawable.star_bronze
            2 -> R.drawable.star_silver
            3 -> R.drawable.star_gold
            else -> R.drawable.star_empty
        }

        // Establece la imagen de clasificación por estrellas correspondiente
        imageView.setImageResource(starDrawableId)
    }

    // Navega al fragmento de juego de trivia, pasando el nombre de la categoría como argumento
    private fun navigateToTriviaGameFragment(categoryName: String) {

        // Reproduce el sonido al seleccionar una categoría
        (activity as? SoundPlayable)?.playSoundEffect(requireContext())

        // Crea un nuevo bundle y añade la categoría como una cadena
        val bundle = Bundle().apply {
            putString("categoryName", categoryName)
        }
        // Navega al TriviaGameFragment, pasando el bundle como argumento
        findNavController().navigate(R.id.action_triviaCategoriesFragment_to_triviaGameFragment, bundle)
    }

    // Método para inicializar el binding del fragmento
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCategoriesTriviaBinding.inflate(inflater, container, false)
}
