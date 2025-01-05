package com.github.supergluelib.customitem

import com.github.supergluelib.foundation.util.ItemBuilder
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

// represents a fast furnace -- Data == speed multiplier
class ExampleCustomBlockImplementation: CustomBlock<Int>(Int::class.java, { 1 } ) {
    override fun onPlace(block: Block, item: ItemStack, data: Int, player: Player, event: BlockPlaceEvent) {} // optional
    override fun onRightClickCustomBlock(block: Block, player: Player, data: Int, event: PlayerInteractEvent) {} // optional
    override fun onLeftClickCustomBlock(block: Block, player: Player, data: Int, event: PlayerInteractEvent) {}  // optional
    override fun onBreak(block: Block, data: Int, player: Player, event: BlockBreakEvent) {} // optional

    override fun mightBeThisBlock(block: Block) = block.type == Material.FURNACE

    override fun getItem(data: Int): ItemStack = ItemBuilder(Material.FURNACE, "Speed x$data").identifier("specialfurnace").build()
    override fun isItem(item: ItemStack, meta: ItemMeta, id: String?) = id == "specialfurnace"

    override fun getData(item: ItemStack) = 1 // item.pdc.speed or whatever

}