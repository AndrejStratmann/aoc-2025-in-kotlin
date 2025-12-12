package day12

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int { // O(exp)
        if (input.isEmpty()) return 0
        var i = 0
        val shapesRaw = ArrayList<List<String>>()
        while (i < input.size && !input[i].contains('x')) {
            val line = input[i]
            if (line.isNotBlank() && line.endsWith(":")) {
                i++
                val rows = ArrayList<String>()
                while (i < input.size && input[i].isNotBlank() && !input[i].endsWith(":")) {
                    rows.add(input[i])
                    i++
                }
                shapesRaw.add(rows)
            } else i++
        }
        val regionsW = ArrayList<Int>()
        val regionsH = ArrayList<Int>()
        val regionsC = ArrayList<IntArray>()
        while (i < input.size) {
            val line = input[i].trim()
            i++
            if (line.isEmpty()) continue
            val parts = line.split(":")
            val dims = parts[0].trim().split("x")
            val w = dims[0].toInt()
            val h = dims[1].toInt()
            val cntStr = parts[1].trim().split(" ").filter { it.isNotEmpty() }
            val cnt = IntArray(shapesRaw.size)
            for (k in cnt.indices) if (k < cntStr.size) cnt[k] = cntStr[k].toInt()
            regionsW.add(w)
            regionsH.add(h)
            regionsC.add(cnt)
        }

        fun norm(ps: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
            var mx = Int.MAX_VALUE
            var my = Int.MAX_VALUE
            for ((x, y) in ps) {
                if (x < mx) mx = x
                if (y < my) my = y
            }
            return ps.map { (x, y) -> Pair(x - mx, y - my) }
                .sortedWith(compareBy<Pair<Int, Int>> { it.second }.thenBy { it.first })
        }

        fun key(ps: List<Pair<Int, Int>>): String {
            val sb = StringBuilder()
            for ((x, y) in ps) {
                sb.append(x).append(',').append(y).append(';')
            }
            return sb.toString()
        }

        fun gen(pts: List<Pair<Int, Int>>): List<List<Pair<Int, Int>>> {
            val res = ArrayList<List<Pair<Int, Int>>>()
            val seen = HashSet<String>()
            var cur = pts
            repeat(4) {
                val n1 = norm(cur)
                val k1 = key(n1)
                if (seen.add(k1)) res.add(n1)
                val mir = cur.map { (x, y) -> Pair(-x, y) }
                val n2 = norm(mir)
                val k2 = key(n2)
                if (seen.add(k2)) res.add(n2)
                cur = cur.map { (x, y) -> Pair(-y, x) }
            }
            return res
        }

        val S = shapesRaw.size
        val orients = ArrayList<List<List<Pair<Int, Int>>>>()
        val areas = IntArray(S)
        for (idx in 0 until S) {
            val rows = shapesRaw[idx]
            val pts = ArrayList<Pair<Int, Int>>()
            for (y in rows.indices) {
                val row = rows[y]
                for (x in row.indices) if (row[x] == '#') pts.add(Pair(x, y))
            }
            areas[idx] = pts.size
            orients.add(gen(pts))
        }

        fun fit(w: Int, h: Int, cntIn: IntArray): Boolean {
            val cnt = cntIn.clone()
            var total = 0
            for (i in 0 until S) total += cnt[i] * areas[i]
            if (total > w * h) return false
            val used = BooleanArray(w * h)
            val placements = Array(S) { ArrayList<IntArray>() }
            for (s in 0 until S) {
                for (o in orients[s]) {
                    var maxX = 0
                    var maxY = 0
                    for ((x, y) in o) {
                        if (x > maxX) maxX = x
                        if (y > maxY) maxY = y
                    }
                    if (maxX >= w || maxY >= h) continue
                    for (y0 in 0..h - 1 - maxY) {
                        for (x0 in 0..w - 1 - maxX) {
                            val cells = IntArray(o.size)
                            var t = 0
                            for ((dx, dy) in o) {
                                cells[t++] = (y0 + dy) * w + (x0 + dx)
                            }
                            placements[s].add(cells)
                        }
                    }
                }
            }
            fun dfs(si: Int): Boolean {
                var k = si
                while (k < S && cnt[k] == 0) k++
                if (k == S) return true
                val ps = placements[k]
                if (ps.isEmpty()) return false
                for (cells in ps) {
                    var ok = true
                    for (p in cells) if (used[p]) {
                        ok = false
                        break
                    }
                    if (!ok) continue
                    for (p in cells) used[p] = true
                    cnt[k]--
                    if (dfs(k)) return true
                    cnt[k]++
                    for (p in cells) used[p] = false
                }
                return false
            }
            return dfs(0)
        }

        var ans = 0
        for (r in regionsW.indices) {
            if (fit(regionsW[r], regionsH[r], regionsC[r])) ans++
        }
        return ans
    }

    fun part2(input: List<String>): Int { // O(1)
        return 0
    }

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
