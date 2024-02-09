package com.github.supergluelib.foundation.input

import org.bukkit.block.Block
import org.bukkit.entity.Player
import java.util.*

object Input {
    object BlockPlace {
        internal val awaiting: HashMap<UUID, (Block) -> Unit> = HashMap()
        fun take(player: Player, whenPlaced: (Block) -> Unit) { awaiting[player.uniqueId] = whenPlaced }
    }

    object Chat {
        internal val awaiting: HashMap<UUID, (String) -> Unit> = HashMap()
        fun take(player: Player, whenChat: (String) -> Unit) { awaiting[player.uniqueId] = whenChat }
    }

}