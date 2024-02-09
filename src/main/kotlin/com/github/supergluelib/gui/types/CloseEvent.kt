package com.github.supergluelib.gui.types

import org.bukkit.entity.Player

/**
 * Implement logic when the player closes the inventory
 */
interface CloseEvent {

    fun whenClosed(player: Player)

}