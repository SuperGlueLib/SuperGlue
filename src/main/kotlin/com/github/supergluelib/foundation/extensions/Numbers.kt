package com.github.supergluelib.foundation.extensions

import java.text.DecimalFormat
import kotlin.math.floor

@Deprecated("Unstable for now, may be fixed in a later version")
fun Double.round(precision: Int): Double = 10.pow(precision).let { if (precision == 0) floor(this) else (this*it)/it }
@Deprecated("Unstable for now, may be fixed in a later version")
fun Float.round(precision: Int): Float = toDouble().round(precision).toFloat()
fun Double.format(decimals: Int = 2) = DecimalFormat.getInstance().apply { maximumFractionDigits = decimals }.apply { isGroupingUsed = true }.apply { minimumFractionDigits = 2 }.format(this)
fun Int.format() = DecimalFormat.getIntegerInstance().apply { isGroupingUsed = true }.format(this)
fun Long.format() = DecimalFormat.getIntegerInstance().apply { isGroupingUsed = true }.format(this)
infix fun Int.pow(other: Int): Int = (1..other).fold(1) { acc, _ -> acc * this }