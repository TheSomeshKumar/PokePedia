package com.thesomeshkumar.pokepedia.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Dimension values following Material Design 3 spacing scale
 * https://m3.material.io/foundations/layout/applying-layout/spacing
 */
@Immutable
data class Dimensions(
    // Spacing scale
    val spaceExtraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceMedium: Dp = 12.dp,
    val spaceLarge: Dp = 16.dp,
    val spaceExtraLarge: Dp = 20.dp,
    val spaceXXLarge: Dp = 24.dp,
    
    // Component specific sizes
    val chipPaddingHorizontal: Dp = 16.dp,
    val chipPaddingVertical: Dp = 10.dp,
    val chipCornerRadius: Dp = 16.dp,
    
    val cardPadding: Dp = 20.dp,
    val cardCornerRadius: Dp = 20.dp,
    val cardElevation: Dp = 0.dp,
    
    // Icon and Image sizes
    val iconSizeSmall: Dp = 16.dp,
    val iconSizeMedium: Dp = 24.dp,
    val iconSizeLarge: Dp = 32.dp,
    val imageSizeSmall: Dp = 80.dp,
    val imageSizeMedium: Dp = 90.dp,
    val imageSizeLarge: Dp = 120.dp,
    val imageSizeExtraLarge: Dp = 150.dp,
    
    // Progress bar
    val progressBarHeight: Dp = 10.dp,
    val progressBarCornerRadius: Dp = 5.dp,
    
    // Badge and decorative elements
    val badgeIndicatorWidth: Dp = 4.dp,
    val badgeIndicatorHeight: Dp = 24.dp,
    val badgeCornerRadius: Dp = 2.dp,
    
    // Hero section
    val heroSectionHeight: Dp = 300.dp,
    val heroTopPadding: Dp = 80.dp,
    
    // Gradient overlay
    val gradientOverlayHeight: Dp = 8.dp
)

internal val LocalDimensions = staticCompositionLocalOf { Dimensions() }

