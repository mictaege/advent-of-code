package io.github.mictaege.aoc.y2023.d11.p1

import io.github.mictaege.aoc.y2023.cartesianProduct
import io.github.mictaege.aoc.y2023.d11.example
import io.github.mictaege.aoc.y2023.d11.input
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

enum class Type(val char: Char) {
    SPACE('.'),
    GALAXY('#');

    companion object {
        fun from(char: Char) = Type.entries.toTypedArray().first { it.char == char }
    }

}

data class Pos(val y: Int, val x: Int)

class Sector(val pos: Pos, val char: Char, val number: Int) {
    val type = Type.from(char)
}

class Route(val a: Sector, val b: Sector) {
    val max_Y = max(a.pos.y, b.pos.y)
    val min_Y = min(a.pos.y, b.pos.y)
    val max_X = max(a.pos.x, b.pos.x)
    val min_X = min(a.pos.x, b.pos.x)
}

class Universe(val original: String) {
    val sectors: MutableMap<Pos, Sector> = mutableMapOf()
    val max_Y
        get() = sectors.keys.map { it.y }.max()
    val max_X
        get() = sectors.keys.map { it.x }.max()
    val emptyColumns: Set<Int>
    val emptyRows: Set<Int>
    val routes: List<Route>
    val overallDistance: Int

    init {
        val lines = original.lines()
        var galaxies = 0
        lines.forEachIndexed { y, l ->
            val chars = l.toCharArray()
            chars.forEachIndexed { x, c ->
                val pos = Pos(y, x)
                if (c == Type.GALAXY.char) {
                    galaxies += 1
                    val sector = Sector(pos, c, galaxies)
                    sectors[pos] = sector
                } else {
                    val sector = Sector(pos, c, 0)
                    sectors[pos] = sector
                }
            }
        }

        emptyRows = mutableSetOf()
        for (i in 0..max_Y) {
            if (isEmptyRow(i)) {
                emptyRows.add(i)
            }
        }
        emptyColumns = mutableSetOf()
        for (i in 0..max_X) {
            if (isEmptyColumn(i)) {
                emptyColumns.add(i)
            }
        }

        routes = sectors.values.filter { it.type == Type.GALAXY }.cartesianProduct().map { Route(it.first, it.second) }

        overallDistance = routes.sumOf { distance(it) }
    }

    private fun distance(route: Route): Int {
        val emptyColumnsBetween = (route.min_X + 1..<route.max_X).toSet().intersect(emptyColumns).count()
        val emptyRowsBetween = (route.min_Y + 1..<route.max_Y).toSet().intersect(emptyRows).count()
        val x_1 = route.min_X
        val x_2 = route.max_X + emptyColumnsBetween
        val y_1 = route.min_Y
        val y_2 = route.max_Y + emptyRowsBetween
        return abs(x_1 - x_2) + abs(y_1 - y_2)
    }

    private fun isEmptyRow(i: Int): Boolean =
        !sectors.filter { it.value.pos.y == i }.filter { it.value.type == Type.GALAXY }.any()

    private fun isEmptyColumn(i: Int): Boolean =
        !sectors.filter { it.value.pos.x == i }.filter { it.value.type == Type.GALAXY }.any()

}

fun main() {
    val result = Universe(input).overallDistance
    println(result)
}