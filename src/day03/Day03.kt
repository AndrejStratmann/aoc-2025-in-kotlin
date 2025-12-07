package day03

import println
import readInput

fun main() {
    fun best(s: String, k: Int): Long {
        var d = s.length - k
        val st = IntArray(s.length)
        var sz = 0
        for (c in s) {
            val x = c - '0'
            while (sz > 0 && st[sz - 1] < x && d > 0) {
                sz--
                d--
            }
            st[sz++] = x
        }
        var r = 0L
        for (i in 0 until k) r = r * 10 + st[i]
        return r
    }

    fun part1(input: List<String>): Long { // O(n)
        var s = 0L
        for (l in input) if (l.isNotEmpty()) s += best(l, 2)
        return s
    }

    fun part2(input: List<String>): Long { // O(n)
        var s = 0L
        for (l in input) if (l.isNotEmpty()) s += best(l, 12)
        return s
    }

    val lines = readInput("Day03")
    part1(lines).println()
    part2(lines).println()
}
