package io.github.mictaege.aoc.y2023.d02.p1

import io.github.mictaege.aoc.y2023.d02.input

enum class Color(val max: Int) {
    RED(12), GREEN(13), BLUE(14)
}

class Cube(val original: String) {
    val amount = original.replace(Regex("\\D"), "").trim().toInt()
    val color = Color.valueOf(original.replace(Regex("\\d"),"").trim().uppercase())
    val possible = amount <= color.max
}

class Sample(val original: String) {
    val cubes = original.split(',').map { Cube(it.trim()) }
    val red = cubes.firstOrNull { it.color == Color.RED }
    val green = cubes.firstOrNull { it.color == Color.GREEN }
    val blue = cubes.firstOrNull { it.color == Color.BLUE }
    val possible = cubes.all { it.possible }
}

class Game(val original: String) {
    val id = original.split(':')[0].replace("Game ", "").trim().toInt()
    val samples = original.split(':')[1].split(';').map { s -> Sample(s.trim()) }
    val possible = samples.all { it.possible }
}

fun main() {
    val result = input.lines().map { l -> Game(l) }.filter { it.possible }.sumOf { it.id }
    println(result)
}