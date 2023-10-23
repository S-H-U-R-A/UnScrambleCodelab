package com.example.unscramble.ui

/**
 * Clase de datos que representa el estado de la UI del juego
 *
 * @property currentScrambledWord representa la palabra actual que debe ser mostrada al usuario
 */
data class GameUiState(
    val currentScrambledWord: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false
)
