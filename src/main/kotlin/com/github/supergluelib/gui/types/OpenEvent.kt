package com.github.supergluelib.gui.types

import org.bukkit.entity.Player

/**
 * Implement logic when the player opens the inventory
 */
interface OpenEvent {

    fun onOpen(player: Player)

}