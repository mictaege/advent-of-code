package io.github.mictaege.aoc.y2023.d04.p2

import io.github.mictaege.aoc.y2023.d04.input

class Scratchcard(val original: String) {
    val index: Int = original.split(":")[0]
        .replace("Card", "").trim()
        .toInt()
    val winningNumbers: List<Int> = original.split(":")[1]
            .split("|")[0].trim()
            .split(Regex("\\s+")).map { it.toInt() }
    val havingNumbers: List<Int> = original.split(":")[1]
            .split("|")[1].trim()
            .split(Regex("\\s+")).map { it.toInt() }
    val matchingNumbers: List<Int> = havingNumbers.filter { it in winningNumbers }

}

class Table(val original: String) {
    val cards: List<Scratchcard> = original.lines().map { Scratchcard(it) }
}

class WinningStack(val table: Table) {
    val won = mutableListOf<Scratchcard>()

    init {
        table.cards.forEach { addWin(it) }
    }

    private fun addWin(card: Scratchcard) {
        won.add(card)
        for (i in card.index..<card.index + card.matchingNumbers.size) {
            if (i < table.cards.size) {
                addWin(table.cards[i])
            }
        }
    }

}


fun main() {
    val result = WinningStack(Table(input)).won.size
    println(result)
}