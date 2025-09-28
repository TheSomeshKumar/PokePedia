package com.thesomeshkumar.pokepedia.pokemon.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */
actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<PokemonDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(DB_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }

    companion object {
        const val DB_NAME = "pokemon.db"
    }
}
