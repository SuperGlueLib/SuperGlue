package com.github.supergluelib.foundation.input

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent

internal object InputListener: Listener {

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val uuid = event.player.uniqueId

        Input.BlockPlace.awaiting.remove(uuid)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlace(event: BlockPlaceEvent) {
        val uuid = event.player.uniqueId
        val map = Input.BlockPlace.awaiting
        if (map.containsKey(uuid)) {
            val callback = map[uuid]!!
            map.remove(uuid)
            callback.invoke(event.blockPlaced)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlace(event: AsyncPlayerChatEvent) {
        val uuid = event.player.uniqueId
        val map = Input.Chat.awaiting
        if (map.containsKey(uuid)) {
            val callback = map[uuid]!!
            map.remove(uuid)
            callback.invoke(event.message)
        }
    }

}