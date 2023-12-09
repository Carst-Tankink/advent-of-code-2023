package day9

import util.Solution

class MirageMaintenance(fileName: String?) : Solution<List<Long>, Long>(fileName) {
    override fun parse(line: String): List<Long> = line.split(" ").map { it.toLong() }

    override fun solve1(data: List<List<Long>>): Long = data.sumOf { predictNext(it) }

    private fun predictNext(history: List<Long>): Long {
        tailrec fun getDifferences(acc: List<List<Long>>, currentLine: List<Long>): List<List<Long>> {
            return if (currentLine.all { it == 0L }) acc + listOf(currentLine) else {
                val diffs = currentLine.zipWithNext { first, second -> second - first  }
                getDifferences(acc + listOf(currentLine), diffs)
            }
        }

        val differences: List<List<Long>> = getDifferences(emptyList(), history)
        return differences.sumOf { it.last() }
    }

    override fun solve2(data: List<List<Long>>): Long {
        TODO("Not yet implemented")
    }
}