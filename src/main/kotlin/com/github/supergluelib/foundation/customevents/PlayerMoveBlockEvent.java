package com.github.supergluelib.foundation.customevents;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

/**
 * A SubSet of PlayerMoveEvent which is run when a players block value changes.
 *
 * Requires registering the CustomEventListener
 * @see CustomEventListener
 */

public class PlayerMoveBlockEvent extends PlayerMoveEvent {

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    private static HandlerList HANDLERS_LIST = new HandlerList();
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }


    public PlayerMoveBlockEvent(@NotNull Player player, @NotNull Location from, @NotNull Location to) {
        super(player, from, to);
    }


}