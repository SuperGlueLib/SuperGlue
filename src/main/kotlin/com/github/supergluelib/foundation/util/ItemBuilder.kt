package com.github.supergluelib.foundation.util

import com.github.supergluelib.customitem.SuperItems.getIdentifier
import com.github.supergluelib.customitem.SuperItems.setIdentifier
import com.github.supergluelib.foundation.Foundations
import com.github.supergluelib.foundation.extensions.toColor
import com.github.supergluelib.foundation.extensions.toHashMap
import com.github.supergluelib.foundation.itembuilder.ActionsModule
import com.github.supergluelib.foundation.util.ItemBuilder.Companion.registerProcessingStep
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
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

    companion object {
        private val processingSteps = mutableListOf<(ItemMeta, ItemBuilder) -> Unit>()

        /**
         * - This method allows you to register global ItemBuilder functionality within the build() function.
         * - This is encouraged to be used in tandem with extension functions and [ItemBuilder.properties] to achieve fully integrated functionality.
         * - Processing occurs *after* default properties have been applied allowing you to override existing properties.
         * - Modify the ItemMeta object directly.
         */
        fun registerProcessingStep(processing: (ItemMeta, ItemBuilder) -> Unit) { processingSteps.add(processing) }


        /** @see registerProcessingStep */
        fun Foundations.registerItemBuilderProcess(process: (ItemMeta, ItemBuilder) -> Unit) = registerProcessingStep(process)
    }

    // TODO: Potions & Skulls submodules as well? && Ensure support for Actions instance reuse

    var actions: ActionsModule? = null
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
    var itemModel: NamespacedKey? = null

    var leathercolor: Color? = null

    var skullowner: UUID? = null
    var skullURLTexture: String? = null // This is the B64

    /**
     * This variable is used to add custom functionality to the Item Builder, for example, via extension functions which set custom properties
     * and then use the process registry (via [Foundations.registerItemBuilderProcess] or [registerProcessingStep]) to handle their implementation.
     *
     * properties are specific to this item builder instance and can be anything, they can be set or accessed at any time.
     */
    val properties: MutableMap<String, Any> = mutableMapOf()

    fun name(name: String) = apply { this.name = name; }
    fun lore(lines: List<String>) = apply { this.lore = ArrayList(lines) }
    fun addLores(lines: List<String>) = apply { lore = (lore ?: ArrayList()).apply { addAll(lines) } }
    fun addLore(vararg line: String) = addLores(line.toList())
    fun addEnchant(enchant: Enchantment, level: Int) = apply { if (enchants == null) enchants = hashMapOf(enchant to level) else enchants!![enchant] = level }
    fun enchants(enchants: Map<Enchantment, Int>) = apply { this@ItemBuilder.enchants = enchants.toHashMap() }
    fun hideEnchants(hide: Boolean) = apply { hideEnchants = hide }
    /** Attaches a custom identifier to the metadata (NBT) of your ItemStack */
    fun identifier(id: String) = apply { this.identifier = id }
    fun addPersistentInt(key: NamespacedKey, data: Int) = apply { if (persistentInts == null) persistentInts = hashMapOf(key to data) else persistentInts!![key] = data }
    fun addPersistentString(key: NamespacedKey, data: String) = apply { if (persistentStrings == null) persistentStrings = hashMapOf(key to data) else persistentStrings!![key] = data }
    fun customModelData(model: Int) = apply { customModelData = model }
    fun customModelData(vararg model: String) = apply { customModelDataStrings = model.toList() }
    fun itemModel(model: NamespacedKey) = apply { itemModel = model }
    /** Automatically constructs NamespacedKey from your plugin instance */
    fun itemModel(itemName: String) = apply { itemModel = NamespacedKey(Foundations.plugin, itemName) }

    fun hex(use: Boolean) = apply { useHex = use }
    fun unbreakable(unbreakable: Boolean) = apply { this.unbreakable = unbreakable }
    fun glowing(glowing: Boolean) = apply { this.glowing = glowing }

    fun leathercolor(color: Color) = apply { leathercolor = color }
    fun hideDye(hide: Boolean) = apply { hideDye = hide }

    fun skullOwner(player: UUID) = apply { skullowner = player }
    fun skullURLTexture(urlTail: String) = apply { skullURLTexture = urlTail }

    fun actions(identifier: String, actions: (ActionsModule) -> Unit) = apply {
        this.identifier(identifier)
        this.actions = ActionsModule().also(actions)
    }

    fun build(): ItemStack {
        val stack = ItemStack(type, amount ?: 1)
        val meta = stack.itemMeta!!
        if (name != null) meta.setDisplayName(name!!.toColor(useHex ?: false))
        if (lore != null) meta.lore = lore!!.map { it.toColor(useHex ?: false) }
        identifier?.let { meta.setIdentifier(identifier!!) }
        unbreakable?.let(meta::setUnbreakable)
        itemModel?.let(meta::setItemModel)
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
        if (glowing == true && (enchants == null || enchants!!.isEmpty())) meta.setEnchantmentGlintOverride(true)

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

        if (processingSteps.isNotEmpty()) {
            processingSteps.forEach { step -> step.invoke(meta, this) }
        }

        actions?.register(identifier!!)

        stack.itemMeta = meta
        return stack
    }

    constructor(item: ItemStack, hex: Boolean? = null): this(item.type, item.amount.takeIf { it != 1 }) {
        val meta: ItemMeta = item.itemMeta ?: return
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