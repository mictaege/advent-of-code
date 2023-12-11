package io.github.mictaege.aoc.y2023.d10.p1

import io.github.mictaege.aoc.y2023.d10.example
import io.github.mictaege.aoc.y2023.d10.p1.Direction.*

enum class Direction {
    NORTH, EAST, SOUTH, WEST, NONE, UNKNOWN
}

enum class Tile(val char: Char, val a: Direction, val b : Direction) {
    NS_PIPE('|', NORTH, SOUTH),
    EW_PIPE('-', EAST, WEST),
    NE_PIPE('L', NORTH, EAST),
    NW_PIPE('J', NORTH, WEST),
    SW_PIPE('7', SOUTH, WEST),
    SE_PIPE('F', SOUTH, EAST),
    GROUND('.', NONE, NONE),
    START('S', UNKNOWN, UNKNOWN);

    companion object {
        fun from(char: Char) = entries.toTypedArray().first { it.char == char }
    }

    fun connectsTo(other: Tile): Boolean = when (this) {
        GROUND -> false
        START -> other != GROUND
        else -> this.a == other.a || this.b == other.b
    }

}

data class Pos(val y: Int, val x: Int)

class Cell(val pos: Pos, char: Char) {
    val tile = Tile.from(char)

    fun findNeighbour(table: Table): Cell {
        val neighbours = mutableListOf<Cell>()
        for (y in pos.y - 1..pos.y + 1) {
            for (x in pos.x - 1..pos.x + 1) {
                val neighbour = Pos(y, x)
                if (neighbour != pos) {
                    table.cells[neighbour]?.let { neighbours.add(it) }
                }
            }
        }
        val filter = neighbours.filter { this.tile.connectsTo(it.tile) }
        return filter.first()
    }

}

class Table(val original: String) {
    val cells: Map<Pos, Cell>
    val start: Cell
    val steps: List<Pos>

    init {
        cells = mutableMapOf()
        original.lines().forEachIndexed { y, l ->
            l.toCharArray().forEachIndexed { x, c ->
                val pos = Pos(y, x)
                cells.put(pos, Cell(pos, c))
            }
        }
        start = cells.values.first { it.tile == Tile.START }
        steps = mutableListOf()
        var cell = start.findNeighbour(this)
        while (cell.pos !in steps) {
            steps.add(cell.pos)
            cell = cell.findNeighbour(this)
        }
    }
}

fun main() {
    Table(example).steps.forEach { println(it) }
}