package com.github.supergluelib.customitem

import com.github.supergluelib.foundation.Foundations
import com.github.supergluelib.foundation.extensions.registerListeners
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

object SuperItems {
    private var setup: Boolean = false
    internal fun setup(plugin: JavaPlugin) {
        setup = true
        plugin.registerListeners( ItemListener() )
    }

    fun register(vararg items: CustomItem) {
        if (!setup) Bukkit.getLogger().warning("Make sure you have used Foundations.setup(plugin) before registering custom items")
        items.forEach(this::register)
    }

    fun unregister(vararg items: CustomItem) {
        if (!setup) Bukkit.getLogger().warning("Make sure you have used Foundations.setup(plugin) before using custom items")

    }

    fun Foundations.registerItems(vararg items: CustomItem) = Foundations.also { register(*items) }

    private val items: HashMap<Class<out CustomItem>, CustomItem> = hashMapOf()
    private val blocks: HashMap<Class<out CustomBlock<*>>, CustomBlock<*>> = hashMapOf()
    internal fun fromItemStack(item: ItemStack) = item.itemMeta?.let { meta -> items.values.find { it.isItem(item, meta) }?.fromItemStack(item) }
    internal fun getCustomBlock(block: Block) = blocks.values.find { it.isBlock(block) }

    private fun <T: CustomItem> register(item: T) {
        items[item::class.java] = item
        if (item is CustomBlock<*>) blocks[item::class.java as Class<out CustomBlock<*>>] = item
    }

    private fun <T: CustomItem> unregister(item: T) {
        items.remove(item::class.java)
        if (item is CustomBlock<*>) blocks.remove(item::class.java as Class<out CustomBlock<*>>)
    }

    fun getByBlock(clazz: Class<out CustomBlock<*>>) = blocks[clazz]
}