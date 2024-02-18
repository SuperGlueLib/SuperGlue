package com.github.supergluelib.foundation

import org.bukkit.Bukkit

/**Run the code asynchronously */
fun async(code: () -> Unit) = Runnables.async(code)
/** Run the code in the next tick */
fun runNextTick(code: () -> Unit) = Runnables.runNextTick(code)

/**
 * A class implementing regular bukkit runnables but designed to be kotlin-friendly.
 */
object Runnables {

    private val plugin get() = Foundations.plugin

    /** Run the code in the next tick */
    fun runNextTick(code: () -> Unit) = Bukkit.getScheduler().runTaskLater(plugin, code, 0)
    /** Run the code after [ticks] ticks */
    fun runLater(ticks: Int, code: () -> Unit) = Bukkit.getScheduler().runTaskLater(plugin, code, ticks.toLong())
    /** Calls Scheduler.runTask, running the code at the next available interval */
    fun runTask(code: () -> Unit) = Bukkit.getScheduler().runTask(plugin, code)
    /** Calls Scheduler.runTask, synchronously running the code on the next available tick. */
    fun sync(code: () -> Unit) = Bukkit.getScheduler().runTask(plugin, code)

    /** Starts a timer running after every interval
     * @param interval the time between executions in ticks
     * @param delay the time before the first execution in ticks.
     */
    fun runTimer(interval: Int, delay: Int = 0, code: () -> Unit) = Bukkit.getScheduler().runTaskTimer(plugin, code, delay.toLong(), interval.toLong())

    /** Run the code asynchronously */
    fun async(code: () -> Unit) = Bukkit.getScheduler().runTaskAsynchronously(plugin, code)

    /** Runs the code asynchronously if [async] is true, or immediately and synchronously if it is false */
    fun async(async: Boolean, code: () -> Unit) { if (async) async(code) else code.invoke() }

}