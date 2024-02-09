package com.github.supergluelib.gui.guiparts

import com.github.supergluelib.gui.GUI
import org.bukkit.inventory.ItemStack

abstract class AbstractButton(
    val id: Int,
    protected val itemstack: ItemStack,
) {
    abstract fun getItem(): ItemStack
    abstract fun onClick(action: (GUI.ClickData.() -> Unit)?): AbstractButton
}