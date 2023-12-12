package io.github.mictaege.aoc.y2023.d10.p2

import io.github.mictaege.aoc.y2023.d10.example_3
import io.github.mictaege.aoc.y2023.d10.input
import io.github.mictaege.aoc.y2023.d10.p2.Direction.*

enum class Direction {
    NORTH, EAST, SOUTH, WEST, NONE, UNKNOWN
}

enum class Tile(val char: Char, val a: Direction, val b : Direction) {
    NS_PIPE('|', NORTH, SOUTH),
    EW_PIPE('-', EAST, WEST),
    NE_BEND('L', NORTH, EAST),
    NW_BEND('J', NORTH, WEST),
    SW_BEND('7', SOUTH, WEST),
    SE_BEND('F', SOUTH, EAST),
    GROUND('.', NONE, NONE),
    START('S', UNKNOWN, UNKNOWN);

    companion object {
        fun from(char: Char) = entries.toTypedArray().first { it.char == char }
    }

    fun hasDirection(direction: Direction) = when(this) {
        GROUND -> false
        START -> true
        else -> direction in listOf(a, b )
    }

}

data class Pos(val y: Int, val x: Int)

class Cell(val pos: Pos, val char: Char) {
    val tile = Tile.from(char)

    fun findNeighbour(table: Table): Cell? {
        val up: Cell? = table.cells[Pos(pos.y - 1, pos.x)]
        val left: Cell? = table.cells[Pos(pos.y, pos.x - 1)]
        val down: Cell? = table.cells[Pos(pos.y + 1, pos.x)]
        val right: Cell? = table.cells[Pos(pos.y, pos.x + 1)]

        if (up != null && up !in table.steps && tile.hasDirection(NORTH) && up.tile.hasDirection(SOUTH)) {
            return up
        } else if (left != null && left !in table.steps && tile.hasDirection(WEST) && left.tile.hasDirection(EAST)) {
            return left
        } else if (down != null && down !in table.steps && tile.hasDirection(SOUTH) && down.tile.hasDirection(NORTH)) {
            return down
        }  else if (right != null && right !in table.steps && tile.hasDirection(EAST) && right.tile.hasDirection(WEST)) {
            return right
        } else {
            return null
        }
    }

    fun isInterior(table: Table): Boolean {
        if (tile != Tile.GROUND) {
            return false
        } else {
            val x_Axis = (0..table.max_X).toList().map { Pos(pos.y, it) }.filterNot { it == pos }
            val intersections = x_Axis.filter { it -> it in table.steps.map { c -> c.pos } }.size
            return intersections == 0 || intersections % 2 != 0
        }
    }

}

class Table(val original: String) {
    val cells: Map<Pos, Cell>
    val max_Y: Int
    val max_X: Int
    val start: Cell
    val steps: List<Cell>
    val interiorCells: List<Cell>

    init {
        cells = mutableMapOf()
        val lines = original.lines()
        lines.forEachIndexed { y, l ->
            val chars = l.toCharArray()
            chars.forEachIndexed { x, c ->
                val pos = Pos(y, x)
                cells.put(pos, Cell(pos, c))
            }
        }
        max_Y = cells.keys.map { it.y }.max()
        max_X = cells.keys.map { it.x }.max()
        start = cells.values.first { it.tile == Tile.START }
        steps = mutableListOf()
        steps.add(start)
        var cell = start.findNeighbour(this)
        while (cell != null) {
            steps.add(cell)
            cell = cell.findNeighbour(this)
        }
        interiorCells = cells.values.filter { it.isInterior(this) }

    }
}

fun main() {
    val result = Table(example_3).interiorCells.size
    println(result)
}