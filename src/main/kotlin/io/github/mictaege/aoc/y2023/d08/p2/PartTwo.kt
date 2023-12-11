package io.github.mictaege.aoc.y2023.d08.p2

import io.github.mictaege.aoc.y2023.d08.example_3
import io.github.mictaege.aoc.y2023.d08.input
import java.time.LocalDateTime

enum class Direction(val transform: (Node) -> String) {
    L({ it.left}), R({ it.right})
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
    val endsWithA: Boolean
    val endsWithZ: Boolean
    val left: String
    val right: String

    init {
        name = original.split("=")[0].trim()
        endsWithA = name.endsWith("A")
        endsWithZ = name.endsWith("Z")
        left = original.split("=")[1].trim().split(",")[0].replace(Regex("[^A-Z0-9]"), "")
        right = original.split("=")[1].trim().split(",")[1].replace(Regex("[^A-Z0-9]"), "")
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
        var currentNodes = nodes.filterValues { it.endsWithA }
        while (!currentNodes.values.all { it.endsWithZ }) {
            val direction = instruction.next()
            currentNodes = transform(currentNodes, direction)
            steps++
            if (steps % 1000000L == 0L) {
                println(steps)
            }
        }
        return steps
    }
    private fun transform(currentNodes: Map<String, Node>, direction: Direction): Map<String, Node> {
        val keys = currentNodes.values.map(direction.transform)
        return nodes.filter { n -> n.key in keys }
    }

}

fun main() {
    //24.035.773.251.517
    // Correct for the example, but far to slow to solve
    // the real input in a meaningful time
    val net = Network(example_3)
    println(net.calculateSteps())
}
