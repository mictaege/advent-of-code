package io.github.mictaege.aoc.y2023.d01.p2

import io.github.mictaege.aoc.y2023.d01.input

class Line(private val original: String) {
    private val mapping = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to  "9"
    )
    private val tokens: List<String>
        get() {
            val all = mutableListOf<String>()
            all.addAll(mapping.keys)
            all.addAll(mapping.values)
            return all
        }
    private val firstMatch = original.findAnyOf(tokens) ?: Pair(0, "")
    private val lastMatch = original.findLastAnyOf(tokens) ?: Pair(0, "")
    private val firstNumber: String
        get() {
            return if (firstMatch.second.length == 1) {
                firstMatch.second
            } else {
                mapping[firstMatch.second] ?: ""
            }
        }
    private val lastNumber: String
        get() {
            return if (lastMatch.second.length == 1) {
                lastMatch.second
            } else {
                mapping[lastMatch.second] ?: ""
            }
        }
    val number = "$firstNumber$lastNumber".toInt()
}

fun main() {
    val result = input.lines().map { l -> Line(l) }.sumOf { l -> l.number }
    println(result)
}