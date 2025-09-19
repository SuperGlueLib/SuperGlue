package com.github.supergluelib.gui

import com.github.supergluelib.foundation.util.ItemBuilder
import org.bukkit.Material

object Panes {
    internal const val paneIdentifier = "menu_pane"

    private fun pane(type: Material) = ItemBuilder(type, "&7").identifier(paneIdentifier).hideToolTip().build()

    val CLEAR get() = pane(Material.GLASS_PANE).clone()
    val WHITE get() = pane(Material.WHITE_STAINED_GLASS_PANE).clone()
    val YELLOW get() = pane(Material.YELLOW_STAINED_GLASS_PANE).clone()
    val ORANGE get() = pane(Material.ORANGE_STAINED_GLASS_PANE).clone()
    val RED get() = pane(Material.RED_STAINED_GLASS_PANE).clone()
    val LIME get() = pane(Material.LIME_STAINED_GLASS_PANE).clone()
    val GREEN get() = pane(Material.GREEN_STAINED_GLASS_PANE).clone()
    val CYAN get() = pane(Material.CYAN_STAINED_GLASS_PANE).clone()
    val BLUE get() = pane(Material.LIGHT_BLUE_STAINED_GLASS_PANE).clone()
    val LIGHT_BLUE get() = pane(Material.LIGHT_BLUE_STAINED_GLASS_PANE).clone()
    val MAGENTA get() = pane(Material.MAGENTA_STAINED_GLASS_PANE).clone()
    val PINK get() = pane(Material.PINK_STAINED_GLASS_PANE).clone()
    val PURPLE get() = pane(Material.PURPLE_STAINED_GLASS_PANE).clone()
    val LIGHT_GRAY get() = pane(Material.LIGHT_GRAY_STAINED_GLASS_PANE).clone()
    val GRAY get() = pane(Material.GRAY_STAINED_GLASS_PANE).clone()
    val BROWN get() = pane(Material.BROWN_STAINED_GLASS_PANE).clone()
    val BLACK get() = pane(Material.BLACK_STAINED_GLASS_PANE).clone()

    private val allPanes = listOf(
        CLEAR, WHITE, YELLOW, ORANGE, RED, LIME, GREEN, CYAN, BLUE, LIGHT_BLUE,
        MAGENTA, PINK, PURPLE , LIGHT_GRAY, GRAY, BROWN, BLACK
    )

    fun random() = allPanes.random()
}