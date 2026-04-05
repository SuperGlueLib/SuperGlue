package com.github.supergluelib.foundation.database

import com.github.supergluelib.foundation.Foundations
import java.io.File
import java.sql.DriverManager

/**
 * Represents a connection to an SQLite database.
 * - A connection is created when this class is initialised.
 * - Nothing is executed until [start] is called
 * - Make sure to run [close] when closing your connection to the database or in your onDisable.
 */
abstract class SQLiteDatabase(name: String) {
//    val conn = SQLiteConnection(File(path))
    private val parentFile = Foundations.plugin.dataFolder.apply { mkdirs() }
    private val file = File(Foundations.plugin.dataFolder, name).apply { createNewFile() }
    protected val connection = DriverManager.getConnection("jdbc:sqlite:${file.path}")


    // TODO auto versioning schema & migrations
    abstract fun createTables()
    fun runMigrations() {}

    /** Run this to initialise tables, run migrations, etc. */
    open fun start() {
        createTables()
        runMigrations()
    }

    open fun close() {
        connection.close()
    }
}