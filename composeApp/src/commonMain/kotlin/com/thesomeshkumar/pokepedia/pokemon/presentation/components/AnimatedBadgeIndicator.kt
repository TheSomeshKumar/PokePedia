package com.thesomeshkumar.pokepedia.pokemon.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.thesomeshkumar.pokepedia.theme.AppTheme
import kotlin.math.PI
import kotlin.math.sin

/**
 * Reusable animated badge indicator with ripple wave effect
 */
@Composable
fun AnimatedBadgeIndicator(
    color: Color,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions

    // Wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "badge_wave")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                3000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    // Calculate wave offset
    val waveOffset = sin(wavePhase.toDouble() * (PI / 180.0)).toFloat() * 5f

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        // Ripple effect layers
        repeat(3) { index ->
            val rippleScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 2000,
                        delayMillis = index * 400,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "ripple_scale_$index"
            )

            val rippleAlpha by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 2000,
                        delayMillis = index * 400,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "ripple_alpha_$index"
            )

            Box(
                modifier = Modifier
                    .size(
                        dimensions.badgeIndicatorWidth,
                        dimensions.badgeIndicatorHeight
                    )
                    .scale(rippleScale)
                    .alpha(rippleAlpha)
                    .background(
                        color,
                        RoundedCornerShape(dimensions.badgeCornerRadius)
                    )
            )
        }

        Box(
            modifier = Modifier
                .size(
                    dimensions.badgeIndicatorWidth,
                    dimensions.badgeIndicatorHeight
                )
                .offset(y = waveOffset.dp)
                .background(
                    color,
                    RoundedCornerShape(dimensions.badgeCornerRadius)
                )
        )
    }
}
