package com.github.supergluelib.foundation.itembuilder

import com.github.supergluelib.customitem.CustomItem
import com.github.supergluelib.customitem.SuperItems.getIdentifier
import com.github.supergluelib.foundation.util.ItemBuilder
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

internal class ItemBuilderActionHandler: CustomItem() {

    internal companion object {
        val actionMap = mutableMapOf<String, ItemBuilder.Actions>()
    }

    override fun getItem(): Nothing = throw IllegalArgumentException("Cannot generate this item")
    override fun isItem(item: ItemStack, meta: ItemMeta, id: String?) = id != null && id in actionMap
    override fun fromItemStack(item: ItemStack, meta: ItemMeta, id: String?) = this

    override fun onRightClick(player: Player, item: ItemStack, event: Event) {
        val id = item.itemMeta?.getIdentifier()?.takeIf { it in actionMap } ?: return
        actionMap[id]!!.action(player)
    }
}