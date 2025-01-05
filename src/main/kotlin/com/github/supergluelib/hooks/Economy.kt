package com.github.supergluelib.hooks

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getServer
import org.bukkit.plugin.RegisteredServiceProvider
import java.util.UUID

class Economy internal constructor(): Hooks.Hook {

    private val _eco: Economy?
    private val eco get() = _eco ?: throw IllegalStateException("Vault is required to use Economy!")


    init {
        val plugin = getServer().pluginManager.getPlugin("Vault")
        val rsp: RegisteredServiceProvider<Economy>? = getServer().servicesManager.getRegistration(Economy::class.java)

        _eco = if (plugin == null || rsp == null) null else rsp.provider
    }

    private fun UUID.toOfflinePlayer() = Bukkit.getOfflinePlayer(this)

    fun withdraw(uuid: UUID, amount: Double) = eco.withdrawPlayer(uuid.toOfflinePlayer(), amount)
    fun deposit(uuid: UUID, amount: Double) = eco.depositPlayer(uuid.toOfflinePlayer(), amount)
    fun has(uuid: UUID, amount: Double) = eco.getBalance(uuid.toOfflinePlayer()) >= amount
    fun check(uuid: UUID) = eco.getBalance(uuid.toOfflinePlayer())

    fun withdrawIfPossible(uuid: UUID, amount: Double) = if (has(uuid, amount)) withdraw(uuid, amount).let { true } else false

}