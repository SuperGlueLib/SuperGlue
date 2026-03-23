package com.github.supergluelib.foundation.database

typealias FieldFlag = SQLQueryBuilder.Field.Flag

class SQLQueryBuilder {

    class CreateTableQuery(private val name: String, private val ifNotExists: Boolean = false) {
        private val types = mutableListOf<Field>()

        fun int(name: String, vararg flags: Field.Flag) = apply { types.add(Field(name, "INT", null, flags.toList())) }
        fun char(name: String, length: Int, vararg flags: Field.Flag) = apply { types.add(Field(name, "CHAR", length, flags.toList())) }
        fun varchar(name: String, length: Int, vararg flags: Field.Flag) = apply { types.add(Field(name, "VARCHAR", length, flags.toList())) }
    }

    class Field(val name: String, val type: String, val length: Int? = null, val flags: List<Flag>) {

        enum class Flag { PRIMARY_KEY, AUTOINCREMENT, UNIQUE, NOT_NULL }

        // TODO: Sort flags by the order they appear in the enum.
        val createTableString get() = "$name $type ${ flags.joinToString(" ") { it.name.replace("_", " ")} }"
    }
}