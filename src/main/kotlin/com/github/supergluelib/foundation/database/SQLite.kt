package com.github.supergluelib.foundation.database

import java.io.File
import java.sql.DriverManager

/**
 * Handles an instance of a SQLite Database, Make sure to close it before the server shuts down.
 * Automatically intialises a connection when the class is created
 */
class SQLiteDatabase(val file: File) {
    private val filePath = file.apply { parentFile.mkdirs() }.path
    val connection = DriverManager.getConnection("jdbc:sqlite:$filePath")
    // TODO: return auto generated keys with prepareStatement(string, int)

    // Convenience methods
    fun prepareStatement(sql: String) = connection.prepareStatement(sql)

    // Connection Management
    fun close() = connection.close()

    /** @return a pair with this object to it's associated class object, useful for bypassing type erasure */
    fun <T: Any> T.toClassPair() = this to this::class.java
}