package com.github.supergluelib.gui.types

import org.bukkit.entity.Player

/**
 * Prevent players from closing the inventory until you allow them to close it via [canClose]
 */
interface ForceKeepOpen {

    fun canClose(player: Player): Boolean

}