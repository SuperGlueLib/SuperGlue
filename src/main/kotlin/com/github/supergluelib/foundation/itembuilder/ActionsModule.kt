package com.github.supergluelib.foundation.itembuilder

import com.github.supergluelib.customitem.SuperItems
import com.github.supergluelib.gui.GUI
import org.bukkit.entity.Player

class ActionsModule {

    private companion object {
        private var registered = false
    }

    var gui: GUI? = null

    fun openGUI(gui: GUI) = apply { this.gui = gui }

    /** Registers this specific action */
    internal fun register(id: String) {
        if (!registered) {
            SuperItems.register(ItemBuilderActionHandler())
            registered = true
        }
        ItemBuilderActionHandler.actionMap[id] = this
    }

    internal fun action(player: Player) {
        gui?.open(player)
    }
}