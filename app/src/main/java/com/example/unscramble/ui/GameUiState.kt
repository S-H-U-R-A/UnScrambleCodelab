package com.example.unscramble.ui

/**
 * Clase de datos que representa el estado de la UI del juego
 *
 * @property currentScrambledWord representa la palabra actual que debe ser mostrada al usuario
 */
data class GameUiState(
    val currentScrambledWord: String = ""
)
