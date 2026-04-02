package com.github.supergluelib.foundation

import org.bukkit.plugin.java.JavaPlugin

object Foundations {
    private var _plugin: JavaPlugin? = null
    internal val plugin: JavaPlugin get() {
        if (_plugin == null) throw NullPointerException("""
            You must call Foundations#setup(plugin) before you use this class, usually in the onEnable
            and Foundations#onDisable() after you've finished using it, usually in the onDisable()
        """.trimIndent())
        else return _plugin!!
    }

    internal var disableHooks: MutableList<() -> Unit> = mutableListOf()

    fun setup(plugin: JavaPlugin): Foundations {
        this._plugin = plugin
        return this
    }

    fun log(info: String) = plugin.logger.info(info)

    fun onDisable() {
        disableHooks.forEach { it.invoke() }
    }
}

