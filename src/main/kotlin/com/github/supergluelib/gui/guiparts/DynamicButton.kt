package com.github.supergluelib.gui.guiparts

import com.github.supergluelib.gui.GUI
import org.bukkit.inventory.ItemStack

/**
 * Dynamic buttons act like regular buttons only the item in their place is automatically updated
 * via the [generateItem] function every time they are clicked,
 */
class DynamicButton(
    id: Int,
    private val generateItem: () -> ItemStack,
    onClick: (GUI.ClickData.() -> Unit)? = null,
    var updateOnClick: Boolean = true
): Button(id, generateItem.invoke(), onClick) {

    override fun getItem() = generateItem.invoke().applyID(id)

    // Builder methods
    override fun onClick(action: (GUI.ClickData.() -> Unit)?) = apply { onClick = action }
    fun updateOnClick(shouldUpdate: Boolean = true) = apply { updateOnClick = shouldUpdate }

}
