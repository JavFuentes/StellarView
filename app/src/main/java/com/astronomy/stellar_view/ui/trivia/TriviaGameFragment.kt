package com.astronomy.stellar_view.ui.trivia

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.astronomy.stellar_view.R
import com.astronomy.stellar_view.ui.base.BaseFragment
import com.astronomy.stellar_view.databinding.FragmentGameTriviaBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class TriviaGameFragment : BaseFragment<FragmentGameTriviaBinding>() {

    private val viewModel: TriviaGameViewModel by viewModels()

    private var correctSoundPlayer: MediaPlayer? = null
    private var wrongSoundPlayer: MediaPlayer? = null

    private var buttonPressed = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtiene la categoría actual del SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("TriviaPreferences", Context.MODE_PRIVATE)
        val categoryNameKey = arguments?.getString("categoryName") ?: sharedPreferences.getString("categoryNamePersistent", null)

        // Almacena persistentemente la categoría en caso de que exista una categoría nueva
        if (categoryNameKey != null) {
            sharedPreferences.edit().putString("categoryNamePersistent", categoryNameKey).apply()
        }

        // Obtiene el nombre de la categoría a través del identificador
        val categoryNameResId = resources.getIdentifier(categoryNameKey, "string", requireContext().packageName)
        val categoryName = getString(categoryNameResId)
        binding.tvCategory.text = categoryName
        val categoryNumber = categoryNameKey?.substring(categoryNameKey.indexOf('_') + 1)?.toInt()

        // Actualiza el contador de preguntas respondidas
        val counter = viewModel.getAnsweredQuestionsCounter(categoryName)
        binding.tvCounter.text = counter.toString()

        // Inicializa los reproductores de sonido para las respuestas correctas e incorrectas
        correctSoundPlayer = MediaPlayer.create(context, R.raw.ok_sfx)
        wrongSoundPlayer = MediaPlayer.create(context, R.raw.wrong_sfx)
        correctSoundPlayer?.setVolume(1.0f, 1.0f)
        wrongSoundPlayer?.setVolume(1.0f, 1.0f)

        // Obtiene el idioma actual del dispositivo
        val lang = when (Locale.getDefault().language) {
            "es" -> "ES"
            "it" -> "IT"
            else -> "EN"
        }

        // Obtiene una pregunta aleatoria si no hay una pregunta actual
        if (viewModel.getCurrentQuestion() == null) {
            viewModel.getRandomQuestion(categoryNumber.toString(), lang)
        }

        // Observa el LiveData de la pregunta y actualiza la interfaz de usuario cuando cambia
        viewModel.questionEntity.observe(viewLifecycleOwner) { questionEntity ->
            if (questionEntity != null) {
                // Establece la pregunta actual en el ViewModel
                viewModel.setCurrentQuestion(questionEntity)

                // Actualiza el texto de la pregunta y las opciones de respuesta
                binding.tvQuestion.text = questionEntity.question

                val options = mutableListOf(questionEntity.opt1, questionEntity.opt2, questionEntity.opt3, questionEntity.opt4)
                options.shuffle()

                binding.btnOptionA.text = options[0]
                binding.btnOptionB.text = options[1]
                binding.btnOptionC.text = options[2]
                binding.btnOptionD.text = options[3]

                val okAnimationView = binding.okAnimation
                val wrongAnimationView = binding.wrongAnimation

                // Configura los listeners de los botones de respuesta
                listOf(binding.btnOptionA, binding.btnOptionB, binding.btnOptionC, binding.btnOptionD).forEach { button ->
                    button.setOnClickListener {

                        // Si un botón ya ha sido presionado, sal del listener
                        if (buttonPressed) return@setOnClickListener

                        buttonPressed = true

                        // Marca la pregunta como respondida
                        viewModel.onQuestionAnswered(questionEntity.id_question, categoryName)

                        // Deshabilita los botones para evitar respuestas múltiples
                        binding.btnOptionA.isClickable = false
                        binding.btnOptionB.isClickable = false
                        binding.btnOptionC.isClickable = false
                        binding.btnOptionD.isClickable = false

                        // Verifica si la respuesta es correcta y muestra la animación y sonido correspondientes
                        if (button.text == questionEntity.opt1) {
                            okAnimationView.visibility = View.VISIBLE
                            okAnimationView.playAnimation()
                            correctSoundPlayer?.start()

                            // Incrementa el contador de respuestas correctas
                            viewModel.onCorrectAnswerGiven(categoryName)
                        } else {
                            wrongAnimationView.visibility = View.VISIBLE
                            wrongAnimationView.playAnimation()
                            wrongSoundPlayer?.start()
                        }

                        // Cambia el color de las opciones incorrectas a blanco
                        listOf(binding.btnOptionA, binding.btnOptionB, binding.btnOptionC, binding.btnOptionD).forEach { otherButton ->
                            if (otherButton.text != questionEntity.opt1) {
                                val color = ContextCompat.getColor(requireContext(), R.color.white)
                                otherButton.backgroundTintList = ColorStateList.valueOf(color)
                            }
                        }

                        button.postDelayed({

                            buttonPressed = false

                            // Verifica si se ha respondido a todas las preguntas y muestra el fragmento de resultados
                            val answeredQuestionsCounter = viewModel.getAnsweredQuestionsCounter(categoryName)
                            val correctAnswersCounter = viewModel.getCorrectAnswersCounter(categoryName)

                            if (answeredQuestionsCounter > 10) {
                                val newFragment = TriviaResultFragment()

                                val args = Bundle()
                                args.putInt("correctAnswers", correctAnswersCounter)
                                args.putString("categoryKey", categoryNameKey)
                                newFragment.arguments = args

                                val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                                transaction.replace(R.id.mainContainer, newFragment)
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                transaction.commit()

                                // Reinicia el juego para la nueva ronda
                                viewModel.onNewRoundStarted(categoryName)
                            }
                            else {
                                // Recarga el fragmento del juego para la siguiente pregunta
                                val newFragment = TriviaGameFragment()
                                val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                                transaction.replace(R.id.mainContainer, newFragment)
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                transaction.commit()
                            }
                        }, 3000)
                    }
                }
            } else {
                // Muestra un Snackbar en caso de error al cargar la pregunta
                Snackbar.make(binding.root, getString(R.string.local_db_error_alert), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    // Esconde las animaciones al reanudar el fragmento
    override fun onResume() {
        super.onResume()
        // Esconde las animaciones al reanudar el fragmento
        binding.okAnimation.visibility = View.GONE
        binding.wrongAnimation.visibility = View.GONE

        // Bloquea la orientación de la pantalla en la dirección actual
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }
    }

    override fun onPause() {
        super.onPause()
        // Desbloquea la orientación de la pantalla cuando el fragmento se pausa
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    // Inflates the layout for this fragment
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentGameTriviaBinding.inflate(inflater, container, false)
}
