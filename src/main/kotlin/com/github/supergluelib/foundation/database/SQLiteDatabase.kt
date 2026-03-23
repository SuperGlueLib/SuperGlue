package com.github.supergluelib.foundation.database

import java.io.File
import java.sql.DriverManager

/**
 * Represents a connection to an SQLite database.
 * - A connection is created when this class is initialised.
 * - Nothing is executed until [start] is called
 * - Make sure to run [close] when closing your connection to the database or in your onDisable.
 */
abstract class SQLiteDatabase(path: String) {
//    val conn = SQLiteConnection(File(path))
    private val file = File(path).apply { parentFile.mkdirs() }
    protected val connection = DriverManager.getConnection("jdbc:sqlite:$path")


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