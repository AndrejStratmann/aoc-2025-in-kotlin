package day02

import println
import readInput
import kotlin.math.pow

fun main() {
    fun solve(input: List<String>): Pair<Long, Long> {
        val r = input[0].split(',').filter { it.isNotEmpty() }.map {
            val (a, b) = it.split('-'); a.toLong()..b.toLong()
        }
        val lo = r.minOf { it.first }
        val hi = r.maxOf { it.last }
        val ml = hi.toString().length
        val s1 = mutableSetOf<Long>()
        val s2 = mutableSetOf<Long>()
        fun inR(x: Long) = r.any { x in it }
        for (h in 1..ml / 2) {
            val a = 10.0.pow((h - 1).toDouble()).toLong()
            val b = 10.0.pow(h.toDouble()).toLong()
            for (base in a until b) {
                val t = base.toString()
                var rep = t + t
                for (k in 2..ml / h) {
                    val v = rep.toLong()
                    if (v > hi) break
                    if (v >= lo && inR(v)) {
                        s2.add(v)
                        if (k == 2) s1.add(v)
                    }
                    rep += t
                }
            }
        }
        return s1.sum() to s2.sum()
    }

    fun part1(input: List<String>): Long { // O(n)
        return solve(input).first
    }

    fun part2(input: List<String>): Long { // O(n)
        return solve(input).second
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
