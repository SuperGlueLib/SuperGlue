package com.github.supergluelib.foundation

object Server {

    private fun classExists(path: String) = runCatching { Class.forName(path) }.isSuccess

    /** @return true if the server is running paper, or a fork of paper. */
    fun isPaper() = classExists("com.destroystokyo.paper.event.player.PlayerArmorChangeEvent")

    /** @return true if the server is running folia, or a fork of folia. */
    fun isFolia() = classExists("io.papermc.paper.threadedregions.RegionizedServer")

    /** @return true if mini message is found on the server otherwise false. */
    fun hasMiniMessage() = classExists("net.kyori.adventure.text.Component")

}