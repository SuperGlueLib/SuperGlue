package com.github.supergluelib.foundation.misc

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import java.util.UUID

val Location.worldBlockPos get() = WorldBlockPos(world!!.uid, blockX, blockY, blockZ)
val Location.blockPos get() = BlockPos(blockX, blockY, blockZ)
val Block.blockPos get() = location.blockPos
val Block.worldBlockPos get() = location.worldBlockPos
val BlockState.blockPos get() = location.blockPos

interface BlockPosBase {
    val x: Int
    val y: Int
    val z: Int
}

data class BlockPos(override val x: Int, override val y: Int, override val z: Int): BlockPosBase {
    fun withWorld(world: World) = WorldBlockPos(world.uid, x, y, z)

    fun getLocation(world: World?) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    fun getBlock(world: World) = world.getBlockAt(x, y, z)

    fun isLoaded(world: World) = world.isChunkLoaded(x shr 4, z shr 4)

    /** @return a new BlockPos containing the addition of each coordinate of the original two BlockPos's */
    fun add(other: BlockPos) = BlockPos(other.x + x, other.y + y, other.z + z)
    /** @return a new BlockPos with the parameters added on to their respective coordinates */
    fun add(x: Int, y: Int, z: Int) = BlockPos(this.x + x, this.y + y, this.z + z)

    /** @return a new BlockPos which has the coordinates of the [other] BlockPos subtracted from this BlockPos */
    fun subtract(other: BlockPos) = BlockPos(x - other.x, y - other.y, z - other.z)
    /** @return a new BlockPos which has the given coordinates subtracted from this BlockPos */
    fun subtract(x: Int, y: Int, z: Int) = BlockPos(this.x - x, this.y - y, this.z - z)
}

data class WorldBlockPos(val worldId: UUID, override val x: Int, override val y: Int, override val z: Int): BlockPosBase {
    val world get() = Bukkit.getWorld(worldId)!!

    val isLoaded: Boolean get() = world.isChunkLoaded(x shr 4, z shr 4)
    val location: Location get() = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    val block: Block get() = world.getBlockAt(x,y,z)


    /** @return a new blockpos with the coordinates added */
    fun add(other: BlockPosBase) = add(other.x, other.y, other.z)
    /** @return a new blockpos with the coordinates added */
    fun add(x: Int, y: Int, z: Int) = WorldBlockPos(worldId, this.x + x, this.y + y, this.z + z)

    /** @return a new BlockPos which has the coordinates of the [other] BlockPos subtracted from this BlockPos */
    fun subtract(other: BlockPosBase) = subtract(other.x, other.y, other.z)
    /** @return a new BlockPos which has the given coordinates subtracted from this BlockPos */
    fun subtract(x: Int, y: Int, z: Int) = WorldBlockPos(worldId, this.x - x, this.y - y, this.z - z)
}
