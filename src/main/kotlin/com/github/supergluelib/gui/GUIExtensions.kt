package com.github.supergluelib.gui

import com.github.supergluelib.foundation.util.ItemBuilder
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

interface GUIExtensions {

    /** @return true if the inventory has no empty slots */
    fun Inventory.isFull() = firstEmpty() == -1

    /** An iterable range of each slot in the inventory [0-size) */
    fun Inventory.getSlots() = (0 until size)

    /** @return the amount of rows this inventory has */
    fun Inventory.getRows() = size/9

    /** Sets each item in the [row]
     *  @param row starting at 0
     */
    fun Inventory.setRow(row: Int, item: ItemStack) =  setItems(item, ((9*row)until (9*row+9)))

    /** Sets each item in each of the [rows]
     * @param rows starting at 0
     */
    fun Inventory.setRows(item: ItemStack, vararg rows: Int) = rows.forEach { setRow(it, item) }

    /** Sets each item in the [column]
     * @param column starting at 0
     */
    fun Inventory.setColumn(column: Int, item: ItemStack) = setItems(item, (column until size step 9))

    /** Sets each item in each of the [columns]
     * @param columns starting at 0
     */
    fun Inventory.setColumns(item: ItemStack, vararg columns: Int) = columns.forEach { setColumn(it, item) }

    /** Sets the borders (outline) of the inventory to [item] */
    fun Inventory.setBorder(item: ItemStack) = apply {
        setRow(0, item)
        setRow(getRows()-1, item)
        setColumn(0, item)
        setColumn(8, item)
    }

    /** Set each currently air/null slot in the inventory to [item] */
    fun Inventory.fillEmpty(item: ItemStack) = apply { getSlots().forEach { if (isSlotEmpty(it)) setItem(it, item) } }
    /** @return true if the slot is air or null (empty) */
    fun Inventory.isSlotEmpty(slot: Int) = getItem(slot)?.type?.isAir != false

    /** Sets each slot in [slots] to [item]. */
    fun Inventory.setItems(item: ItemStack, vararg slots: Int) = slots.forEach { setItem(it, item) }
    /** Sets each slot in [slots] to [item]. */
    fun Inventory.setItems(item: ItemStack, slots: Iterable<Int>) = slots.forEach { setItem(it, item) }
    /** Sets the [item] every [increment] slots for [reps] amount of times */
    fun Inventory.setItemIncrementally(item: ItemStack, slot: Int, increment: Int, reps: Int) {
        var times = 0
        var slot = slot
        while (times <= reps && slot < size) {
            setItem(slot, item)
            times++
            slot += increment
        }
    }
    /** Sets the item at the specific row and column ( starting at 0 ) */
    fun Inventory.setItem(row: Int, column: Int, item: ItemStack) = setItem(( row * 9 ) + column, item)

    /** Set the item in the given slot */
    fun Inventory.setItem(slot: Int, item: () -> ItemStack) = setItem(slot, item.invoke())
    /** Set the item in the given slot to the item represented by the supplied item builder */
    fun Inventory.setItemBuilder(slot: Int, itemBuilder: () -> ItemBuilder) = setItem(slot, itemBuilder.invoke().build())
    /** Set the item in the given slot to the item represented by the supplied item builder */
    fun Inventory.setItemBuilder(slot: Int, item: ItemBuilder) = setItem(slot, item.build())

    fun Inventory.getNextAvailableSlot(startingAt: Int = 0) = (startingAt until size).first { isSlotEmpty(it) }
    fun Inventory.setNextAvailableSlot(startingAt: Int = 0, item: ItemStack) = setItem(getNextAvailableSlot(startingAt), item)
    fun Inventory.setNextAvailableSlot(startingAt: Int = 0, item: () -> ItemStack) = setNextAvailableSlot(startingAt, item.invoke())
    fun Inventory.setNextAvailableSlots(startingAt: Int = 0, items: List<ItemStack>) =  apply { items.forEach { setNextAvailableSlot(startingAt, it) } }
    fun Inventory.setNextAvailableSlots(startingAt: Int = 0, items: () -> List<ItemStack>) = setNextAvailableSlots(startingAt, items.invoke())
    fun Collection<ItemStack>.addToInventory(inventory: Inventory, startingAt: Int = 0) = inventory.setNextAvailableSlots(startingAt, this.toList())

    enum class InventoryCorner { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }
    fun Inventory.getCornerSlot(corner: InventoryCorner) = when (corner) {
        InventoryCorner.TOP_LEFT -> 0
        InventoryCorner.TOP_RIGHT -> 9
        InventoryCorner.BOTTOM_LEFT -> size-9
        InventoryCorner.BOTTOM_RIGHT -> size-1
    }
    fun Inventory.getCornerWithFlare(corner: InventoryCorner) = when (corner){
        InventoryCorner.TOP_LEFT -> listOf(0,1,9)
        InventoryCorner.TOP_RIGHT -> listOf(7,8,17)
        InventoryCorner.BOTTOM_LEFT -> getCornerSlot(InventoryCorner.BOTTOM_LEFT).let { listOf(it,it+1,it-9) }
        InventoryCorner.BOTTOM_RIGHT -> getCornerSlot(InventoryCorner.BOTTOM_RIGHT).let { listOf(it, it-1, it-9) }
    }

}