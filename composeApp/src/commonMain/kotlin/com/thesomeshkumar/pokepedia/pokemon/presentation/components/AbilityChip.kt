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
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonAbilityUI
import com.thesomeshkumar.pokepedia.theme.AppTheme

@Composable
fun AbilityChip(
    ability: PokemonAbilityUI,
    modifier: Modifier = Modifier
) {
    val chipColor = if (ability.isHidden) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.primary
    }
    val dimensions = AppTheme.dimensions

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        chipColor,
                        chipColor.copy(alpha = 0.8f)
                    )
                )
            )
            .padding(
                horizontal = dimensions.chipPaddingHorizontal,
                vertical = dimensions.chipPaddingVertical
            )
    ) {
        Text(
            text = if (ability.isHidden) "${ability.displayName} ✨" else ability.displayName,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}

