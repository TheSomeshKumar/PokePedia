package com.thesomeshkumar.pokepedia.core.presentation

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Created by SixFlags on 26 July, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */
sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    class StringResourceId(
        val id: StringResource,
        val args: Array<Any> = arrayOf()
    ) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResourceId -> stringResource(
                resource = id,
                formatArgs = args
            )
        }
    }
}