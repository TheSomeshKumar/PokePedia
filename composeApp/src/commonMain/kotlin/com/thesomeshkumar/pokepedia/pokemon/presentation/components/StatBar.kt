package com.thesomeshkumar.pokepedia.pokemon.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonStatUI
import com.thesomeshkumar.pokepedia.theme.AppTheme

@Composable
fun StatBar(
    stat: PokemonStatUI,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = stat.progressValue,
        animationSpec = tween(durationMillis = 600),
        label = "stat_progress"
    )

    val pokemonColors = AppTheme.pokemonColors
    val dimensions = AppTheme.dimensions
    
    val statColor = when {
        stat.baseStat >= 100 -> pokemonColors.statExcellent
        stat.baseStat >= 70 -> pokemonColors.statGood
        stat.baseStat >= 50 -> pokemonColors.statAverage
        else -> pokemonColors.statLow
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stat.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(statColor.copy(alpha = 0.15f))
                    .padding(
                        horizontal = dimensions.spaceSmall,
                        vertical = dimensions.spaceExtraSmall / 2
                    )
            ) {
                Text(
                    text = stat.baseStat.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = statColor
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensions.spaceSmall))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.progressBarHeight)
                .clip(RoundedCornerShape(dimensions.progressBarCornerRadius))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(dimensions.progressBarCornerRadius))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                statColor,
                                statColor.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
        }
    }
}

