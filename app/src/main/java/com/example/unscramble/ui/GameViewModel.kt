package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel(){

    /**
     * _ui state es un flujo mutable de estados de juego
     */
    private val _uiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> get() = _uiState.asStateFlow()

    /**
     * Estado inicial de la palabra que adivina el usuario
     */
    var userGuess: String by mutableStateOf("")
        private set

    /**
     * Used words conjunto de palabras que ya fueron usadas en el juego
     */
    private var usedWords: MutableSet<String> = mutableSetOf()

    /**
     * Current word palabra actual mostrada en el juego
     */
    private lateinit var currentWord: String

    init {
        resetGame()
    }

    /**
     * Reset game
     *
     * Se encarga de reiniciar el juego, limpiando el conjunto de palabras usadas y asignando una nueva palabra desordenada
     *
     */
    fun resetGame() {

        usedWords.clear()//Se limpia el conjunto de palabras usadas

        _uiState.value = GameUiState(//Asignamos un nuevo estado al flujo de estados
            currentScrambledWord = pickRandomWordAndShuffle()
        )

    }

    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    fun checkUserGuess(){
        if ( userGuess.equals(currentWord, ignoreCase = true) ){
            val updateScore = _uiState.value.score.plus(SCORE_INCREASE)//Incrementamos el valor del score
            updateGameState(updateScore)//Solicitamos que se actualice el estado de la Ui.
        }else{
            _uiState.update { currentStateUi ->
                currentStateUi.copy(
                    isGuessedWordWrong = true
                )
            }
        }
        updateUserGuess("")//Se restablece el valor de la caja de texto del usuario
    }

    private fun updateGameState(updatedScore: Int) {
        if(usedWords.size == MAX_NO_OF_WORDS){
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        }else{
            _uiState.update { currentStateUi ->
                currentStateUi.copy(
                    isGuessedWordWrong = false,
                    currentWordCount = currentStateUi.currentWordCount.inc(),
                    score = updatedScore,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                )
            }
        }

    }

    fun skipWord(){
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }

    /**
     * Pick random word and shuffle
     *
     * @return devuelve una palabra aleatoria de la lista de palabras, verificando que no se haya usado antes
     */
    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()//obtenemos una palabra aleatoria de la lista de palabras

        return if (usedWords.contains(currentWord)) {//Si el conjunto de palabras usadas contiene la palabra aleatoria, se vuelve a llamar la funcion
             pickRandomWordAndShuffle()
        } else {
            // De lo contrario, se agrega la palabra al conjunto de palabras usadas y se desordena
            usedWords.add(currentWord)
            shuffleCurrentWord(currentWord)//Se retorna la palabra desordenada
        }
    }

    /**
     * Shuffle current word
     *
     * @param word palabra a desordenar
     * @return la palabra dada desordenada
     */
    private fun shuffleCurrentWord(word: String): String {

        val tempWord = word.toCharArray()// Se convierte la palabra en un arreglo de caracteres

        tempWord.shuffle()//Desordena el arreglo de caracteres

        while ( String(tempWord) == word ) {//Si despues de desordenar la palabra sigue siendo la misma, se vuelve a desordenar
            tempWord.shuffle()
        }

        return String(tempWord)//retornamos la palabra desordenada
    }



}