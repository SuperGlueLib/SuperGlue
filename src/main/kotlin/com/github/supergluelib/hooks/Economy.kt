package com.github.supergluelib.hooks

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getServer
import org.bukkit.entity.Player
import org.bukkit.plugin.RegisteredServiceProvider
import java.util.UUID

class Economy internal constructor(): Hooks.Hook {

    private val _eco: Economy?
    private val eco get() = _eco ?: throw IllegalStateException("Vault and a valid Economy provider are required to use Economy Functions!")


    init {
        val plugin = getServer().pluginManager.getPlugin("Vault")
        val rsp: RegisteredServiceProvider<Economy>? = getServer().servicesManager.getRegistration(Economy::class.java)

        _eco = if (plugin == null || rsp == null) null else rsp.provider
    }

    private fun UUID.toOfflinePlayer() = Bukkit.getOfflinePlayer(this)

    fun withdraw(uuid: UUID, amount: Number) { eco.withdrawPlayer(uuid.toOfflinePlayer(), amount.toDouble()) }
    fun withdraw(player: Player, amount: Number) { eco.withdrawPlayer(player, amount.toDouble()) }

    fun deposit(uuid: UUID, amount: Number) { eco.depositPlayer(uuid.toOfflinePlayer(), amount.toDouble()) }
    fun deposit(player: Player, amount: Number) { eco.depositPlayer(player, amount.toDouble()) }

    fun has(uuid: UUID, amount: Number) = eco.getBalance(uuid.toOfflinePlayer()) >= amount.toDouble()
    fun has(player: Player, amount: Number) = eco.getBalance(player) >= amount.toDouble()

    fun check(uuid: UUID) = eco.getBalance(uuid.toOfflinePlayer())
    fun check(player: Player) = eco.getBalance(player)

    fun withdrawIfPossible(uuid: UUID, amount: Number) = if (has(uuid, amount)) withdraw(uuid, amount).let { true } else false
    fun withdrawIfPossible(player: Player, amount: Number) = if (has(player, amount)) withdraw(player, amount).let { true } else false

}
