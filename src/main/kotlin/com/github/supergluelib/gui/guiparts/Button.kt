package com.github.supergluelib.gui.guiparts

import com.github.supergluelib.foundation.Foundations
import com.github.supergluelib.gui.GUI
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * Represents an item in a gui which has a specific click function.
 */
open class Button(
    id: Int,
    itemstack: ItemStack,
    var onClick: (GUI.ClickData.() -> Unit)? = null,
): AbstractButton(id, itemstack) {

    override fun getItem() = itemstack.applyID(id)
    override fun onClick(action: (GUI.ClickData.() -> Unit)?) = apply { onClick = action }

    protected fun ItemStack.applyID(id: Int) = apply { applyID(this, id) }

    companion object {
        private val buttonkey = NamespacedKey(Foundations.plugin, "buttonkey")

        fun ItemStack.isButton() = itemMeta?.persistentDataContainer?.has(buttonkey, PersistentDataType.INTEGER) == true
        fun ItemStack.getId() = itemMeta?.persistentDataContainer?.get(buttonkey, PersistentDataType.INTEGER)

        fun applyID(item: ItemStack, id: Int) {
            val meta = item.itemMeta ?: return
            meta.persistentDataContainer.set(buttonkey, PersistentDataType.INTEGER, id)
            item.itemMeta = meta
        }
    }


}