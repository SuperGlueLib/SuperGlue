package com.github.supergluelib.command.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
/**
 * Ensure that the target player is not the command sender.
 */
annotation class NotSelf

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
/**
 * Ensure that the target player's inventory has at least one empty slot available.
 */
annotation class NotFullInventory