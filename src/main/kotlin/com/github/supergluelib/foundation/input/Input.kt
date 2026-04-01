package com.github.supergluelib.foundation.input

import com.github.supergluelib.foundation.Foundations
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.entity.Player
import java.util.*

object Input {

    init {
        Bukkit.getPluginManager().registerEvents(InputListener, Foundations.plugin)
    }

    object BlockPlace {
        internal val awaiting: HashMap<UUID, (Block) -> Unit> = HashMap()
        fun take(player: Player, whenPlaced: (Block) -> Unit) { awaiting[player.uniqueId] = whenPlaced }
    }

    object Chat {
        internal val awaiting: HashMap<UUID, (String) -> Unit> = HashMap()
        fun take(player: Player, whenChat: (String) -> Unit) { awaiting[player.uniqueId] = whenChat }
    }

}