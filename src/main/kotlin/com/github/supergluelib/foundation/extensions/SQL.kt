package com.github.supergluelib.foundation.extensions

import com.github.supergluelib.foundation.Foundations
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*

/**
 * uses [PreparedStatement.setObject] to fill in the placeholders for this [PreparedStatement]
 */
fun PreparedStatement.prepare(vararg params: Pair<*, Class<*>>) = apply {
    (1..params.size).forEach { set(it, params[it - 1].first!!, params[it-1].second) }
}

/** uses [prepare] to populate the placeholders then returns the [ResultSet] returned from executing the query.*/
fun PreparedStatement.executeQuery(vararg params: Pair<*, Class<*>>) = prepare(*params).executeQuery()

/** uses [prepare] to populate the placeholders then returns the affected row count. */
fun PreparedStatement.executeUpdate(vararg params: Pair<*, Class<*>>) = prepare(*params).executeUpdate()

private fun PreparedStatement.set(num: Int, obj: Any, clazz: Class<*>) = when {
    java.lang.String::class.java.isAssignableFrom(clazz) -> setString(num, obj as String)
    java.lang.Integer::class.java.isAssignableFrom(clazz) -> setInt(num, obj as Int)
    java.lang.Double::class.java.isAssignableFrom(clazz) -> setDouble(num, obj as Double)
    java.lang.Boolean::class.java.isAssignableFrom(clazz) -> setBoolean(num, obj as Boolean)
    java.lang.Float::class.java.isAssignableFrom(clazz) -> setFloat(num, obj as Float)
    java.lang.Long::class.java.isAssignableFrom(clazz) -> setLong(num, obj as Long)
    java.lang.Short::class.java.isAssignableFrom(clazz) -> setShort(num, obj as Short)
    UUID::class.java.isAssignableFrom(clazz) -> setString(num, (obj as UUID).toString())
    else -> { Foundations.plugin.logger.warning("Couldn't pass $obj to prepared statement, unsupported type ${obj::class.java.name}") }
}