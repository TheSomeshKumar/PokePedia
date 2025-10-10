package com.thesomeshkumar.pokepedia.pokemon.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<PokemonDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(DB_NAME)

        return Room.databaseBuilder<PokemonDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        ).apply {
            // Enable Database Inspector support
            // Using TRUNCATE mode instead of WAL for better Database Inspector compatibility
            setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            
            // Fallback to destructive migration for development
            fallbackToDestructiveMigration(dropAllTables = true)
        }
    }

    companion object {
        const val DB_NAME = "pokemon.db"
    }
}
