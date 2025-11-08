package com.thesomeshkumar.pokepedia.pokemon.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.thesomeshkumar.pokepedia.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import pokepedia.composeapp.generated.resources.Res
import pokepedia.composeapp.generated.resources.clear_search
import pokepedia.composeapp.generated.resources.search_label
import pokepedia.composeapp.generated.resources.search_pokemon_hint

/**
 * Enhanced SearchBar component following Material Design 3 principles.
 * Features:
 * - Smooth animations for clear button
 * - Proper Material 3 theming
 * - Enhanced visual design with elevation
 * - Consistent spacing using app dimensions
 * - Improved accessibility
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    focusRequester: FocusRequester? = null
) {
    val dimensions = AppTheme.dimensions
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        enabled = enabled,
        placeholder = {
            Text(
                text = placeholder ?: stringResource(Res.string.search_pokemon_hint),
                style = typography.bodyLarge,
                color = colors.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(Res.string.search_label),
                tint = colors.onSurfaceVariant
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(150)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(150)
                ),
                exit = fadeOut(animationSpec = tween(100)) + scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(100)
                )
            ) {
                IconButton(
                    onClick = onClearClick,
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(Res.string.clear_search),
                        tint = colors.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(dimensions.spaceExtraLarge),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.outline.copy(alpha = 0.5f),
            focusedTextColor = colors.onSurface,
            unfocusedTextColor = colors.onSurface,
            cursorColor = colors.primary,
            focusedPlaceholderColor = colors.onSurfaceVariant.copy(alpha = 0.6f),
            unfocusedPlaceholderColor = colors.onSurfaceVariant.copy(alpha = 0.6f),
            disabledBorderColor = colors.outline.copy(alpha = 0.3f),
            disabledTextColor = colors.onSurface.copy(alpha = 0.6f),
//            disabledPlaceholderColor = colors.onSurfaceVarianiant.copy(alpha = 0.4f)
        ),
        textStyle = typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
            .shadow(
                elevation = if (query.isNotEmpty()) 2.dp else 0.dp,
                shape = RoundedCornerShape(dimensions.spaceExtraLarge),
                spotColor = colors.primary.copy(alpha = 0.1f)
            )
            .then(
                if (focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else {
                    Modifier
                }
            )
    )
}

