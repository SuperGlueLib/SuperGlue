package com.github.supergluelib.command

import com.github.supergluelib.command.LampManager.handler
import com.github.supergluelib.command.annotations.NotFullInventory
import com.github.supergluelib.command.annotations.NotSelf
import com.github.supergluelib.foundation.Foundations
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import revxrsal.commands.Lamp
import revxrsal.commands.bukkit.BukkitLamp
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.command.CommandActor
import revxrsal.commands.exception.CommandErrorException
import revxrsal.commands.node.ParameterNode

object LampManager {

    private val plugin get() = Foundations.plugin

    lateinit var handler: Lamp<BukkitCommandActor> private set

    /**
     * Get the command handler for your plugin, registered with all the wrapper's extensions.
     * @param handler Your existing command handler if you have one, otherwise creates one for you
     * @return the same [BukkitCommandHandler] (for conciseness)
     */
    fun create(init: (Lamp.Builder<BukkitCommandActor>) -> Unit): Lamp<BukkitCommandActor> {
        this.handler = BukkitLamp
            .builder(plugin)
            .registerSuperGlueDefaults()
            .apply(init)
            .build()

        return handler
    }

    fun Foundations.setupCommands(init: (Lamp.Builder<BukkitCommandActor>) -> Unit): Foundations = apply { create(init) }

    fun Foundations.registerCommands(vararg commands: Any): Foundations {
        if (!this@LampManager::handler.isInitialized) create {}
        handler.register(*commands)
        return this
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
    fun <T, A: Annotation> Lamp.Builder<BukkitCommandActor>.registerParameterValidator(
        constraintClass: Class<T>,
        annotationClass: Class<A>,
        errorMessage: String,
        condition: (CommandActor, ParameterNode<BukkitCommandActor, T>, T) -> Boolean
    ): Lamp.Builder<BukkitCommandActor> = parameterValidator(constraintClass) { actor, value, param, _ ->
        if (param.annotations().contains(annotationClass) && !condition.invoke(actor, param, value)) throw CommandErrorException(errorMessage)
    }

    private fun Lamp.Builder<BukkitCommandActor>.registerSuperGlueDefaults(): Lamp.Builder<BukkitCommandActor> {
        registerParameterValidator(Player::class.java, NotSelf::class.java, "You cannot specify yourself") {
              actor, _, player -> actor.uniqueId() != player.uniqueId
        }

        registerParameterValidator(Player::class.java, NotFullInventory::class.java, "The target's inventory is full") {
            _, _, target -> target.inventory.firstEmpty() != -1
        }

        suggestionProviders { providers ->
            providers.addProvider(Player::class.java) { _ -> Bukkit.getOnlinePlayers().map { it.name } }
        }

        return this
    }

}