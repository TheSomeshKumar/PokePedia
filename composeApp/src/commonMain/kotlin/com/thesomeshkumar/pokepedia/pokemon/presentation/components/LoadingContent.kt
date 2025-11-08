package com.thesomeshkumar.pokepedia.pokemon.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pokepedia.composeapp.generated.resources.Res
import pokepedia.composeapp.generated.resources.loading_pokemon

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingContent(
    message: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularWavyProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message ?: stringResource(Res.string.loading_pokemon),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

