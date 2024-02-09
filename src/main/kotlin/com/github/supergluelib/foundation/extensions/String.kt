package com.github.supergluelib.foundation.extensions

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import java.util.*

/** Convert a string to an int, or supply the default if the string is not an int */
fun String.toIntOrElse(default: Int): Int = this.toIntOrNull() ?: default
/** @return Whether the string represents an int such as "32" */
fun String.isInt() = toIntOrNull() != null
/** Returns the [EntityType] represented by this string, or null if no match is found */
fun String.toEntityTypeOrNull() = runCatching { EntityType.valueOf(this) }.getOrNull()
/** Returns the [Material] represented by this string, or null if no match is found */
fun String.toMaterialOrNull() = Material.matchMaterial(this)
/** Converts this string to a UUID, or returns null if this string does not represent a UUID */
fun String.toUUID() = runCatching { UUID.fromString(this) }.getOrNull()
/** @return whether the string matches exactly the name of a currently online player */
fun String.isPlayerName() = Bukkit.getPlayerExact(this) != null
/** Convert this name of a player into a player, or return null if none is found */
fun String.toPlayer() = Bukkit.getPlayerExact(this)

/** Remove all occurences of the provided strings */
fun String.remove(vararg sequences: String) = sequences.fold(this) {str, seq -> str.replace(seq, "") }
/** Remove all occurences of the provided regexes */
fun String.remove(vararg sequences: Regex) = sequences.fold(this) { str, reg -> str.replace(reg, "") }