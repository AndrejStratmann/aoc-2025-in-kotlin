package day07

import println
import readInput

fun main() {
    fun solve(input: List<String>): Pair<Long, Long> { // O(hw)
        val h = input.size
        val w = input[0].length
        var sr = 0
        var sc = 0
        for (i in 0 until h)
            for (j in 0 until w)
                if (input[i][j] == 'S') { sr = i; sc = j }
        val cur = LongArray(w)
        val nxt = LongArray(w)
        cur[sc] = 1
        var splits = 0L
        var timelines = 0L
        for (i in sr until h) {
            java.util.Arrays.fill(nxt, 0)
            val row = input[i]
            for (j in 0 until w) {
                val c = cur[j]
                if (c == 0L) continue
                when (row[j]) {
                    '^' -> {
                        splits += c
                        val ni = i + 1
                        if (ni >= h) {
                            timelines += c * 2
                        } else {
                            if (j > 0) nxt[j - 1] += c else timelines += c
                            if (j + 1 < w) nxt[j + 1] += c else timelines += c
                        }
                    }
                    else -> {
                        val ni = i + 1
                        if (ni >= h) timelines += c else nxt[j] += c
                    }
                }
            }
            for (j in 0 until w) cur[j] = nxt[j]
        }
        return splits to timelines
    }

    fun part1(input: List<String>) = solve(input).first  // O(hw)
    fun part2(input: List<String>) = solve(input).second // O(hw)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
