package day01

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int { // O(n)
        var p = 50
        var c = 0
        for (s in input) {
            if (s.isEmpty()) continue
            val d = s.substring(1).toLong() % 100
            p = when (s[0]) {
                'L' -> (p - d.toInt() + 100) % 100
                'R' -> (p + d.toInt()) % 100
                else -> error("")
            }
            if (p == 0) c++
        }
        return c
    }

    fun part2(input: List<String>): Int { // O(n)
        var p = 50
        var c = 0L
        for (s in input) {
            if (s.isEmpty()) continue
            val dir = s[0]
            val d = s.substring(1).toLong()
            if (d > 0) {
                val f = when (dir) {
                    'R' -> if (p == 0) 100 else 100 - p
                    'L' -> if (p == 0) 100 else p
                    else -> error("")
                }
                if (d >= f) c += 1 + (d - f) / 100
            }
            val m = (d % 100).toInt()
            p = when (dir) {
                'L' -> (p - m + 100) % 100
                'R' -> (p + m) % 100
                else -> error("")
            }
        }
        return c.toInt()
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
