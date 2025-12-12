package day11

import println
import readInput

fun main() {
    fun build(input: List<String>): Pair<Array<IntArray>, Map<String, Int>> {
        val id = HashMap<String, Int>()
        fun get(s: String): Int {
            return id.getOrPut(s) { id.size }
        }
        val adj = ArrayList<MutableList<Int>>()
        fun ensure(i: Int) {
            while (adj.size <= i) adj.add(mutableListOf())
        }
        for (line in input) {
            if (line.isBlank()) continue
            val p = line.split(":")
            if (p.size < 2) continue
            val uName = p[0].trim()
            val u = get(uName)
            ensure(u)
            val rest = p[1].trim()
            if (rest.isNotEmpty()) {
                for (tok in rest.split(" ")) {
                    if (tok.isEmpty()) continue
                    val v = get(tok.trim())
                    ensure(v)
                    adj[u].add(v)
                }
            }
        }
        val g = Array(adj.size) { IntArray(adj[it].size) }
        for (i in adj.indices) {
            val list = adj[i]
            val a = IntArray(list.size)
            for (j in list.indices) a[j] = list[j]
            g[i] = a
        }
        return g to id
    }

    fun part1(input: List<String>): Long { // O(v+e)
        val (g, id) = build(input)
        val s = id["you"] ?: return 0
        val t = id["out"] ?: return 0
        val dp = LongArray(g.size) { -1L }
        fun dfs(u: Int): Long {
            if (u == t) return 1
            if (dp[u] != -1L) return dp[u]
            var r = 0L
            for (v in g[u]) r += dfs(v)
            dp[u] = r
            return r
        }
        return dfs(s)
    }

    fun part2(input: List<String>): Long { // O(v+e)
        val (g, id) = build(input)
        val s = id["svr"] ?: return 0
        val t = id["out"] ?: return 0
        val dac = id["dac"] ?: return 0
        val fft = id["fft"] ?: return 0
        val n = g.size
        val dp = Array(n) { LongArray(4) { -1L } }
        fun dfs(u: Int, m: Int): Long {
            var mm = m
            if (u == dac) mm = mm or 1
            if (u == fft) mm = mm or 2
            if (u == t) return if (mm == 3) 1 else 0
            if (dp[u][mm] != -1L) return dp[u][mm]
            var r = 0L
            for (v in g[u]) r += dfs(v, mm)
            dp[u][mm] = r
            return r
        }
        return dfs(s, 0)
    }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
