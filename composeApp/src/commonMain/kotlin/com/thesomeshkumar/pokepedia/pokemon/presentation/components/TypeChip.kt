package com.thesomeshkumar.pokepedia.pokemon.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonTypeUI

@Composable
fun TypeChip(
    type: PokemonTypeUI,
    modifier: Modifier = Modifier
) {
    val typeColor = parseColorHex(type.colorHex)
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        typeColor,
                        typeColor.copy(alpha = 0.85f)
                    )
                )
            )
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
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

