package com.github.supergluelib.foundation.extensions

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/** load a YML Configuration from a file */
fun File.loadYamlConfiguration() = YamlConfiguration.loadConfiguration(this)

/** Ensure that this file exists by creating any missing parent directories and this file if they do not exist
 * @return This file, for chaining.
 */
fun File.ensureExists(): File  {
    if (!exists()) {
        parentFile.mkdirs()
        createNewFile()
    }
    return this
}

/**
 * Maps the keys of this [ConfigurationSection] alongside the result of `thisSection.getConfigurationSection(key)`
 * itself as a parameter in the mapping function.
 * @return a list containing the results of the mapping function
 */
fun <R> ConfigurationSection.mapSections(transform: (section: ConfigurationSection, key: String) -> R) = getKeys(false)
    .map { transform(this.getConfigurationSection(it)!!, it) }