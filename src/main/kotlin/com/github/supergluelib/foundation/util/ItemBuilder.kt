package com.github.supergluelib.foundation.util

import com.github.supergluelib.customitem.SuperItems.getIdentifier
import com.github.supergluelib.customitem.SuperItems.setIdentifier
import com.github.supergluelib.foundation.extensions.toColor
import com.github.supergluelib.foundation.extensions.toHashMap
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import java.net.URL
import java.util.*

/**
 * A kotlin friendly class optimising and shortening the creation of custom itemstacks.
 */

class ItemBuilder(private var type: Material, Name: String? = null, private var amount: Int? = null) {
    constructor(type: Material, amount: Int?): this(type, null, amount)
    var name: String? = Name
    var lore: ArrayList<String>? = null
    var identifier: String? = null
    var persistentInts: HashMap<NamespacedKey, Int>? = null
    var persistentStrings: HashMap<NamespacedKey, String>? = null
    var useHex: Boolean? = null
    var enchants: HashMap<Enchantment, Int>? = null
    var hideEnchants: Boolean? = null
    var hideDye: Boolean? = null
    var unbreakable: Boolean? = null
    var glowing: Boolean? = null
    var customModelData: Int? = null
    var customModelDataStrings: List<String>? = null

    var leathercolor: Color? = null

    var skullowner: UUID? = null
    var skullURLTexture: String? = null // This is the B64

    fun name(name: String) = apply { this.name = name; }
    fun lore(lines: List<String>) = apply { this.lore = ArrayList(lines) }
    fun addLores(lines: List<String>) = apply { lore = (lore ?: ArrayList()).apply { addAll(lines) } }
    fun addLore(vararg line: String) = addLores(line.toList())
    fun addEnchant(enchant: Enchantment, level: Int) = apply { if (enchants == null) enchants = hashMapOf(enchant to level) else enchants!![enchant] = level }
    fun enchants(enchants: Map<Enchantment, Int>) = apply { this@ItemBuilder.enchants = enchants.toHashMap() }
    fun hideEnchants(hide: Boolean) = apply { hideEnchants = hide }
    fun identifier(id: String) = apply { this.identifier = id }
    fun addPersistentInt(key: NamespacedKey, data: Int) = apply { if (persistentInts == null) persistentInts = hashMapOf(key to data) else persistentInts!![key] = data }
    fun addPersistentString(key: NamespacedKey, data: String) = apply { if (persistentStrings == null) persistentStrings = hashMapOf(key to data) else persistentStrings!![key] = data }
    fun customModelData(model: Int) = apply { customModelData = model }
    fun customModelData(vararg model: String) = apply { customModelDataStrings = model.toList() }

    fun hex(use: Boolean) = apply { useHex = use }
    fun unbreakable(unbreakable: Boolean) = apply { this.unbreakable = unbreakable }
    fun glowing(glowing: Boolean) = apply { this.glowing = glowing }

    fun leathercolor(color: Color) = apply { leathercolor = color }
    fun hideDye(hide: Boolean) = apply { hideDye = hide }

    fun skullOwner(player: UUID) = apply { skullowner = player }
    fun skullURLTexture(urlTail: String) = apply { skullURLTexture = urlTail }

    fun build(): ItemStack {
        val stack = ItemStack(type, amount ?: 1)
        val meta = stack.itemMeta!!
        if (name != null) meta.setDisplayName(name!!.toColor(useHex ?: false))
        if (lore != null) meta.lore = lore!!.map { it.toColor(useHex ?: false) }
        identifier?.let { meta.setIdentifier(identifier!!) }
        unbreakable?.let(meta::setUnbreakable)
        customModelData?.let(meta::setCustomModelData)
        customModelDataStrings?.let {
            val comp = meta.customModelDataComponent
            comp.strings = it
            meta.setCustomModelDataComponent(comp)
        }
        if (persistentInts?.isNotEmpty() == true)
            persistentInts!!.entries.forEach { meta.persistentDataContainer[it.key, PersistentDataType.INTEGER] = it.value }
        if (persistentStrings?.isNotEmpty() == true)
            persistentStrings!!.entries.forEach { meta.persistentDataContainer[it.key, PersistentDataType.STRING] = it.value }
        if (enchants?.isNotEmpty() == true) enchants!!.forEach { (enchant, level) -> meta.addEnchant(enchant, level, true) }
        if (hideEnchants == true || (enchants?.isEmpty() == true && glowing == true)) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        if (glowing == true && enchants?.isEmpty() != false) meta.setEnchantmentGlintOverride(true)

        if (meta is LeatherArmorMeta) {
            leathercolor?.let { meta.setColor(it) }
            if (hideDye == true) meta.addItemFlags(ItemFlag.HIDE_DYE)
        }

        if (meta is SkullMeta){
            skullowner?.let { meta.setOwningPlayer(Bukkit.getOfflinePlayer(it)) }
            skullURLTexture?.runCatching { // Prioritise URL Texture if it is set.
                val profile = Bukkit.createPlayerProfile(UUID.randomUUID())
                profile.textures.skin = URL("http://textures.minecraft.net/texture/$skullURLTexture")
                meta.ownerProfile = profile
            }?.getOrNull()

        }

        stack.itemMeta = meta
        return stack
    }

    constructor(item: ItemStack, hex: Boolean? = null): this(item.type, item.amount.takeIf { it != 1 }) {
        val meta = item.itemMeta ?: return
        if (meta.hasLore()) lore(meta.lore!!)
        if (meta.getIdentifier() != null) identifier(meta.getIdentifier()!!)
        if (meta.hasEnchants()) enchants(meta.enchants)
        if (meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) hideEnchants(true)
        val pdc = meta.persistentDataContainer
        pdc.keys.forEach {
            if (pdc.has(it, PersistentDataType.STRING)) addPersistentString(it, pdc.get(it, PersistentDataType.STRING)!!)
            if (pdc.has(it, PersistentDataType.INTEGER)) addPersistentInt(it, pdc.get(it, PersistentDataType.INTEGER)!!)
        }
        apply { this@ItemBuilder.useHex = hex }
        if (meta.isUnbreakable) unbreakable(true)
        if (meta.hasCustomModelData()) customModelData(meta.customModelData)
        if (meta.enchantmentGlintOverride) glowing(true)
        if (meta is LeatherArmorMeta) {
            leathercolor(meta.color)
            if (meta.hasItemFlag(ItemFlag.HIDE_DYE)) hideDye(true)
        }
        if (meta is SkullMeta) {
            if (meta.owningPlayer != null) skullOwner(meta.owningPlayer!!.uniqueId)
        }
    }
}
