package com.jellyone

import kotlin.math.PI
import kotlin.math.abs

fun sin(x: Double, precision: Double): Double {
    var term = x // First term in the series
    var sum = term
    var n = 1

    while (abs(term) > precision) {
        term *= -x * x / ((2 * n) * (2 * n + 1))
        sum += term
        n++
    }

    return sum
}

fun ln(x: Double, precision: Double): Double {
    if (x <= 0) throw IllegalArgumentException("ln(x) is undefined for x <= 0")
    if (x == 1.0) return 0.0

    val y = (x - 1) / (x + 1)
    var term = y
    var sum = term
    var n = 1

    while (abs(term) > precision) {
        term *= y * y
        sum += term / (2 * n + 1)
        n++
    }

    return 2 * sum
}

fun log(x: Double, base: Double, precision: Double): Double {
    return ln(x, precision) / ln(base, precision)
}

fun cos(x: Double, precision: Double) = sin(x + PI/2, precision)

fun tan(x: Double, precision: Double) = sin(x, precision) / cos(x, precision)