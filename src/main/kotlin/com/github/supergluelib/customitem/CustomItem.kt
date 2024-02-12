package com.github.supergluelib.customitem

import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * A class to extend when creating custom items.
 * Provided are a number of functions which are called when certain actions occur.
 *
 * Note that methods that encapsulate multiple other methods are all called, for example;
 * when a player right-clicks a block, both onRightClick & onRightClickBlock are called, only one needs to be handled.
 *
 * - Each item must be registered via [SuperItems.register]
 */
abstract class CustomItem() {

    class Settings(var canPlace: Boolean = false)
    val settings = Settings()

    abstract fun isItem(item: ItemStack, meta: ItemMeta = item.itemMeta!!): Boolean
    /** This method assumes that [isItem] is true */
    abstract fun fromItemStack(item: ItemStack): CustomItem
    abstract fun getItem(): ItemStack

    open fun onRightClick(player: Player, item: ItemStack, event: Event) {}
    open fun onLeftClick(player: Player, item: ItemStack, event: Event) {}

    open fun onRightClickBlock(player: Player, block: Block, item: ItemStack, event: PlayerInteractEvent) {}
    open fun onRightClickAir(player: Player, item: ItemStack, event: PlayerInteractEvent) {}
    open fun onRightClickEntity(player: Player, entity: Entity, item: ItemStack, event: PlayerInteractAtEntityEvent) {}
    open fun onLeftClickBlock(player: Player, block: Block, item: ItemStack, event: PlayerInteractEvent) {}
    open fun onLeftClickAir(player: Player, item: ItemStack, event: PlayerInteractEvent) {}
    open fun onLeftClickEntity(player: Player, entity: Entity, item: ItemStack, event: EntityDamageByEntityEvent) {}
    /** Called when a block is broken with the tool in hand */
    open fun onBreakBlockWith(player: Player, block: Block, item: ItemStack, event: BlockBreakEvent) {}

}
