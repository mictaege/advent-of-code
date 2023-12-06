package io.github.mictaege.aoc.y2023.d06.p2

import io.github.mictaege.aoc.y2023.d06.input

class Boat(val charged: Long, val race: Race) {
    val distance = charged * (race.time - charged)
    val wins = distance > race.record
}

class Race(val time: Long, val record: Long) {
    val winning = (1..time).map { Boat(it, this) }.filter { it.wins }
}

class RaceTable(val original: String) {
    val race: Race
    init {
        val lines = original.lines()
        val time: Long = lines[0].split(":")[1].trim().replace(Regex("\\s+"), "").toLong()
        val distance: Long = lines[1].split(":")[1].trim().replace(Regex("\\s+"), "").toLong()
        race = Race(time, distance)
    }

}

fun main() {
    val result = RaceTable(input).race.winning.size
    println(result)
}