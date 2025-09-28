package com.thesomeshkumar.pokepedia.pokemon.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */
actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<PokemonDatabase> {
        val os = System
            .getProperty("os.name")
            .lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(
                System.getenv("APPDATA"),
                "PokemonApp"
            )

            os.contains("mac") -> File(
                userHome,
                "Library/Application Support/PokemonApp"
            )

            else -> File(
                userHome,
                ".local/share/PokemonApp"
            )
        }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(
            appDataDir,
            DB_NAME
        )
        return Room.databaseBuilder(dbFile.absolutePath)
    }

    companion object {
        const val DB_NAME = "pokemon.db"
    }
}
