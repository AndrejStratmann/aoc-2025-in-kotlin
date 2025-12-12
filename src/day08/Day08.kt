package day08

import println
import readInput

fun main() {
    data class P(val x: Long, val y: Long, val z: Long)
    data class E(val d: Long, val i: Int, val j: Int)

    fun solve(input: List<String>): Pair<Long, Long> {
        val pts = input.filter { it.isNotEmpty() }.map {
            val (x, y, z) = it.split(',')
            P(x.toLong(), y.toLong(), z.toLong())
        }
        val n = pts.size
        val es = ArrayList<E>()
        for (i in 0 until n) for (j in i + 1 until n) {
            val a = pts[i]
            val b = pts[j]
            val dx = a.x - b.x
            val dy = a.y - b.y
            val dz = a.z - b.z
            es.add(E(dx * dx + dy * dy + dz * dz, i, j))
        }
        es.sortBy { it.d }

        fun runPart1(): Long {
            val p = IntArray(n) { it }
            val sz = IntArray(n) { 1 }
            fun find(x0: Int): Int {
                var x = x0
                while (p[x] != x) {
                    p[x] = p[p[x]]
                    x = p[x]
                }
                return x
            }
            fun union(a0: Int, b0: Int) {
                var a = find(a0)
                var b = find(b0)
                if (a == b) return
                if (sz[a] < sz[b]) {
                    val t = a
                    a = b
                    b = t
                }
                p[b] = a
                sz[a] += sz[b]
            }
            val k = 1000.coerceAtMost(es.size)
            for (i in 0 until k) {
                val e = es[i]
                union(e.i, e.j)
            }
            val comp = mutableListOf<Int>()
            for (i in 0 until n) if (p[i] == i) comp.add(sz[i])
            comp.sortDescending()
            return comp.take(3).fold(1L) { acc, v -> acc * v }
        }

        fun runPart2(): Long {
            val p = IntArray(n) { it }
            val sz = IntArray(n) { 1 }
            fun find(x0: Int): Int {
                var x = x0
                while (p[x] != x) {
                    p[x] = p[p[x]]
                    x = p[x]
                }
                return x
            }
            fun union(a0: Int, b0: Int): Boolean {
                var a = find(a0)
                var b = find(b0)
                if (a == b) return false
                if (sz[a] < sz[b]) {
                    val t = a
                    a = b
                    b = t
                }
                p[b] = a
                sz[a] += sz[b]
                return true
            }
            var comps = n
            var xLast = 0L
            var yLast = 0L
            for (e in es) {
                if (union(e.i, e.j)) {
                    comps--
                    if (comps == 1) {
                        xLast = pts[e.i].x
                        yLast = pts[e.j].x
                        break
                    }
                }
            }
            return xLast * yLast
        }

        return runPart1() to runPart2()
    }

    fun part1(input: List<String>): Long { // O(n^2 log n)
        return solve(input).first
    }

    fun part2(input: List<String>): Long { // O(n^2 log n)
        return solve(input).second
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
