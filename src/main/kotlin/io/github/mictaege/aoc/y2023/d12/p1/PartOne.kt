package io.github.mictaege.aoc.y2023.d12.p1

import io.github.mictaege.aoc.y2023.d12.input
import io.github.mictaege.aoc.y2023.d12.p1.Condition.*
import io.github.mictaege.aoc.y2023.splitBy
import java.time.Duration
import java.time.LocalDateTime

data class Possibilities(val springs: List<Spring>) {

    fun isValid(rule: Rule): Boolean {
        val groups = springs.splitBy(Spring(OPERATIONAL)).map { Group(it.size) }
        return groups == rule.group
    }

}

data class Group(val length: Int)

class Rule(val original: String, val springs: List<Spring>) {
    val group = original.split(",").map { Group(it.toInt()) }
    val possibilities = mutableListOf<Possibilities>()
    val valid: List<Possibilities>

    init {
        combinations(springs, 0, mutableListOf(), possibilities)
        valid = possibilities.filter { it.isValid(this) }
    }

    fun combinations(input: List<Spring>, index: Int, output: MutableList<Spring>, results: MutableList<Possibilities>) {
        if (index == input.size) {
            results.add(Possibilities(output.toList()))
            return
        }

        if (input[index].condition == UNKNOWN) {
            output.add(Spring(OPERATIONAL))
            combinations(input, index + 1, output, results)
            output.removeAt(output.size - 1)

            output.add(Spring(DAMAGED))
            combinations(input, index + 1, output, results)
            output.removeAt(output.size - 1)
        } else {
            output.add(input[index])
            combinations(input, index + 1, output, results)
            output.removeAt(output.size - 1)
        }
    }

}

enum class Condition(val char: Char) {
    OPERATIONAL('.'),
    DAMAGED('#'),
    UNKNOWN('?');

    companion object {
        fun from(char: Char) = Condition.entries.toTypedArray().first { it.char == char }
    }

    override fun toString(): String = "$char"


}
data class Spring(val condition: Condition) {
    override fun toString(): String = "$condition"
}

class Record(val original: String) {
    val springs = original.split(" ")[0].toCharArray().map { Spring(Condition.from(it)) }
    val rule = Rule(original.split(" ")[1], springs)

}

class Report(val original: String) {
    val records = original.lines().map { Record(it) }
    val sum = records.sumOf { it.rule.valid.size }
}

fun main() {
    val start = LocalDateTime.now()
    val result = Report(input).sum
    println(result)
    println(Duration.between(start, LocalDateTime.now()).toSeconds())
}