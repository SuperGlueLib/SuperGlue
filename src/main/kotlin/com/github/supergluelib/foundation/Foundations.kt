package com.github.supergluelib.foundation

import com.github.supergluelib.customitem.SuperItems
import com.github.supergluelib.foundation.customevents.CustomEventListener
import com.github.supergluelib.foundation.extensions.register
import com.github.supergluelib.foundation.input.InputListener
import com.github.supergluelib.foundation.misc.PluginMessager
import com.github.supergluelib.gui.GUIManager
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

    private var usingPluginMessaging: Boolean = false
    private var usingCustomEvents: Boolean = false

    fun setup(plugin: JavaPlugin): Foundations {
        this._plugin = plugin
        InputListener.register(plugin)
        SuperItems.setup(plugin)
        GUIManager.setup(plugin)
        return this
    }

    fun useCustomEvents(use: Boolean = true) {
        usingCustomEvents = use
        if (use) CustomEventListener.setup(plugin)
    }

    fun usePluginMessaging(use: Boolean = true) {
        usingPluginMessaging = use
        if (use) PluginMessager // Init -> Register
    }

    fun onDisable() {
        if (usingPluginMessaging) PluginMessager.disable()
    }
}

