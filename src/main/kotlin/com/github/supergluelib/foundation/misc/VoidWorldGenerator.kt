package com.github.supergluelib.foundation.misc

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.LimitedRegion
import org.bukkit.generator.WorldInfo
import java.util.*

class VoidWorldGenerator(val generateSpawnBlock: Boolean = false): ChunkGenerator() {

    override fun shouldGenerateNoise() = false
    override fun shouldGenerateNoise(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int) = false
    override fun shouldGenerateSurface() = false
    override fun shouldGenerateSurface(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int) = false
    override fun shouldGenerateBedrock() = false
    override fun shouldGenerateCaves() = false
    override fun shouldGenerateCaves(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int) = false
    override fun shouldGenerateDecorations() = false
    override fun shouldGenerateDecorations(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int) = false
    override fun shouldGenerateMobs() = false
    override fun shouldGenerateMobs(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int) = false
    override fun shouldGenerateStructures() = false
    override fun shouldGenerateStructures(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int) = false

    override fun getFixedSpawnLocation(world: World, random: Random): Location {
        return Location(world, 0.5, 70.0, 0.5)
    }

    override fun getDefaultPopulators(world: World): MutableList<BlockPopulator> {
        return if (generateSpawnBlock) mutableListOf(PlatformPopulator()) else mutableListOf()
    }

    class PlatformPopulator : BlockPopulator() {

        override fun populate(
            worldInfo: WorldInfo,
            random: Random,
            chunkX: Int,
            chunkZ: Int,
            limitedRegion: LimitedRegion
        ) {
            if (limitedRegion.isInRegion(0, 69, 0)) {
                limitedRegion.setType(0, 69, 0, Material.BEDROCK)
            }
        }
    }
}