package com.github.supergluelib.customitem

import com.github.supergluelib.foundation.extensions.cancel
import com.github.supergluelib.foundation.extensions.dropItem
import com.github.supergluelib.foundation.extensions.toCenter
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

internal class ItemListener: Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.isCancelled && (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.LEFT_CLICK_AIR)) return
        val item = event.item
        val customItem = item?.let { SuperItems.fromItemStack(it) }
        val player = event.player
        val block = event.clickedBlock
        if (customItem != null) {
            when (event.action) {
                Action.RIGHT_CLICK_BLOCK -> {
                    customItem.onRightClick(player, item, event)
                    customItem.onRightClickBlock(player, block!!, item, event)
                }
                Action.RIGHT_CLICK_AIR -> {
                    customItem.onRightClick(player, item, event)
                    customItem.onRightClickAir(player, item, event)
                }
                Action.LEFT_CLICK_BLOCK -> {
                    customItem.onLeftClick(player, item, event)
                    customItem.onLeftClickBlock(player, block!!, item, event)
                }
                Action.LEFT_CLICK_AIR -> {
                    customItem.onLeftClick(player, item, event)
                    customItem.onLeftClickAir(player, item, event)
                }
                else -> {}
            }
        }
        if (block == null) return
        val customBlock = SuperItems.getCustomBlock(block) ?: return
        customBlock.callCustomBlockInteract(event)
    }

    @EventHandler(ignoreCancelled = true)
    fun onInteractWithEntity(event: PlayerInteractAtEntityEvent) {
        val player = event.player
        val item = player.inventory.getItem(event.hand)
        if (item == null || item.type.isAir) return
        val customItem = SuperItems.fromItemStack(item) ?: return
        val entity = event.rightClicked
        customItem.onRightClick(player, item, event)
        customItem.onRightClickEntity(player, entity, item, event)
    }

    @EventHandler(ignoreCancelled = true)
    fun onPunchEntity(event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player ?: return
        val item = player.inventory.itemInMainHand
        if (item.type.isAir) return
        val customItem = SuperItems.fromItemStack(item) ?: return
        val entity = event.entity
        customItem.onLeftClick(player, item, event)
        customItem.onLeftClickEntity(player, entity, item, event)
    }

    @EventHandler(ignoreCancelled = true)
    fun onBreakBlock(event: BlockBreakEvent) {
        val item = event.player.inventory.itemInMainHand.takeUnless { it.type.isAir }
        val customItem = item?.let { SuperItems.fromItemStack(it) }
        customItem?.onBreakBlockWith(event.player, event.block, item, event)

        val customblock = SuperItems.getCustomBlock(event.block)
        if (customblock != null) {
            val data = customblock.getItem(event.block)
            customblock.callCustomBlockBreak(event)
            event.isDropItems = false
            event.block.location.toCenter().dropItem(data)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlaceCustomBlock(event: BlockPlaceEvent) {
        val customItem = SuperItems.fromItemStack(event.itemInHand) ?: return
        if (!customItem.settings.canPlace) return event.cancel()
        if (customItem is CustomBlock<*>)
            customItem.callCustomBlockPlace(event)
    }

    @EventHandler(ignoreCancelled = true)
    fun onProjectileHit(event: ProjectileHitEvent) {
        val customProjectile = SuperItems.getProjectile(event.entity) ?: return
        customProjectile.onHit(event.entity, event.entity.location, event)
        event.hitBlock?.let { customProjectile.onHitBlock(event.entity, it, event) }
        event.hitEntity?.let { customProjectile.onHitEntity(event.entity, it, event) }
    }

}