package com.thesomeshkumar.pokepedia.pokemon.presentation.components

import androidx.compose.ui.graphics.Color

/**
 * Helper function to parse hex color strings
 */
fun parseColorHex(colorHex: String): Color {
    return try {
        val hex = if (colorHex.startsWith("#")) colorHex else "#$colorHex"
        Color(
            hex
                .removePrefix("#")
                .toLong(16) or 0xFF000000
        )
    } catch (e: Exception) {
        Color.Gray
    }
}

