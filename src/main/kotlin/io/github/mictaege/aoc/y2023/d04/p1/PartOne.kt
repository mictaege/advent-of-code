package io.github.mictaege.aoc.y2023.d04.p1

import io.github.mictaege.aoc.y2023.d04.input
import kotlin.math.pow

class Scratchcard(val original: String) {
    val winningNumbers: List<Int> = original.split(":")[1]
            .split("|")[0].trim()
            .split(Regex("\\s+")).map { it.toInt() }
    val havingNumbers: List<Int> = original.split(":")[1]
            .split("|")[1].trim()
            .split(Regex("\\s+")).map { it.toInt() }
    val matchingNumbers: List<Int> = havingNumbers.filter { it in winningNumbers }
    val points: Int
        get() = if (matchingNumbers.isEmpty()) {
            0
        } else {
            2.0.pow(matchingNumbers.size - 1).toInt()
        }
}

class Table(val original: String) {
    val cards: List<Scratchcard> = original.lines().map { Scratchcard(it) }
    val points: Int = cards.sumOf { it.points }
}

fun main() {
    val result = Table(input).points
    println(result)
}