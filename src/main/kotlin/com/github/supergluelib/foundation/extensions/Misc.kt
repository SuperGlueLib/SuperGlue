package com.github.supergluelib.foundation.extensions

fun Enum<*>.formatName() = toString()
    .split("_")
    .joinToString(" ") {
        it.replaceFirstChar { it.uppercaseChar() }
    }