package com.github.supergluelib.foundation.extensions

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/** Send a coloured message to a player */
fun CommandSender.send(msg: String, hex: Boolean = false) = sendMessage(msg.toColor(hex))

/** Adds the item to a players inventory and drops any items that didn't fit onto the floor */
fun Player.giveOrDropItem(item: ItemStack) = inventory.addItem(item).forEach { (_, item) ->  world.dropItem(location, item)}