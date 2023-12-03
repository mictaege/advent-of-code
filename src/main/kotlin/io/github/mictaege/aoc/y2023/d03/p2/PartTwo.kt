package io.github.mictaege.aoc.y2023.d03.p2

import io.github.mictaege.aoc.y2023.d03.input

data class Entry(val x: Int, val y: Int, val value: Char)

class Part(schema: Schema, val entries: List<Entry>) {
    val number = entries.map { "${it.value}" }.reduce { a, b -> a + b }.toInt()

    private val minY = entries.minOf { it.y }
    private val maxY = entries.maxOf { it.y }
    private val minX = entries.minOf { it.x }
    private val maxX = entries.maxOf { it.x }
    val min = entries.first { it.x == minX && it.y == minY }
    val max = entries.last() { it.x == maxX && it.y == maxY }

    val neighbours = schema.neighbours(this)

    val adjacentToSymbol = neighbours.map { it.value }.any { it != '.' && !it.isDigit()  }
}

class Star(val entry: Entry, val adjacentTo: List<Part>) {
    val gear = adjacentTo.size == 2
    val ratio = adjacentTo.map { it.number }.reduce { acc, e -> acc * e}
}

class Schema(val original: String) {
    val entries: List<Entry>
    val parts: List<Part>
    val stars: List<Star>
    val gears: List<Star>
        get() = stars.filter { it.gear }

    init {
        val _entries = mutableListOf<Entry>()
        original.lines().forEachIndexed { y, line ->
            line.toCharArray().forEachIndexed { x, char ->
                _entries.add(Entry(x, y, char))
            }
        }
        entries = _entries

        val _parts = mutableListOf<Part>()
        original.lines().forEachIndexed { y, line ->
            val partEntries = mutableListOf<Entry>()
            val lineEntries = mutableListOf<Entry>()
            line.toCharArray().forEachIndexed { x, char ->
                lineEntries.add(Entry(x, y, char))
            }
            lineEntries.forEach {
                if (it.value.isDigit()) {
                    partEntries.add(it)
                } else {
                    if (partEntries.isNotEmpty()) {
                        _parts.add(Part(this, partEntries))
                    }
                    partEntries.clear()
                }
            }
            if (partEntries.isNotEmpty()) {
                _parts.add(Part(this, partEntries))
            }
        }
        parts = _parts

        val _stars = mutableListOf<Star>()
        entries.filter { it.value == '*' }.forEach { s ->
            _stars.add(Star(s, parts.filter { p -> s in p.neighbours }))
        }
        stars = _stars
    }

    fun neighbours(part: Part): List<Entry> {
        return entries.asSequence()
            .filter { it.y >= part.min.y - 1 }
            .filter { it.x >= part.min.x - 1 }
            .filter { it.y <= part.max.y + 1 }
            .filter { it.x <= part.max.x + 1 }
            .filter { it !in part.entries }
            .toList()
    }

}

fun main() {
    val result = Schema(input).gears.sumOf { it.ratio }
    println(result)
}