package com.thesomeshkumar.pokepedia.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Created by Somesh Kumar on 26 July, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
@Immutable
sealed interface UiText {
    @Immutable
    data class DynamicString(val value: String) : UiText
    
    @Immutable
    data class StringResourceId(
        val id: StringResource,
        val args: List<Any> = emptyList()
    ) : UiText {
        // Secondary constructor for varargs compatibility
        constructor(id: StringResource, vararg args: Any) : this(id, args.toList())
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResourceId -> stringResource(
                resource = id,
                formatArgs = args.toTypedArray()
            )
        }
    }
}