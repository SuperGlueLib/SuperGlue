package com.github.supergluelib.gui.templates

import com.github.supergluelib.foundation.extensions.isInt
import com.github.supergluelib.foundation.extensions.send
import com.github.supergluelib.foundation.input.Input
import com.github.supergluelib.foundation.util.ItemBuilder
import com.github.supergluelib.gui.GUI
import com.github.supergluelib.gui.GUIManager
import com.github.supergluelib.gui.Panes
import com.github.supergluelib.gui.fillEmpty
import org.bukkit.Material
import java.util.concurrent.TimeUnit

class TimeSelectorGUI(val withTime: (Pair<Int, TimeUnit>) -> Unit): GUI() {
    var amount: Int? = null
    var timeunit: TimeUnit? = null

    inline val isComplete get() = amount != null && timeunit != null
    private val titleExtension get() = if (isComplete) " ($amount $timeunit)" else ""

    override fun generateInventory() = createInventory("Time Selection$titleExtension", 27) {
        setButton(11, ItemBuilder(Material.OAK_SIGN,"Choose Amount").addLore("&7Currently: ${amount ?: "Unspecified"}").build()) {
            GUIManager.closeGui(player)
            player.send("&6Type the number of units in the chat (example: 15)")
            Input.Chat.take(player) {
                if (it.isInt()) this@TimeSelectorGUI.amount = it.toInt()
                else player.send("&cYou have specified an invalid number '$it'")
                if (isComplete) invalidateInventoryCache() // Update title if necessary
                this@TimeSelectorGUI.open(player)
            }
        }

        setButton(15, ItemBuilder(Material.CLOCK, "Choose Unit").addLore("&7Currently: ${timeunit ?: "Unspecified"}").build()) {
            TimeUnitSelection(this@TimeSelectorGUI).open(player)
        }

        val doneItem = if (isComplete) ItemBuilder(Material.LIME_STAINED_GLASS_PANE, "&aDone")
            .addLore("&7Selected: $amount $timeunit").build()
        else ItemBuilder(Material.RED_STAINED_GLASS_PANE, "&cIncomplete").build()

        setButton(13, doneItem) {
            if (!isComplete) player.send("&cInvalid time specified")
            else withTime.invoke(amount!! to timeunit!!)
        }
    }

    class TimeUnitSelection(val selector: TimeSelectorGUI): GUI() {
        override fun generateInventory() = createInventory("Time Unit Selection", 27) {
            setItem(10, SECONDS)
            setItem(12, MINUTES)
            setItem(14, HOURS)
            setItem(16, DAYS)
            fillEmpty(Panes.BLACK)
        }
        private companion object {
            val SECONDS = ItemBuilder(Material.CLOCK, "&6Seconds").identifier("SECONDS").build()
            val MINUTES = ItemBuilder(Material.CLOCK, "&6Minutes").identifier("MINUTES").build()
            val HOURS = ItemBuilder(Material.CLOCK, "&6Hours").identifier("HOURS").build()
            val DAYS = ItemBuilder(Material.CLOCK, "&6Days").identifier("DAYS").build()
        }

        override fun onClick(click: ClickData) {
            val unit = click.identifier?.let(TimeUnit::valueOf) ?: return
            selector.timeunit = unit
            if (selector.isComplete) selector.invalidateInventoryCache() // Update title if necessary
            selector.open(click.player)
        }
    }
}
