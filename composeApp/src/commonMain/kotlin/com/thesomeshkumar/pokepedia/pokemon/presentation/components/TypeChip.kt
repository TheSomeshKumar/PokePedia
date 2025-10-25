package com.thesomeshkumar.pokepedia.pokemon.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonTypeUI
import com.thesomeshkumar.pokepedia.theme.AppTheme

@Composable
fun TypeChip(
    type: PokemonTypeUI,
    modifier: Modifier = Modifier
) {
    val typeColor = parseColorHex(type.colorHex)
    val dimensions = AppTheme.dimensions
    
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        typeColor,
                        typeColor.copy(alpha = 0.85f)
                    )
                )
            )
            .padding(
                horizontal = dimensions.spaceExtraLarge,
                vertical = dimensions.chipPaddingVertical
            )
    ) {
        Text(
            text = type.displayName,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

