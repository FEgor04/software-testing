package com.jellyone

import java.io.File
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

fun sec(x: Double, precision: Double): Double {
    return 1 / cos(x, precision)
}

fun csc(x: Double, precision: Double): Double {
    return 1 / sin(x, precision)
}

fun complexExpression(x: Double, precision: Double): Double {
    val secX = sec(x, precision)
    val cscX = csc(x, precision)
    val cosX = cos(x, precision)
    val sinX = sin(x, precision)
    val cotX = cosX / sinX

    val log5X = log(x, 5.0, precision)
    val log2X = log(x, 2.0, precision)
    val log10X = log(x, 10.0, precision)
    val lnX = ln(x, precision)
    val log3X = log(x, 3.0, precision)


    val result = if (x <= 0) {
        ((((secX - secX) - cscX) + cosX) / (sinX / cotX) + (cotX * sinX))
    } else {
        ((((log5X * log2X) / log10X) * (log5X + lnX)) + (log3X - log10X)) - (((lnX * lnX) - (lnX * log5X)) / log10X)
    }

    print("$x,$secX,$cscX,$cosX,$sinX,$cotX,$log5X,$log2X,$log10X,$lnX,$log3X,$result\n")
    return result
}

fun writeToCSV(filename: String, start: Double, end: Double, step: Double, precision: Double) {
    val file = File(filename)
    file.bufferedWriter().use { writer ->
        writer.write("X,Result\n")
        var x = start
        while (x <= end) {
            val result = complexExpression(x, precision)
            writer.write("$x,$result\n")
            x += step
        }
    }
}

fun main() {
    print("x,secX,cscX,cosX,sinX,cotX,log5X,log2X,log10X,lnX,log3X,result\n")
    val precision = 1e-10
    writeToCSV("results.csv", -PI, 10.0, 0.1, precision)
}