package com.astronomy.stellar_view.ui.trivia;

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astronomy.stellar_view.data.local.trivia.QuestionEntity
import com.astronomy.stellar_view.data.local.trivia.QuestionsDao;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.launch

@HiltViewModel
class TriviaGameViewModel @Inject constructor(
    // Inyección de dependencias para el DAO y SharedPreferences
    private val questionsDao: QuestionsDao,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    // Lista para almacenar los IDs de las preguntas ya respondidas.
    private var answeredQuestionIds = ArrayList<Int>()

    // LiveData para la entidad de la pregunta actual
    private val _questionEntity = MutableLiveData<QuestionEntity>()
    val questionEntity: LiveData<QuestionEntity> get() = _questionEntity

    // Variable para almacenar la pregunta actual
    private var currentQuestion: QuestionEntity? = null

    init {
        // Carga los IDs de las preguntas respondidas al iniciar la ViewModel.
        loadAnsweredQuestionIds()
    }

    // Establece la pregunta actual
    fun setCurrentQuestion(question: QuestionEntity?) {
        this.currentQuestion = question
    }

    // Obtiene la pregunta actual
    fun getCurrentQuestion(): QuestionEntity? {
        return this.currentQuestion
    }

    // Obtiene una pregunta aleatoria que no ha sido respondida antes
    fun getRandomQuestion(categoryNumber: String, lang: String) {
        viewModelScope.launch {
            val question = questionsDao.getRandomQuestion(categoryNumber, lang, answeredQuestionIds)
            _questionEntity.value = question
        }
    }

    // Obtiene el número de preguntas respondidas en una categoría
    fun getAnsweredQuestionsCounter(categoryName: String): Int {
        val counter = sharedPreferences.getInt(categoryName, 0)
        return if (counter == 0) {
            1
        } else {
            counter
        }
    }

    // Obtiene el número de respuestas correctas en una categoría
    fun getCorrectAnswersCounter(categoryName: String): Int {
        return sharedPreferences.getInt("${categoryName}_correct", 0)
    }

    // Registra que una pregunta ha sido respondida
    fun onQuestionAnswered(questionId: Int, categoryName: String) {
        answeredQuestionIds.add(questionId)
        saveAnsweredQuestionIds()
        val currentCounter = sharedPreferences.getInt(categoryName, 0)
        if (currentCounter == 0) {
            sharedPreferences.edit().putInt(categoryName, 2).apply()
        } else {
            sharedPreferences.edit().putInt(categoryName, currentCounter + 1).apply()
        }
    }

    // Registra que se ha dado una respuesta correcta
    fun onCorrectAnswerGiven(categoryName: String) {
        val currentCounter = sharedPreferences.getInt("${categoryName}_correct", 0)
        sharedPreferences.edit().putInt("${categoryName}_correct", currentCounter + 1).apply()
    }

    // Limpia todos los datos almacenados al iniciar una nueva ronda
    fun onNewRoundStarted(categoryName: String) {
        sharedPreferences.edit().putInt(categoryName, 0).apply()
        sharedPreferences.edit().putInt("${categoryName}_correct", 0).apply()
        answeredQuestionIds.clear()
        sharedPreferences.edit().putStringSet("answered_ids", emptySet()).apply()
    }

    // Guarda los IDs de las preguntas respondidas en SharedPreferences
    private fun saveAnsweredQuestionIds() {
        val answeredIdsSet = answeredQuestionIds.map { it.toString() }.toSet()
        sharedPreferences.edit().putStringSet("answered_ids", answeredIdsSet).apply()
    }

    // Carga los IDs de las preguntas respondidas de SharedPreferences
    private fun loadAnsweredQuestionIds() {
        val answeredIdsSet = sharedPreferences.getStringSet("answered_ids", emptySet())
        answeredQuestionIds = (answeredIdsSet?.map { it.toInt() }?.toMutableList() ?: ArrayList()) as ArrayList<Int>
    }
}
