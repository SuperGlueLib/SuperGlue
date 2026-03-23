package com.github.supergluelib.foundation.database

import java.io.File

abstract class SQLiteDatabase(private val path: String) {
    val conn = SQLiteConnection(File(path))

    // TODO auto versioning schema & migrations
    abstract fun createTables()
    fun runMigrations() {}

    open fun start() {
        createTables()
        runMigrations()
    }

    open fun close() {
        conn.close()
    }
}