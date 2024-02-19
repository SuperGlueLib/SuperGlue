package com.github.supergluelib.foundation.util

import com.github.supergluelib.foundation.Foundations
import com.github.supergluelib.foundation.Runnables
import com.github.supergluelib.foundation.async
import com.github.supergluelib.foundation.extensions.register
import com.github.supergluelib.foundation.extensions.uuid
import com.google.common.cache.CacheBuilder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 */
abstract class PlayerCache<T> {

    private val listener = PlayerCacheListener().apply { register(Foundations.plugin) }

    private inner class PlayerCacheListener(): Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        fun loadPlayerDataOnJoin(event: PlayerJoinEvent) {
            val uuid = event.player.uniqueId
            val secData = secondaryCache.getIfPresent(uuid)
            if (secData != null) {
                secondaryCache.invalidate(uuid)
                onlineCache[uuid] = secData
            } else {
                async { onlineCache[uuid] = load(uuid) }
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        fun removeDataOnQuit(event: PlayerQuitEvent) {
            val uuid = event.uuid
            val onlineData = onlineCache[uuid] ?: return
            saveAsync(uuid, onlineData)
            onlineCache.remove(uuid)
            secondaryCache.put(uuid, onlineData)
        }
    }

    private val onlineCache = hashMapOf<UUID, T>()
    private val secondaryCache = CacheBuilder.newBuilder().expireAfterAccess(20, TimeUnit.MINUTES).build<UUID, T>()

    // Private data accessors
    private fun getOnlineData(uuid: UUID) = onlineCache[uuid] ?: secondaryCache.getIfPresent(uuid)
    private fun loadAndCacheOfflineData(uuid: UUID) = load(uuid).also {
        if (Bukkit.getPlayer(uuid) != null) onlineCache[uuid] = it
        else secondaryCache.put(uuid, it)
    }
    /** Saves this players data asynchronously according to [save] */
    protected fun saveAsync(uuid: UUID, data: T) = async { save(uuid, data) }

    // Abstract implementation methods
    /**
     * Load the data of this player from the database.
     * - This method needs to be thread-safe! It is always called asynchronously.
     */
    protected abstract fun load(uuid: UUID): T

    /**
     * Save the data of this player to the database.
     * - This method needs to be thread-safe! It is always called asynchronously
     */
    protected abstract fun save(uuid: UUID, data: T)

    // Handler getter methods
    /**
     * Gets the data of this player and then executes the callback.
     * If the data is found in the cache, the callback will be executed immediately, otherwise it will wait for the data
     * to be loaded and then executed. The callback is always run synchronously.
     */
    fun getDataAndThen(uuid: UUID, callback: T.() -> Unit) {
        val onlineData = getOnlineData(uuid)
        if (onlineData != null) return callback.invoke(onlineData)
        else async {
            val offlineData = loadAndCacheOfflineData(uuid)
            Runnables.runTask { callback.invoke(offlineData) }
        }
    }

    /** @return the data found in the cache, otherwise null */
    fun getCacheData(uuid: UUID): T? = getOnlineData(uuid)

    /** Grabs the cached data for this player and saves it asynchronously */
    fun saveAsync(player: Player) = saveAsync(player.uniqueId, getOnlineData(player.uniqueId)!!)
}