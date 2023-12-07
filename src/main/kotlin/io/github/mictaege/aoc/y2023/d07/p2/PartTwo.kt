package io.github.mictaege.aoc.y2023.d07.p2

import io.github.mictaege.aoc.y2023.d07.input
import io.github.mictaege.aoc.y2023.d07.p2.Card.C_J
import io.github.mictaege.aoc.y2023.d07.p2.HandType.*

enum class Card {
    C_A, C_K, C_Q, C_T, C_9, C_8, C_7, C_6, C_5, C_4, C_3, C_2, C_J;
    override fun toString() = name.substring(2)
}

enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

class Hand(original: String) : Comparable<Hand> {
    val cards = original.split(Regex("\\s+"))[0].toCharArray().map { Card.valueOf("C_$it") }
    val bid = original.split(Regex("\\s+"))[1].trim().toLong()
    val type: HandType

    init {
        var candidate = findType(cards)
        if (cards.contains(C_J)) {
            Card.entries.toTypedArray().filter { it != C_J }.forEach {c ->
                val copy = cards.map { if (it == C_J) c else it }
                val actual = findType(copy)
                if (actual < candidate) {
                    candidate = actual
                }
            }
        }
        type = candidate
    }

    private fun findType(cards: List<Card>): HandType {
        val groups: Map<Card, List<Card>> = cards.groupBy { it }
        val max: Int = groups.values.maxOf { it.size }
        return when (max) {
            5 -> FIVE_OF_A_KIND
            4 -> FOUR_OF_A_KIND
            3 -> if (groups.size == 2) FULL_HOUSE else THREE_OF_A_KIND
            2 -> if (groups.size == 3) TWO_PAIR else ONE_PAIR
            else -> HIGH_CARD
        }
    }

    override fun compareTo(other: Hand): Int {
        return Comparator.comparing(Hand::type)
            .thenComparing { o1, o2 ->
                val diff = o1.cards.zip(o2.cards).find { it.first != it.second }
                diff?.first?.compareTo(diff.second) ?: 0
            }
            .compare(this, other)
    }

}

class Game(val original: String) {
    val hands = original.lines().map { Hand(it) }.sorted()

    fun totalWinning(): Long {
        return hands.reversed()
            .mapIndexed { i, h -> Pair(i + 1, h) }
            .sumOf { it.first * it.second.bid }
    }

}

fun main() {
    val result = Game(input).totalWinning()
    println(result)
}