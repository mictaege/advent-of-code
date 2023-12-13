package io.github.mictaege.aoc.y2023.d11.p1

import io.github.mictaege.aoc.y2023.cartesianProduct
import io.github.mictaege.aoc.y2023.d11.input
import java.util.*

enum class Type(val char: Char) {
    SPACE('.'),
    GALAXY('#');

    companion object {
        fun from(char: Char) = Type.entries.toTypedArray().first { it.char == char }
    }

}

data class Pos(val y: Int, val x: Int) {
    fun up() = Pos(y - 1, x)
    fun down() = Pos(y + 1, x)
    fun left() = Pos(y, x - 1)
    fun right() = Pos(y, x + 1)

    fun neighbours() = listOf(up(), down(), left(), right())

}

class Sector(val pos: Pos, val char: Char, val number: Int) {
    val type = Type.from(char)

    override fun toString(): String {
        return if (type == Type.GALAXY) "$number" else "$char"
    }
}

class Route(val a: Sector, val b: Sector) {
    var distance = 0
    override fun toString(): String {
        return "Route(a=$a, b=$b, distance=$distance)"
    }

}

class Universe(val original: String) {
    val sectors: MutableMap<Pos, Sector> = mutableMapOf()
    val max_Y
        get() = sectors.keys.map { it.y }.max()
    val max_X
        get() = sectors.keys.map { it.x }.max()

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

        for (i in max_Y.downTo(0)) {
            if (isEmptyRow(i)) {
                addEmptyRow(i)
            }
        }
        for (i in max_X.downTo(0)) {
            if (isEmptyColumn(i)) {
                addEmptyColumn(i)
            }
        }

        routes = sectors.values.filter { it.type == Type.GALAXY }.cartesianProduct().map { Route(it.first, it.second) }

        routes.forEach { calculateDistance(it) }

        overallDistance = routes.sumOf { it.distance }
    }

    private fun isEmptyRow(i: Int): Boolean =
        !sectors.filter { it.value.pos.y == i }.filter { it.value.type == Type.GALAXY }.any()

    private fun addEmptyRow(i: Int) {
        for (y in max_Y.downTo(i)) {
            for (x in 0..max_X) {
                val pos = Pos(y, x)
                val target = Pos(y, x).down()
                val sector = sectors[pos]!!
                sectors.remove(pos)
                sectors[target] = Sector(target, sector.char, sector.number)

            }
        }
        for (x in 0..max_X) {
            val pos = Pos(i, x)
            sectors[pos] = Sector(pos, Type.SPACE.char, 0)
        }
    }

    private fun isEmptyColumn(i: Int): Boolean =
        !sectors.filter { it.value.pos.x == i }.filter { it.value.type == Type.GALAXY }.any()

    private fun addEmptyColumn(i: Int) {
        for (x in max_X.downTo(i)) {
            for (y in 0..max_Y) {
                val pos = Pos(y, x)
                val target = Pos(y, x).right()
                val sector = sectors[pos]!!
                sectors.remove(pos)
                sectors[target] = Sector(target, sector.char, sector.number)

            }
        }
        for (y in 0..max_Y) {
            val pos = Pos(y, i)
            sectors[pos] = Sector(pos, Type.SPACE.char, 0)
        }
    }

    fun contains(pos: Pos) = sectors.keys.contains(pos)

    /* @see https://de.wikipedia.org/wiki/Breitensuche */
    private fun calculateDistance(route: Route) {
        val distances = mutableMapOf<Pos, Int>()
        distances[route.a.pos] = 0

        val queue = LinkedList<Pos>()
        queue.add(route.a.pos)

        while (queue.isNotEmpty()) {
            val current = queue.remove()
            val currentDist = distances[current]!!
            current.neighbours()
                .filter { contains(it) }
                .filter { it -> !distances.contains(it) }
                .forEach {
                    queue.add(it)
                    distances[it] = currentDist + 1
                }
        }

        route.distance = distances[route.b.pos]!!
    }

    fun print() {
        for (y in 0..max_Y) {
            for (x in 0..max_X) {
                val pos = Pos(y, x)
                val sector = sectors[pos]
                print(sector)
            }
            println("")
        }
    }

}


fun main() {
    //calculation is correct but pretty slow and takes several minutes for the real input
    val result = Universe(input).overallDistance
    println(result)
}