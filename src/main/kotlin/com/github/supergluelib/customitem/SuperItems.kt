package com.github.supergluelib.customitem

import com.github.supergluelib.foundation.Foundations
import com.github.supergluelib.foundation.extensions.registerListeners
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
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
        items.forEach(this::unregister)
    }

    fun Foundations.registerItems(vararg items: CustomItem) = Foundations.also { register(*items) }

    private val items: HashMap<Class<out CustomItem>, CustomItem> = hashMapOf()
    private val blocks: HashMap<Class<out CustomBlock<*>>, CustomBlock<*>> = hashMapOf()
    private val projectiles: HashMap<Class<out ThrowableItem>, ThrowableItem> = hashMapOf()

    internal fun fromItemStack(item: ItemStack): CustomItem? = item.itemMeta?.let { meta ->
        val id = meta.getIdentifier()
        items.values.find { it.isItem(item, meta, id) }?.fromItemStack(item, meta, id)
    }

    internal fun getCustomBlock(block: Block) = blocks.values.find { it.isBlock(block) }
    internal fun getProjectile(proj: Projectile) = projectiles.values.find { it.isProjectile(proj) }

    private fun <T: CustomItem> register(item: T) {
        items[item::class.java] = item
        if (item is CustomBlock<*>) blocks[item::class.java as Class<out CustomBlock<*>>] = item
        if (item is ThrowableItem) projectiles[item::class.java as Class<out ThrowableItem>] = item
    }

    private fun <T: CustomItem> unregister(item: T) {
        items.remove(item::class.java)
        if (item is CustomBlock<*>) blocks.remove(item::class.java as Class<out CustomBlock<*>>)
        if (item is ThrowableItem) projectiles.remove(item::class.java as Class<out ThrowableItem>)
    }

    fun getByBlock(clazz: Class<out CustomBlock<*>>) = blocks[clazz]

    // New Item Identifier System
    val idKey = NamespacedKey(Foundations.plugin, "superglue-id")
    fun ItemMeta.setIdentifier(id: String) = persistentDataContainer.set(idKey, PersistentDataType.STRING, id)
    fun ItemMeta.getIdentifier(): String? = persistentDataContainer.get(idKey, PersistentDataType.STRING)

}