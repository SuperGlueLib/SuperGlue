package com.github.supergluelib.foundation.util

import com.github.supergluelib.foundation.Runnables
import com.github.supergluelib.foundation.extensions.loadYamlConfiguration
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * Handles the setting up, saving and reloading of a YAML file.
 * - onReload is called when the class is loaded
 * - Variables created in the subclass must be lateinit as onReload() is called *before* sub-instantiation
 * - I am hoping to fix this soon - maybe just make you call reload() in your init script.
 */
abstract class YamlFileHandler(private val plugin: JavaPlugin, val name: String, resource: Boolean = false) {
    val file: File = File(plugin.dataFolder, name)
    var config: FileConfiguration
    private var loaded = false

    init {
        if (!file.exists()){
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            if (resource) plugin.saveResource(name, false)
            else runCatching { file.createNewFile() }
        }
        config = file.loadYamlConfiguration()
        onReload()
    }

    /** Called when the file is first loaded and for every subsequent reload, Use this to update your cache. */
    protected abstract fun onReload()

    fun reload() {
        config = file.loadYamlConfiguration()
        onReload()

    }

    fun save(async: Boolean = false) = Runnables.async(async) { config.save(file) }
}