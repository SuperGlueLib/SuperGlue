package com.github.supergluelib.foundation.extensions

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.java.JavaPlugin

/** Easy Listener registering :D */
fun Listener.register(plugin: JavaPlugin) = Bukkit.getPluginManager().registerEvents(this, plugin)
/** Easy mass listener registering */
fun JavaPlugin.registerListeners(vararg listeners: Listener) = Bukkit.getPluginManager().let { manager -> listeners.forEach { manager.registerEvents(it, this@registerListeners) } }


/** Cancels a cancellable event by setting `isCancelled = true` */
fun Cancellable.cancel() { isCancelled = true }

/** Checks if player click was in top inventory not bottom inventory */
fun InventoryClickEvent.clickedTopInventory() = clickedInventory?.equals(view.topInventory) == true

/** @return the [Player] who clicked... like a [HumanEntity][org.bukkit.entity.HumanEntity] only better... */
val InventoryClickEvent.player get() = whoClicked as Player

/** the uuid of the player involved with this event */
val PlayerEvent.uuid get() = player.uniqueId