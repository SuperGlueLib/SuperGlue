package com.github.supergluelib.customitem

import com.github.supergluelib.foundation.*
import com.github.supergluelib.foundation.extensions.ensureExists
import com.github.supergluelib.foundation.misc.BlockPos
import com.github.supergluelib.foundation.misc.blockPos
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.io.File
import java.lang.reflect.Type

/**
 * Represents a custom block, placeable in the world.
 * This class internally saves the location data of blocks henceforth allowing you to implement custom blocks with no saving/loading,
 * the blocks location & associated data will be saved in `pluginDataFolder/blocks/classname.json`
 *
 * @param blockDataClass The class of data associated with each block to be saved
 */
abstract class CustomBlock<T>(val blockDataClass: Type, val defaultData: () -> T): CustomItem() {
    final override fun onRightClickBlock(player: Player, block: Block, item: ItemStack, event: PlayerInteractEvent) {} // Prevent overriding conflicting method
    final override fun getItem() = getItem(defaultData.invoke()) // Make sure Item is always supplied with data
    final override fun fromItemStack(item: ItemStack) = this

    private val file = File(Foundations.plugin.dataFolder, "blocks/${this::class.java.simpleName}.json").apply { ensureExists() }
    private val mapType = object: TypeToken<HashMap<String, HashMap<BlockPos, T>>>() {}.rawType
    private val nestedMapType = object: TypeToken<HashMap<BlockPos, T>>() {}.rawType
    private val blockPosAdapter = Gson().newBuilder().registerCustomTypeAdapter(BlockPosArrayGsonAdapter()).create()
    private val gson = Gson()
        .newBuilder()
        .registerCustomTypeAdapter(NestedMapGsonAdapter<String, BlockPos, T>(String::class.java, BlockPos::class.java, blockDataClass, blockPosAdapter))
        .create()

    private val dataStore: HashMap<String, HashMap<BlockPos, T>> = file.reader().use {
        gson.fromJson<HashMap<String, HashMap<BlockPos, T>>>(it, mapType)
    } ?: hashMapOf()

    class CustomBlockState<T>(val worldName: String, val pos: BlockPos, val data: T) {
        val world get() = Bukkit.getWorld(worldName)
        val block get() = world?.getBlockAt(pos.x, pos.y, pos.z)
        val isLoaded get() = world?.let { pos.isLoaded(it) } == true
    }

    fun getAllBlocks() = dataStore.entries.flatMap { entry ->
        entry.value.map { CustomBlockState<T>(entry.key, it.key, it.value) }
    }

    fun saveFile(async: Boolean) = Runnables.async(async) {
        file.writer().use { gson.toJson(dataStore, it) }
    }

    // Handler methods for this custom block type

    /** Whether the supplied block represents this custom block */
    fun isBlock(block: Block) = mightBeThisBlock(block) && dataStore[block.world.name]?.containsKey(block.blockPos) == true

    /** Retrieve the cached/saved data for the supplied block */
    fun getData(block: Block) = dataStore[block.world.name]?.get(block.blockPos)

    /** Removes this blocks data from the cache - Does not save */
    fun removeData(block: Block) { dataStore[block.world.name]!!.remove(block.blockPos) }

    /** Associates this data with the block in the cache - Does not save  */
    fun addBlockData(block: Block, data: T)  {
        dataStore.getOrPut(block.world.name, ::hashMapOf)[block.blockPos] = data
    }

    // Abstract methods required for implementation

    abstract fun getItem(data: T): ItemStack
    abstract fun getData(item: ItemStack): T?


    // Optional methods for implementation
    /**
     * -- Optional Implementation [<br>]
     * The event will not be cancelled automatically thus you will not need to subtract any quantity from the item in their hand by default.
     * You also do not need to save any data in this event, all data will be saved automatically.
     */
    open fun onPlace(block: Block, item: ItemStack, data: T, player: Player, event: BlockPlaceEvent) {}

    /** -- Optional Implementation [<br>]
     * Called when a player right clicks on this custom block
     */
    open fun onRightClickCustomBlock(block: Block, player: Player, data: T, event: PlayerInteractEvent) {}

    /** -- Optional Implementation [<br>]
     * Called when a player left clicks on this custom block
     */
    open fun onLeftClickCustomBlock(block: Block, player: Player, data: T, event: PlayerInteractEvent) {}

    /** -- Optional Implementation [<br>]
     * Called when a player places this block, Saving is handled automatically.
     */
    open fun onBreak(block: Block, data: T, player: Player, event: BlockBreakEvent) {}

    /** -- Optional Implementation [<br>]
     * This method is called as an optimised way of checking if the supplied block could be the custom block overriding this method.
     * An example of this may entail conducting a material check on the block type, if that returns false, then there is no need
     * to do any further calculations and thus will immediately return false, only in the event that this method returns true
     * will the location be checked against the saved locations to guarantee the return value of [isBlock]
     */
    open fun mightBeThisBlock(block: Block): Boolean = true


    // Internal methods for calling events etc.
    /** Gets the item that should be dropped when breaking this block */
    internal fun getItem(block: Block) = getItem(getData(block) ?: defaultData.invoke())

    /** Calls the custom blocks break event */
    internal fun callCustomBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        val data = getData(block)!!
        removeData(block)
        saveFile(true)
        onBreak(block, data, event.player, event)
    }

    /** Calls the custom blocks place event */
    internal fun callCustomBlockPlace(event: BlockPlaceEvent) {
        val block = event.block
        val item = event.itemInHand
        val data = getData(item) ?: defaultData.invoke()
        addBlockData(block, data)
        saveFile(true)
        onPlace(block, item, data, event.player, event)
    }

    /** Calls the custom interact for both left and right click events on this block */
    internal fun callCustomBlockInteract(event: PlayerInteractEvent) {
        if (!event.hasBlock()) return
        val block = event.clickedBlock!!
        val data = getData(block)!!
        if (event.action == Action.LEFT_CLICK_BLOCK)
            onLeftClickCustomBlock(block, event.player, data, event)
        else if (event.action == Action.RIGHT_CLICK_BLOCK)
            onRightClickCustomBlock(block, event.player, data, event)
    }

}