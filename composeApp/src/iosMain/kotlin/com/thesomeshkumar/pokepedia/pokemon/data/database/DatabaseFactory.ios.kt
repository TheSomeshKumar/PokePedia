package com.thesomeshkumar.pokepedia.pokemon.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<PokemonDatabase> {
        val dbFile = documentDirectory() + "/$DB_NAME"
        return Room.databaseBuilder<PokemonDatabase>(
            name = dbFile
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        return requireNotNull(documentDirectory?.path)
    }

    companion object {
        const val DB_NAME = "pokemon.db"
    }
}
