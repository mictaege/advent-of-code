package io.github.mictaege.aoc.y2023.d10.p2

import io.github.mictaege.aoc.y2023.d10.example_3
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

    fun reachesBorder(table: Table): Boolean {
        // test none border cells that are able to reach the border
        return table.isBorder(this)
    }

}

class Table(val original: String) {
    val cells: Map<Pos, Cell>
    val size_Y: Int
    val size_X: Int
    val caught: Int

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
        size_Y = cells.values.map { it.pos.y }.max()
        size_X = cells.values.map { it.pos.x }.max()

        caught = cells.values.filter { it.tile == Tile.GROUND }.filter { !it.reachesBorder(this) }.size

    }

    fun isBorder(cell: Cell) = cell.pos.y == 0 || cell.pos.y == size_Y || cell.pos.x == 0 || cell.pos.x == size_X

}

fun main() {
    // currently 17 (not 4) 
    val result = Table(example_3).caught
    println(result)
}