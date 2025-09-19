package com.github.supergluelib.gui

import com.github.supergluelib.foundation.util.ItemBuilder
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class SGInventory(val bukkit: Inventory): Inventory by bukkit {

    fun ItemBuilder.setTo(vararg slots: Int) = buildTo(*slots)
    fun ItemBuilder.buildTo(vararg slots: Int) {
        val item = build()
        slots.forEach { setItem(it, item) }
    }

    fun ItemStack.setTo(vararg slots: Int) = slots.forEach { setItem(it, this) }

}