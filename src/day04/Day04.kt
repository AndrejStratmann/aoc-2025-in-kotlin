package day04

import println
import readInput

fun main() {
    val dx = intArrayOf(-1,-1,-1,0,0,1,1,1)
    val dy = intArrayOf(-1,0,1,-1,1,-1,0,1)

    fun part1(input: List<String>): Int { // O(n)
        val h = input.size
        if (h == 0) return 0
        val w = input[0].length
        var ans = 0
        for (y in 0 until h) for (x in 0 until w) if (input[y][x] == '@') {
            var c = 0
            for (k in 0..7) {
                val ny = y + dy[k]
                val nx = x + dx[k]
                if (ny in 0 until h && nx in 0 until w && input[ny][nx] == '@') c++
            }
            if (c < 4) ans++
        }
        return ans
    }

    fun part2(input: List<String>): Int { // O(n)
        val h = input.size
        if (h == 0) return 0
        val w = input[0].length
        val g = Array(h) { input[it].toCharArray() }
        val cnt = Array(h) { IntArray(w) }
        for (y in 0 until h) for (x in 0 until w) if (g[y][x] == '@') {
            var c = 0
            for (k in 0..7) {
                val ny = y + dy[k]
                val nx = x + dx[k]
                if (ny in 0 until h && nx in 0 until w && g[ny][nx] == '@') c++
            }
            cnt[y][x] = c
        }
        val q = ArrayDeque<Int>()
        for (y in 0 until h) for (x in 0 until w)
            if (g[y][x] == '@' && cnt[y][x] < 4) q.addLast(y * w + x)
        var rem = 0
        while (q.isNotEmpty()) {
            val v = q.removeFirst()
            val y = v / w
            val x = v % w
            if (g[y][x] != '@' || cnt[y][x] >= 4) continue
            g[y][x] = '.'
            rem++
            for (k in 0..7) {
                val ny = y + dy[k]
                val nx = x + dx[k]
                if (ny in 0 until h && nx in 0 until w && g[ny][nx] == '@') {
                    cnt[ny][nx]--
                    if (cnt[ny][nx] < 4) q.addLast(ny * w + nx)
                }
            }
        }
        return rem
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
