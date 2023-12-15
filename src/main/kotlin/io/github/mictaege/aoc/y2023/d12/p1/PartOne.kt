package io.github.mictaege.aoc.y2023.d12.p1

import io.github.mictaege.aoc.y2023.d12.example
import io.github.mictaege.aoc.y2023.d12.p1.Condition.UNKNOWN

class Possibilities(val springs: List<Spring>) {
    override fun toString(): String {
        return springs.joinToString { it.toString() }
    }
}

class Group(val length: Int)

class Rule(val original: String, val springs: List<Spring>) {
    val group = original.split(",").map { Group(it.toInt()) }
    val unknown = springs.filter { it.condition == UNKNOWN }.size
    val operational = unknown - group.size - 1
    val damaged = unknown - operational
    val possibilities = mutableListOf<Possibilities>()

    init {
        
    }

}

enum class Condition(val char: Char) {
    OPERATIONAL('.'),
    DAMAGED('#'),
    UNKNOWN('?');

    companion object {
        fun from(char: Char) = Condition.entries.toTypedArray().first { it.char == char }
    }

    override fun toString(): String {
        return "$char"
    }


}
data class Spring(val condition: Condition) {
    override fun toString(): String {
        return "$condition"
    }
}

class Record(val original: String) {
    val springs = original.split(" ")[0].toCharArray().map { Spring(Condition.from(it)) }
    val rule = Rule(original.split(" ")[1], springs)

}

class Report(val original: String) {
    val records = original.lines().map { Record(it) }
    val sum = records.sumOf { it.rule.possibilities.size }
}

fun main() {
    Report(example).records.forEach { println(it.rule) }
}