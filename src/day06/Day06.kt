package day06

import println
import readInput

fun main() {
    fun solve(input: List<String>, rtl: Boolean): Long { // O(hw)
        if (input.isEmpty()) return 0
        val h = input.size
        val w = input.maxOf { it.length }
        fun ch(i: Int, j: Int) = if (j < input[i].length) input[i][j] else ' '
        val sep = BooleanArray(w) { j -> (0 until h).all { i -> ch(i, j) == ' ' } }
        val blocks = mutableListOf<Pair<Int, Int>>()
        var j = 0
        while (j < w) {
            while (j < w && sep[j]) j++
            if (j >= w) break
            val l = j
            while (j < w && !sep[j]) j++
            blocks += l to (j - 1)
        }
        var ans = 0L
        for ((l, r) in blocks) {
            var op = '+'
            for (c in l..r) {
                val x = ch(h - 1, c)
                if (x == '+' || x == '*') { op = x; break }
            }
            val nums = mutableListOf<Long>()
            if (!rtl) {
                for (i in 0 until h - 1) {
                    val sb = StringBuilder()
                    for (c in l..r) {
                        val x = ch(i, c)
                        if (x in '0'..'9') sb.append(x)
                    }
                    if (sb.isNotEmpty()) nums += sb.toString().toLong()
                }
            } else {
                for (c in r downTo l) {
                    val sb = StringBuilder()
                    for (i in 0 until h - 1) {
                        val x = ch(i, c)
                        if (x in '0'..'9') sb.append(x)
                    }
                    if (sb.isNotEmpty()) nums += sb.toString().toLong()
                }
            }
            var res = if (op == '+') 0L else 1L
            if (op == '+') for (v in nums) res += v else for (v in nums) res *= v
            ans += res
        }
        return ans
    }

    fun part1(input: List<String>) = solve(input, false) // O(hw)
    fun part2(input: List<String>) = solve(input, true)  // O(hw)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
