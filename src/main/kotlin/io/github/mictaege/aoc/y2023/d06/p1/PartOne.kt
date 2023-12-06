package io.github.mictaege.aoc.y2023.d06.p1

import io.github.mictaege.aoc.y2023.d06.example
import io.github.mictaege.aoc.y2023.d06.input

class Boat(val charged: Long, val race: Race) {
    val distance = charged * (race.time - charged)
    val wins = distance > race.record
}

class Race(val time: Long, val record: Long) {
    val winning = (1..time).map { Boat(it, this) }.filter { it.wins }
}

class RaceTable(val original: String) {
    val races: List<Race>
    val product: Long
    init {
        val lines = original.lines()
        val times = lines[0].split(":")[1].trim().split(Regex("\\s+")).map { it.trim().toLong() }
        val distance = lines[1].split(":")[1].trim().split(Regex("\\s+")).map { it.trim().toLong() }
        races = times.zip(distance).map { Race(it.first, it.second) }
        product = races.map { it.winning.size }.reduce { a, b -> a * b}.toLong()
    }

}

fun main() {
    val result = RaceTable(input).product
    println(result)
}