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

/** Works the same as [associate] but omits transformations of which the keys result in null */
fun <T, K, V> Iterable<T>.associateNotNullKeys(transform: (T) -> Pair<K?, V>): Map<K, V> {
    val newmap = LinkedHashMap<K, V>()
    for (element in this) {
        val transformation = transform(element)
        if (transformation.first != null)
            newmap[transformation.first!!] = transformation.second
    }
    return newmap
}

/**
 * Acts the same as [Collection.filter] but allows the result to be null,
 * the returned list only contains values which returned true, not false or null
 */
fun <T> Iterable<T>.filterTrue(predicate: (T) -> Boolean?) = filter { predicate.invoke(it) == true }

/** @return the element immediately after the provided element. e.g. B in ("A", "B", "C").elementAfter("A") */
fun <T> List<T>.elementAfter(ele: T) = elementAtOrNull((indexOf(ele).takeIf { it != -1 } ?: -2) + 1)

/** @return the element immediately before the provided element. e.g. "A" in ("A", "B", "C").elementBefore("B") */
fun <T> List<T>.elementBefore(ele: T) = elementAtOrNull(indexOf(ele) - 1)