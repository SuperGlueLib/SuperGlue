package com.github.supergluelib.command

import com.github.supergluelib.command.annotations.NotFullInventory
import com.github.supergluelib.command.annotations.NotSelf
import com.github.supergluelib.foundation.Foundations
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.command.CommandActor
import revxrsal.commands.command.CommandParameter
import revxrsal.commands.exception.CommandErrorException

object LampManager {

    private val plugin get() = Foundations.plugin

    @Deprecated("You no longer need to pass the plugin variable", ReplaceWith("LampManager.create()"))
    /**
     * Get the command handler for your plugin, registered with all of the wrappers extensions.
     * @param handler Your existing command handler if you have one, otherwise creates one for you
     * @return the same [BukkitCommandHandler] (for conciseness)
     */
    fun create(plugin: JavaPlugin, handler: BukkitCommandHandler = BukkitCommandHandler.create(plugin)): BukkitCommandHandler {
        registerParameterValidators(handler)
        return handler
    }

    /**
     * Get the command handler for your plugin, registered with all of the wrappers extensions.
     * @param handler Your existing command handler if you have one, otherwise creates one for you
     * @return the same [BukkitCommandHandler] (for conciseness)
     */
    fun create(handler: BukkitCommandHandler = BukkitCommandHandler.create(plugin)): BukkitCommandHandler {
        registerParameterValidators(handler)
        return handler
    }

    /**
     * @param condition Whether the parameter is valid, i.e. if this returns false, an error will be thrown.
     *
     * Example Implementation:
     *  ```
     *  handler.registerParameterValidator(Player::class.java, NotSelf::class.java, "You cannot specify yourself") {
     *      actor, _, player -> actor.uniqueId != player.uniqueId
     *  }
     *  ```
     *  This example registers a player validator called "NotSelf" which ensures the player does not
     *  specify themselves.
     */
    fun <T, A: Annotation> BukkitCommandHandler.registerParameterValidator(
        constraintClass: Class<T>,
        annotationClass: Class<A>,
        errorMessage: String,
        condition: (CommandActor, CommandParameter, T) -> Boolean
    ): BukkitCommandHandler {
        registerParameterValidator(constraintClass) { value, param, actor ->
            if (param.hasAnnotation(annotationClass) && !condition.invoke(actor, param, value)) throw CommandErrorException(errorMessage)
        }
        return this
    }

    private fun registerParameterValidators(handler: BukkitCommandHandler) {
        handler.registerParameterValidator(Player::class.java, NotSelf::class.java, "You cannot specify yourself") {
              actor, _, player -> actor.uniqueId != player.uniqueId
        }

        handler.registerParameterValidator(Player::class.java, NotFullInventory::class.java, "The target's inventory is full") {
            _, _, target -> target.inventory.firstEmpty() != -1
        }
    }


}