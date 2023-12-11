package io.github.mictaege.aoc.y2023.d09.p2

import io.github.mictaege.aoc.y2023.d09.input

class Entry(val entries: List<Long>) {
    val first = entries.first()
    var extrapolate = first
}

class Sequence(original: String) {
    val numbers = original.trim().split(Regex("\\s+")).map { it.trim().toLong() }
    val history: List<Entry>
    val extrapolate: Long
        get() = history.first().extrapolate

    init {
        history = mutableListOf()
        var entry = numbers
        while (entry.any { it != 0L }) {
            history.add(Entry(entry))
            entry = entry.zipWithNext { a, b -> b - a }
        }
        history.add(Entry(entry))
        val reversed = history.reversed()
        reversed.forEachIndexed { i, e ->
            if (i > 0) {
                val previous = reversed[i - 1].extrapolate
                e.extrapolate = e.first - previous
            }
        }
    }
}

class Report(val original: String) {
    val sequences = original.lines().map { Sequence(it) }
    val sum = sequences.sumOf { it.extrapolate }
}

fun main() {
    val result = Report(input).sum
    println(result)
}
