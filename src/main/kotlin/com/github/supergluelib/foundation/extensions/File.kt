package com.github.supergluelib.foundation.extensions

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