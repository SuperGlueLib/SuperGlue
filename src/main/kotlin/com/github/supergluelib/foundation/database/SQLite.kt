package com.github.supergluelib.foundation.database

import java.sql.DriverManager

/**
 * Handles an instance of a SQLite Database, Make sure to close it before the server shuts down.
 * Automatically intialises a connection when the class is created
 */
class SQLiteDatabase(val filePath: String) {
    val connection = DriverManager.getConnection("jdbc:sqlite:$filePath")
    // TODO: return auto generated keys with prepareStatement(string, int)

    // Convenience methods
    fun prepareStatement(sql: String) = connection.prepareStatement(sql)

    // Connection Management
    fun close() = connection.close()
}