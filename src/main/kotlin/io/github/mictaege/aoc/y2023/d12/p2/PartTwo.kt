package io.github.mictaege.aoc.y2023.d12.p2

import io.github.mictaege.aoc.y2023.d12.example
import io.github.mictaege.aoc.y2023.d12.p2.Condition.*
import io.github.mictaege.aoc.y2023.splitBy
import java.time.Duration
import java.time.LocalDateTime

data class Possibilities(val springs: List<Spring>) {
    fun isValid(rule: Rule): Boolean {
        val groups = springs.splitBy(Spring(OPERATIONAL)).map { Group(it.size) }
        return groups == rule.groups
    }

}

data class Group(val length: Int)

class Rule(val original: String, val springs: List<Spring>) {
    val groups = original.split(",").map { Group(it.toInt()) }
    val valid = mutableListOf<Possibilities>()
    val invalid = mutableListOf<Possibilities>()

    init {
        combinations(springs, 0, mutableListOf(), valid, invalid)
    }

    fun combinations(
        input: List<Spring>,
        index: Int,
        output: MutableList<Spring>,
        valid: MutableList<Possibilities>,
        invalid: MutableList<Possibilities>
    ) {
        if (index == input.size) {
            val possibility = Possibilities(output.toList())
            if (possibility.isValid(this)) {
                valid.add(possibility)
            } else {
                invalid.add(possibility)
            }
            return
        } else if (output.count { it == Spring(OPERATIONAL)} > groups.size) {
            return
        }

        if (input[index].condition == UNKNOWN) {
            output.add(Spring(OPERATIONAL))
            combinations(input, index + 1, output, valid, invalid)
            output.removeAt(output.size - 1)

            output.add(Spring(DAMAGED))
            combinations(input, index + 1, output, valid, invalid)
            output.removeAt(output.size - 1)
        } else {
            output.add(input[index])
            combinations(input, index + 1, output, valid, invalid)
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
    val springs = unfoldSprings(original.split(" ")[0]).toCharArray().map { Spring(Condition.from(it)) }
    val rule = Rule(unfoldRule(original.split(" ")[1]), springs)

    private fun unfoldSprings(input: String): String = "${input}?".repeat(5).dropLast(1)
    private fun unfoldRule(input: String): String = "${input},".repeat(5).dropLast(1)

}

class Report(val original: String) {
    val records = original.lines().map { Record(it) }
    val sum = records.sumOf { it.rule.valid.size }
}

fun main() {
    val start = LocalDateTime.now()
    val result = Report(example).sum
    println(result)
    println(Duration.between(start, LocalDateTime.now()).toSeconds())
}