package day4

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
        return data.sumOf {
            score(it)
        }
    }

    private fun score(card: Scratchcard): Int {
        return card.foundNumbers.fold(1) { acc, number ->
            if (number in card.winningNumbers) acc * 2 else acc
        } / 2
    }

    override fun solve2(data: List<Scratchcard>): Int {
        return data.foldIndexed(List(data.size) { 1 }) { index, acc, card ->
            val score = (card.foundNumbers intersect card.winningNumbers.toSet()).size
            val copies = acc[index]
            val wonCards = index + 1..index + score
            acc.mapIndexed { idx, v ->
                v + if (idx in wonCards) copies else 0
            }
        }.sum()
    }
}
