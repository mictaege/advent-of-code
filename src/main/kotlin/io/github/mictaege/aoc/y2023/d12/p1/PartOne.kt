package io.github.mictaege.aoc.y2023.d12.p1

import io.github.mictaege.aoc.y2023.d12.example

class Rule(val char: Int)

enum class Condition(val char: Char) {
    OPERATIONAL('.'),
    DAMAGED('#'),
    UNKNOWN('?');

    companion object {
        fun from(char: Char) = Condition.entries.toTypedArray().first { it.char == char }
    }

}
data class Spring(val condition: Condition)

class Record(val original: String) {
    val springs = original.split(" ")[0].toCharArray().map { Spring(Condition.from(it)) }
    val rules = original.split(" ")[1].split(",").map { Rule(it.toInt()) }
}

class Report(val original: String) {
    val records = original.lines().map { Record(it) }
}

fun main() {
    Report(example)
}