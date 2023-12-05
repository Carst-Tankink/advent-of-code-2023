package day4

import util.Helpers
import util.Solution

data class Scratchcard(
    val winningNumbers: List<Int>,
    val foundNumbers: List<Int>,
)

class Scratchcards(fileName: String?) : Solution<Scratchcard, Int>(fileName) {
    override fun parse(line: String): Scratchcard {
        val parts = line.split(": ", " | ")
        val winningNumbers = parts[1].trim().split("\\s+".toRegex()).map { it.toInt() }
        val foundNumbers = parts[2].trim().split("\\s+".toRegex()).map { it.toInt() }

        return Scratchcard(winningNumbers, foundNumbers)
    }

    override fun solve1(data: List<Scratchcard>): Int {
        return data
            .map { score(it) }
            .sumOf { scoringNumbers -> if (scoringNumbers > 0) Helpers.pow(2, scoringNumbers - 1) else 0 }
    }

    private fun score(card: Scratchcard): Int {
        return (card.foundNumbers intersect card.winningNumbers.toSet()).size
    }

    override fun solve2(data: List<Scratchcard>): Int {
        return data.foldIndexed(List(data.size) { 1 }) { index, acc, card ->
            val score = score(card)
            val copies = acc[index]
            val wonCards = index + 1..index + score
            acc.mapIndexed { idx, v ->
                v + if (idx in wonCards) copies else 0
            }
        }.sum()
    }
}
