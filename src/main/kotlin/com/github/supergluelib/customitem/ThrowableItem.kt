package com.github.supergluelib.customitem

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.ProjectileHitEvent

abstract class ThrowableItem: CustomItem() {

    abstract fun isProjectile(proj: Projectile): Boolean

    open fun onHit(projectile: Projectile, location: Location, event: ProjectileHitEvent) {}
    open fun onHitEntity(projectile: Projectile, entity: Entity, event: ProjectileHitEvent) {}
    open fun onHitBlock(projectile: Projectile, block: Block, event: ProjectileHitEvent) {}

}