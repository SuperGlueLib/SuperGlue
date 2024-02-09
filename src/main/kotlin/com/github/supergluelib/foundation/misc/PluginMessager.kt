package com.github.supergluelib.foundation.misc

import com.github.supergluelib.foundation.Foundations
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PluginMessager {
    init {
        Bukkit.getMessenger().registerOutgoingPluginChannel(Foundations.plugin, "BungeeCord")
    }

    /** Sends the player to the bungeecord server specified by [servername] or prints a warning to console if it is not found */
    fun Player.connectToBungeeServer(servername: String) = runCatching {
        val output = ByteStreams.newDataOutput().apply {
            writeUTF("Connect")
            writeUTF(servername)
        }.toByteArray()
        sendPluginMessage(Foundations.plugin, "BungeeCord", output)
    }.onFailure {
        Foundations.plugin.logger.warning("Failed to send $name to bungee server $servername, check the spelling and that the server is online")
        it.printStackTrace()
    }

    internal fun disable() {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(Foundations.plugin, "BungeeCord")
    }
}