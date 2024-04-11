package com.github.supergluelib.foundation.util

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.time.Duration
import kotlin.math.ceil

/**
 * A utility class simplifying adding and checking cooldowns
 * @param duration The duration to use for this cooldown
 */
class CooldownMap<T>(val duration: Duration) {
    private val cache: Cache<T, Long> = CacheBuilder.newBuilder().expireAfterWrite(duration).build<T, Long>()

    /** Add a cooldown to this target */
    fun add(target: T) { cache.put(target!!, System.currentTimeMillis() + duration.toMillis()) }
    /** Cancel the cooldown for this target */
    fun cancel(target: T) = cache.invalidate(target!!)

    /** @return true if this target has a cooldown */
    fun has(target: T) = cache.getIfPresent(target!!) != null
    /** @return the milliseconds left on this targets cooldown, or 0 */
    fun getMillis(target: T): Long = cache.getIfPresent(target!!) ?: 0
    /** @return the seconds left on this targets cooldown or 0, rounded up */
    fun getSeconds(target: T): Int = ceil(getMillis(target) / 1000.0).toInt()
}