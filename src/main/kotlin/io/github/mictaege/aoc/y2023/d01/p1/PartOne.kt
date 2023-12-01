package io.github.mictaege.aoc.y2023.d01.p1

import io.github.mictaege.aoc.y2023.d01.input

class Line(private val original: String) {
    private val firstNumber: Char = original.toCharArray().first { c -> c.isDigit() }
    private val lastNumber: Char = original.toCharArray().last { c -> c.isDigit() }
    val number = "$firstNumber$lastNumber".toInt()
}

fun main() {
    val result = input.lines().map { l -> Line(l) }.sumOf { l -> l.number }
    println(result)
}