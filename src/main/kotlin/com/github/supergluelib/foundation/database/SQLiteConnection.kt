package com.github.supergluelib.foundation.database

import java.io.File
import java.sql.Connection
import java.sql.DriverManager

/**
 * Handles an instance of a SQLite Database, Make sure to close it before the server shuts down.
 * Automatically intialises a connection when the class is created
 */
class SQLiteConnection(val file: File) {
    private val filePath = file.apply { parentFile.mkdirs() }.path
    val connection = DriverManager.getConnection("jdbc:sqlite:$filePath")
    // TODO: return auto generated keys with prepareStatement(string, int)

    // Convenience methods
    fun prepareStatement(sql: String) = connection.prepareStatement(sql)
    fun executeOnce(sql: String) = connection.createStatement().execute(sql)

    // MIGRATIONS
    /*
        Flow:
        - Create Schema Version table
        - on startup, check or create schema version table
        - if (version < 2) thenMigration1()
        - if (version < 3) thenMigration2()
        - if version not up to date, update it in the table
        - NOW load regular stuff.
     */
    // USE TRANSACTIONS WHEN COMMITING MULTIPLE THINGS THAT NEED TO SUCCEED OR FAIL TOGETHER

    // BATCH VS TRANSACTION
    // BATCH: Groups network requests (messages) to be sent from client -> database, so all 100 operations would be sent in one request (great if latency on the network)
    // TRANSACTION: Changes the actual behaviour of the db, without it, a batch is still committed to disk on each OP, but with it, all 100 OPs are commited at once (great for sqlite because of disk IO)
    // Always use prepared statements - they're faster & protect against sql injection
    // INDEXES - USE THEM "EXPLAIN QUERY PLAN" TO SEE IF THEY'RE BEING USED CORRECTLY
    // N+1 QUERIES ISSUE: query minions table to get all IDs and then query another table 1 by 1 with each id to get data about said minion. USE A JOIN
    // SOFT-DELETES: Use a deleted/deleted_at column or even an additional table instead of actually deleting data so you have audits/logs/rollbacks etc.

    /*
    The things that would naturally come next in your project:

    Query builders vs raw SQL vs ORMs — understanding where each fits
    Database testing — how to write tests against a DB without polluting real data (in-memory SQLite is great for this)
    Logging slow queries — knowing when something is performing badly before your users tell you

    The indexing and N+1 topics are probably the most immediately practical — they're the source of the majority of real-world database performance problems.
     */

}