package com.github.supergluelib.foundation.extensions

import org.bukkit.Location
import org.bukkit.inventory.ItemStack

/** Get the top-center of the block represented by this location */
fun Location.toCenter() = Location(world, blockX + 0.5, blockY + 0.5, blockZ + 0.5, yaw, pitch)

/** @return a new location with it's coordinates added to the values provided */
fun Location.add(x: Int, y: Int, z: Int) = add(x.toDouble(), y.toDouble(), z.toDouble())

/** @return a new location with the values provided subtracted */
fun Location.subtract(x: Int, y: Int, z: Int) = subtract(x.toDouble(), y.toDouble(), z.toDouble())

/** Drops an item at the given location */
fun Location.dropItem(item: ItemStack) = world?.dropItem(this, item)

