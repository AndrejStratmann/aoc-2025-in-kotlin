package day10

import println
import readInput
import java.util.BitSet
import java.util.PriorityQueue

fun main() {
    data class M(val pat: String, val btns: List<IntArray>, val tgt: IntArray)

    fun parse(input: List<String>): List<M> {
        val res = ArrayList<M>()
        for (s in input) if (s.isNotBlank()) {
            val lb = s.indexOf('[')
            val rb = s.indexOf(']')
            val pat = s.substring(lb + 1, rb)
            val btns = ArrayList<IntArray>()
            var i = 0
            while (true) {
                val p1 = s.indexOf('(', i)
                if (p1 == -1) break
                val p2 = s.indexOf(')', p1 + 1)
                val inside = s.substring(p1 + 1, p2).trim()
                if (inside.isNotEmpty() && inside[0] != '{') {
                    val arr = inside.split(',').map { it.trim().toInt() }.toIntArray()
                    btns.add(arr)
                }
                i = p2 + 1
            }
            val cb = s.indexOf('{')
            val ce = s.indexOf('}')
            val tgt = if (cb == -1) IntArray(0) else s.substring(cb + 1, ce).split(',')
                .map { it.trim().toInt() }.toIntArray()
            res.add(M(pat, btns, tgt))
        }
        return res
    }

    fun lightsCost(m: M): Int {
        val pat = m.pat
        val btns = m.btns
        val n = pat.length
        val k = btns.size
        if (n == 0 || k == 0) return 0
        val rows = Array(n) { BitSet(k) }
        for (j in 0 until k) for (idx in btns[j]) if (idx in 0 until n) rows[idx].set(j)
        val rhs = IntArray(n) { if (pat[it] == '#') 1 else 0 }
        val where = IntArray(k) { -1 }
        var row = 0
        for (col in 0 until k) {
            var sel = row
            while (sel < n && !rows[sel].get(col)) sel++
            if (sel == n) continue
            val tr = rows[row]; rows[row] = rows[sel]; rows[sel] = tr
            val vr = rhs[row]; rhs[row] = rhs[sel]; rhs[sel] = vr
            where[col] = row
            for (i in 0 until n) if (i != row && rows[i].get(col)) {
                rows[i].xor(rows[row])
                rhs[i] = rhs[i] xor rhs[row]
            }
            row++
        }
        val free = ArrayList<Int>()
        for (c in 0 until k) if (where[c] == -1) free.add(c)
        val f = free.size
        val maxMask = 1 shl f
        val x = IntArray(k)
        var best = Int.MAX_VALUE
        for (mask in 0 until maxMask) {
            java.util.Arrays.fill(x, 0)
            var w = 0
            for (t in 0 until f) if (((mask shr t) and 1) == 1) {
                val c = free[t]
                x[c] = 1
                w++
                if (w >= best) break
            }
            if (w >= best) continue
            for (c in 0 until k) {
                val r = where[c]
                if (r == -1) continue
                var v = rhs[r]
                var b = rows[r].nextSetBit(0)
                while (b >= 0) {
                    if (b != c && x[b] == 1) v = v xor 1
                    b = rows[r].nextSetBit(b + 1)
                }
                if (v == 1) {
                    x[c] = 1
                    w++
                    if (w >= best) break
                }
            }
            if (w < best) best = w
        }
        return if (best == Int.MAX_VALUE) 0 else best
    }

    fun joltsCost(m: M): Int {
        val btns = m.btns
        val tgt = m.tgt
        val d = tgt.size
        if (d == 0) return 0
        val base = (tgt.maxOrNull() ?: 0) + 1
        val pow = LongArray(d)
        pow[0] = 1
        for (i in 1 until d) pow[i] = pow[i - 1] * base
        fun enc(v: IntArray): Long {
            var code = 0L
            for (i in 0 until d) code += v[i] * pow[i]
            return code
        }
        val targetCode = enc(tgt)
        val dist = HashMap<Long, Int>()
        val pq = PriorityQueue<Pair<Int, Long>>(compareBy { it.first })
        dist[0L] = 0
        pq.add(0 to 0L)
        val cur = IntArray(d)
        while (pq.isNotEmpty()) {
            val (cost, code) = pq.poll()
            if (cost != dist[code]) continue
            if (code == targetCode) return cost
            var tmp = code
            for (i in 0 until d) {
                cur[i] = (tmp % base).toInt()
                tmp /= base
            }
            for (b in btns) {
                val nv = cur.clone()
                var ok = true
                for (idx in b) {
                    if (idx !in 0 until d) continue
                    nv[idx]++
                    if (nv[idx] > tgt[idx]) {
                        ok = false
                        break
                    }
                }
                if (!ok) continue
                val nc = enc(nv)
                val nd = cost + 1
                val old = dist[nc]
                if (old == null || nd < old) {
                    dist[nc] = nd
                    pq.add(nd to nc)
                }
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int { // O(n)
        var sum = 0
        for (m in parse(input)) sum += lightsCost(m)
        return sum
    }

    fun part2(input: List<String>): Int { // O(n)
        var sum = 0
        for (m in parse(input)) sum += joltsCost(m)
        return sum
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
