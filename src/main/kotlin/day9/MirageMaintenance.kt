package day9

import util.Solution

class MirageMaintenance(fileName: String?) : Solution<List<Long>, Long>(fileName) {
    override fun parse(line: String): List<Long> = line.split(" ").map { it.toLong() }

    override fun solve1(data: List<List<Long>>): Long = data.sumOf { predictNext(it) }

    private tailrec fun getDifferences(acc: List<List<Long>>, currentLine: List<Long>): List<List<Long>> {
        return if (currentLine.all { it == 0L }) acc + listOf(currentLine) else {
            val diffs = currentLine.zipWithNext { first, second -> second - first }
            getDifferences(acc + listOf(currentLine), diffs)
        }
    }

    private fun predictNext(history: List<Long>): Long {
        return getDifferences(emptyList(), history).sumOf { it.last() }
    }

    private fun predictPrevious(history: List<Long>): Long {
        val differences = getDifferences(emptyList(), history).map { it.first() }
        return differences.foldRight(0L) { v, acc -> v - acc }
    }

    override fun solve2(data: List<List<Long>>): Long = data.sumOf { predictPrevious(it) }
}