package day05

import println
import readInput

fun main() {
    fun solve(input: List<String>): Pair<Int, Long> {
        val b = input.indexOf("")
        val rs = input.take(b).map {
            val (a, c) = it.split('-')
            a.toLong()..c.toLong()
        }
        val ids = input.drop(b + 1).filter { it.isNotEmpty() }.map { it.toLong() }
        val m = mutableListOf<LongRange>()
        for (r in rs.sortedBy { it.first }) {
            if (m.isEmpty()) m.add(r)
            else {
                val last = m.last()
                if (r.first <= last.last + 1) m[m.lastIndex] = last.first..maxOf(last.last, r.last)
                else m.add(r)
            }
        }
        var fresh = 0
        for (v in ids) {
            for (r in m) {
                if (v < r.first) break
                if (v <= r.last) {
                    fresh++
                    break
                }
            }
        }
        var total = 0L
        for (r in m) total += r.last - r.first + 1
        return fresh to total
    }

    fun part1(input: List<String>): Int { // O(n log n)
        return solve(input).first
    }

    fun part2(input: List<String>): Long { // O(n log n)
        return solve(input).second
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
