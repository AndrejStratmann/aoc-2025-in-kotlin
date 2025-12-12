package day09

import println
import readInput

fun main() {
    fun parse(input: List<String>): List<IntArray> {
        val r = ArrayList<IntArray>()
        for (l in input) {
            val s = l.trim()
            if (s.isEmpty()) continue
            val p = s.split(',')
            r.add(intArrayOf(p[0].trim().toInt(), p[1].trim().toInt()))
        }
        return r
    }

    fun part1(input: List<String>): Long { // O(n^2)
        val pts = parse(input)
        val n = pts.size
        if (n == 0) return 0L
        var best = 0L
        for (i in 0 until n) for (j in i + 1 until n) {
            val dx = kotlin.math.abs(pts[i][0] - pts[j][0])
            val dy = kotlin.math.abs(pts[i][1] - pts[j][1])
            if (dx == 0 || dy == 0) continue
            val area = (dx + 1L) * (dy + 1L)
            if (area > best) best = area
        }
        return best
    }

    fun part2(input: List<String>): Long { // O(n^2+hw)
        val raw = parse(input)
        val n = raw.size
        if (n == 0) return 0L
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE
        for (p in raw) {
            val x = p[0]
            val y = p[1]
            if (x < minX) minX = x
            if (x > maxX) maxX = x
            if (y < minY) minY = y
            if (y > maxY) maxY = y
        }
        val w = maxX - minX + 1
        val h = maxY - minY + 1
        val px = IntArray(n)
        val py = IntArray(n)
        val wall = Array(h) { BooleanArray(w) }
        for (i in 0 until n) {
            val x = raw[i][0] - minX
            val y = raw[i][1] - minY
            px[i] = x
            py[i] = y
            wall[y][x] = true
        }
        for (i in 0 until n) {
            val x1 = px[i]
            val y1 = py[i]
            val j = (i + 1) % n
            val x2 = px[j]
            val y2 = py[j]
            if (x1 == x2) {
                val a = if (y1 < y2) y1 else y2
                val b = if (y1 < y2) y2 else y1
                for (y in a..b) wall[y][x1] = true
            } else {
                val a = if (x1 < x2) x1 else x2
                val b = if (x1 < x2) x2 else x1
                for (x in a..b) wall[y1][x] = true
            }
        }
        val out = Array(h + 2) { BooleanArray(w + 2) }
        val q = ArrayDeque<Pair<Int, Int>>()
        q.add(0 to 0)
        out[0][0] = true
        val d = intArrayOf(1, 0, -1, 0, 1)
        while (q.isNotEmpty()) {
            val (cy, cx) = q.removeFirst()
            for (k in 0..3) {
                val ny = cy + d[k]
                val nx = cx + d[k + 1]
                if (ny < 0 || ny > h + 1 || nx < 0 || nx > w + 1 || out[ny][nx]) continue
                if (ny in 1..h && nx in 1..w && wall[ny - 1][nx - 1]) continue
                out[ny][nx] = true
                q.add(ny to nx)
            }
        }
        val good = Array(h) { BooleanArray(w) }
        for (y in 0 until h) for (x in 0 until w)
            if (wall[y][x] || !out[y + 1][x + 1]) good[y][x] = true
        val pref = Array(h + 1) { LongArray(w + 1) }
        for (y in 0 until h) {
            var row = 0L
            for (x in 0 until w) {
                if (good[y][x]) row++
                pref[y + 1][x + 1] = pref[y][x + 1] + row
            }
        }
        var best = 0L
        for (i in 0 until n) for (j in i + 1 until n) {
            val x1 = px[i]
            val y1 = py[i]
            val x2 = px[j]
            val y2 = py[j]
            val dx = kotlin.math.abs(x1 - x2)
            val dy = kotlin.math.abs(y1 - y2)
            if (dx == 0 || dy == 0) continue
            val lx = if (x1 < x2) x1 else x2
            val rx = if (x1 < x2) x2 else x1
            val ly = if (y1 < y2) y1 else y2
            val ry = if (y1 < y2) y2 else y1
            val wCells = dx + 1
            val hCells = dy + 1
            val area = wCells.toLong() * hCells
            val sum = pref[ry + 1][rx + 1] - pref[ly][rx + 1] - pref[ry + 1][lx] + pref[ly][lx]
            if (sum == area && area > best) best = area
        }
        return best
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
