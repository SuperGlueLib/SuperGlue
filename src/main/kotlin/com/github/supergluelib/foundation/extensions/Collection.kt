package com.github.supergluelib.foundation.extensions


/** @return whether the collection contains the provided string, ignoring case */
fun Iterable<String>.containsIgnoreCase(other: String) = any { other.equals(it, true) }
/** @return whether the array contains the provided string, ignoring case */
fun Array<out String>.containsIgnoreCase(other: String) = any { other.equals(it, true)}

/** @return a list containing the elements with indices within the supplied range */
operator fun Array<out Any>.get(range: IntRange) = this.withIndex().filter { range.contains(it.index) }
/** @return a subset of this array from the begin index to the end index */
fun Array<out Any>.sublist(begin: Int, end: Int = size) = Array(end-begin) { this[it + begin] }

/** Fluent method to convert a map to a hashmap */
fun <K, V> Map<K, V>.toHashMap() = HashMap(this)