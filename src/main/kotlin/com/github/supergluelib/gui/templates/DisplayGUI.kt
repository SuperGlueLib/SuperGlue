package com.github.supergluelib.gui.templates

import com.github.supergluelib.gui.GUI
import com.github.supergluelib.gui.Panes
import com.github.supergluelib.gui.fillEmpty
import com.github.supergluelib.gui.setNextAvailableSlots
import org.bukkit.inventory.ItemStack

/**
 * Displays a list of items in a dynamically sized inventory with a black stained pane outline
 *
 * Currently only accepts up to 28 items, this is subject to change once the pagination update is implemented.
 */
class DisplayGUI(val name: String, val items: List<ItemStack>): GUI() {
    // Generated values
    val size = 27 + (9 * ((items.size-1).coerceAtLeast(0)/7)).coerceAtMost(27)

    constructor(name: String, items: () -> List<ItemStack>): this(name, items.invoke())
    constructor(name: String, items: List<ItemStack>, onClick: ClickData.() -> Unit): this(name, items){
        setOnClick(onClick)
    }

    // Builder Values
    private var border = Panes.BLACK
    private var onClick: (ClickData.() -> Unit)? = null

    // Builder Methods
    fun setBorder(item: ItemStack) = apply { border = item }
    fun setOnClick(onClick: (ClickData.() -> Unit)?) = apply { this.onClick = onClick }

    override fun generateInventory() = createInventory(name, size) {
        setBorder(border)
        setNextAvailableSlots { items.take(28) }
        fillEmpty(border)
    }

    override fun onClick(click: ClickData) {
        onClick?.invoke(click)
    }
}