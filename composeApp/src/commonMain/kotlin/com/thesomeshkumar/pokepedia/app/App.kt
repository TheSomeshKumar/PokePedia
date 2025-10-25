package com.thesomeshkumar.pokepedia.app

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.thesomeshkumar.pokepedia.app.navigation.PokemonDetailRoute
import com.thesomeshkumar.pokepedia.app.navigation.PokemonListRoute
import com.thesomeshkumar.pokepedia.app.navigation.navigateToPokemonDetail
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_detail.PokemonDetailScreen
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonListScreen
import com.thesomeshkumar.pokepedia.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun App() {
    AppTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = PokemonListRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<PokemonListRoute>(
                enterTransition = {
                    fadeIn(animationSpec = tween(300)) +
                    scaleIn(
                        initialScale = 0.95f,
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(200)) +
                    scaleOut(
                        targetScale = 0.95f,
                        animationSpec = tween(200)
                    )
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(300)) +
                    scaleIn(
                        initialScale = 0.95f,
                        animationSpec = tween(300)
                    )
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(200))
                }
            ) {
                PokemonListScreen(
                    onPokemonClick = { pokemon ->
                        navController.navigateToPokemonDetail(pokemon)
                    },
                    modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                )
            }

            composable<PokemonDetailRoute>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(400)
                    ) + fadeIn(animationSpec = tween(400))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(400)
                    ) + fadeOut(animationSpec = tween(400))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(300))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(400)
                    ) + fadeOut(animationSpec = tween(400))
                }
            ) { backStackEntry ->
                val pokemonDetailRoute: PokemonDetailRoute = backStackEntry.toRoute()
                PokemonDetailScreen(
                    pokemonId = pokemonDetailRoute.pokemonId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNavigateToPokemon = { pokemonId ->
                        navController.navigateToPokemonDetail(pokemonId)
                    }
                    // No window insets padding here to allow edge-to-edge
                )
            }
        }
    }
}
