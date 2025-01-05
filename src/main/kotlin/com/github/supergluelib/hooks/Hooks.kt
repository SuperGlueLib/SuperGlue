package com.github.supergluelib.hooks

import org.bukkit.Bukkit

object Hooks {
    interface Hook
    private fun isEnabled(name: String) = Bukkit.getPluginManager().isPluginEnabled(name)

    val economy = if (isEnabled("Vault")) Economy() else null
}