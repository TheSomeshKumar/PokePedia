package com.thesomeshkumar.pokepedia.core.domain

/**
 * Created by SixFlags on 26 July, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

sealed interface DataError : Error {
    enum class Remote : DataError {
        REQUEST_TIMEOUT, TOO_MANY_REQUESTS, NO_INTERNET, SERVER, SERIALIZATION, UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL, UNKNOWN
    }
}