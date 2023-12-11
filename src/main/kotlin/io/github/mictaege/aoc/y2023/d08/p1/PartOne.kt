package io.github.mictaege.aoc.y2023.d08.p1

import io.github.mictaege.aoc.y2023.d08.input
import io.github.mictaege.aoc.y2023.d08.p1.Direction.L
import io.github.mictaege.aoc.y2023.d08.p1.Direction.R

enum class Direction {
    L, R
}

class Instruction(val original: String) {
    val directions: List<Direction>
    private var cursor = 0

    init {
        directions = original.toCharArray().map { Direction.valueOf(it.toString()) }
    }

    fun next(): Direction {
        if (cursor >= directions.size) {
            cursor = 0
        }
        return directions[cursor].also { cursor++ }
    }

}

class Node(val original: String) {
    val name: String
    val left: String
    val right: String

    init {
        name = original.split("=")[0].trim()
        left = original.split("=")[1].trim().split(",")[0].replace(Regex("[^A-Z]"), "")
        right = original.split("=")[1].trim().split(",")[1].replace(Regex("[^A-Z]"), "")
    }
}

class Network(val original: String) {
    val instruction: Instruction
    val nodes: Map<String, Node>
    init {
        instruction = Instruction(original.lines()[0])
        nodes = original.lines().drop(0).drop(2).map { Node(it) }.associateBy { it.name }
    }

    fun calculateSteps(): Long {
        var steps = 0L
        var node = nodes["AAA"]!!
        while (node.name != "ZZZ") {
            val direction = instruction.next()
            node = when (direction) {
                L -> nodes[node.left]!!
                R -> nodes[node.right]!!
            }
            steps++
        }
        return steps
    }

}

fun main() {
    val net = Network(input)
    println(net.calculateSteps()) //16531
}
