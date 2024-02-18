package com.github.supergluelib.foundation.extensions

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack

/** Get the top-center of the block represented by this location */
fun Location.toCenter() = Location(world, blockX + 0.5, blockY + 0.5, blockZ + 0.5, yaw, pitch)

/** @return a new location with it's coordinates added to the values provided */
fun Location.add(x: Int, y: Int, z: Int) = add(x.toDouble(), y.toDouble(), z.toDouble())

/** @return a new location with the values provided subtracted */
fun Location.subtract(x: Int, y: Int, z: Int) = subtract(x.toDouble(), y.toDouble(), z.toDouble())

/** Drops an item at the given location */
fun Location.dropItem(item: ItemStack) = world?.dropItem(this, item)

/** Spawns a particle at this location that everyone can see */
fun Location.spawnParticle(particle: Particle, count: Int, offsetAllAxes: Double = 0.0, extra: Double = 0.0, data: Any? = null) = world
    ?.spawnParticle(particle, this, count, offsetAllAxes, offsetAllAxes, offsetAllAxes, extra, data)

/** @return the minimum x, y and z between these two locations */
fun Location.min(other: Location) = Location(world,
    minOf(x, other.x),
    minOf(y, other.y),
    minOf(z, other.z),
    yaw,
    pitch
)

/** @return the maximum x, y and z between these two locations */
fun Location.max(other: Location) = Location(world,
    maxOf(x, other.x),
    maxOf(y, other.y),
    maxOf(z, other.z),
    yaw,
    pitch
)