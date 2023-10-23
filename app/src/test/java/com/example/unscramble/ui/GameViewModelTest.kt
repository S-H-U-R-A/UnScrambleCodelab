package com.example.unscramble.ui

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest{

    //Obtenemos una instancia del viewmodel
    private val viewModel: GameViewModel = GameViewModel()//Para cada función que use el viewmodel se crea una nueva instancia

    /**
     * Prueba de caso limite, cuando arranca la app, se debe cargar la primera palabra
     *
     */
    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {

        //GIVEN - Dado que la app arranca y obtengo el estado de la UI
        val gameUiState = viewModel.uiState.value

        //WHEN - Cuando obtengo la palabra desordenada del archivo de data de prueba
        val unScrambledWord = getUnscrambledWord( gameUiState.currentScrambledWord )

        //THEN - Verificamos cuales son los estados iniciales que debería tener el data class que representa el estado de la UI.

        assertNotEquals(unScrambledWord, gameUiState.currentScrambledWord)//Se verifica que la palabra desordenada no sea igual a la palabra original

        assertTrue(gameUiState.currentWordCount == 1) //Verifica que el contador de palabras sea 1

        assertTrue(gameUiState.score == 0)//Verificamos que el puntaje sea 0

        assertFalse(gameUiState.isGuessedWordWrong)//Verificamos que el estado de error al adivinar la palabra sea falso

        assertFalse(gameUiState.isGameOver)//Verificamos que el valor del estado de Ui que indica que el juego termino sea falso

    }

    /**
     * Prueba de caso limite, cuando el usuario a jugado todas las veces permitidas
     *
     */
    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        //GIVEN
        var expectedScore = 0

        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            // Assert that after each correct answer, score is updated correctly.
            assertEquals(expectedScore, currentGameUiState.score)
        }
        // Assert that after all questions are answered, the current word count is up-to-date.
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)
        // Assert that after 10 questions are answered, the game is over.
        assertTrue(currentGameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        // Given an incorrect word as input
        val incorrectPlayerWord = "and"

        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiState.value
        // Assert that score is unchanged
        assertEquals(0, currentGameUiState.score)
        // Assert that checkUserGuess() method updates isGuessedWordWrong correctly
        assertTrue(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        currentGameUiState = viewModel.uiState.value

        // Assert that checkUserGuess() method updates isGuessedWordWrong is updated correctly.
        assertFalse(currentGameUiState.isGuessedWordWrong)
        // Assert that score is updated correctly.
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {

        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        currentGameUiState = viewModel.uiState.value
        val lastWordCount = currentGameUiState.currentWordCount

        viewModel.skipWord()
        currentGameUiState = viewModel.uiState.value

        // Assert that score remains unchanged after word is skipped.
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
        // Assert that word count is increased by 1 after word is skipped.
        assertEquals(lastWordCount + 1, currentGameUiState.currentWordCount)

    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}