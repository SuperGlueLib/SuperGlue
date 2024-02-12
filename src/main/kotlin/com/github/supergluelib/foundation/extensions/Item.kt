package com.github.supergluelib.foundation.extensions

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

/** Decrement the amount of the itemstack by 1 */
fun ItemStack.removeOne() { amount -= 1 }
/** @return true if the itemstack is not null and not air */
fun ItemStack?.isValid() = (this != null) && !type.isAir
/** @return true if this itemstack has no special item meta outside of it's material type and amount */
fun ItemStack.isBasic() = this.isSimilar(ItemStack(type))

/** @return true if the localized name of this item exists and is equal to [loc] */
fun ItemStack.locnameIs(loc: String): Boolean = itemMeta?.localizedName == loc

/** @return The value gotten from the persistent data container of this item using the given key and type */
private fun <T> ItemStack.getPersistent(key: NamespacedKey, type: PersistentDataType<T, T>) = itemMeta?.persistentDataContainer?.get(key, type)

/** @return true if the localized name of this item exists and is equal to [loc] */
fun ItemMeta.locnameIs(loc: String): Boolean = localizedName == loc

/** Gets the name of the material in a prettier format i.e. BROWN_MUSHROOM -> Brown Mushroom */
fun Material.formattedName() = toString()
    .split("_")
    .joinToString(" ") { it.lowercase().replaceFirstChar(Char::uppercase) }

