package com.example.unscramble.ui

/**
 * Clase de datos que representa el estado de la UI del juego
 *
 * @property currentScrambledWord representa la palabra actual que debe ser mostrada al usuario
 * @property currentWordCount representa el contador de palabras que ya se usarón en el juego
 * @property score representa el puntaje actual del usuario
 * @property isGuessedWordWrong representa el estado de error al adivinar la palabra
 * @property isGameOver representa el estado de juego cuando ya llego al limite de palabras permitidas
 */
data class GameUiState(
    val currentScrambledWord: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false
)
