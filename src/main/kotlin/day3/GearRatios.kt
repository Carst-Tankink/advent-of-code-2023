package day3

import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

class GearRatios(fileName: String?) : Solution<List<Char>, Int>(fileName) {
    override fun parse(line: String): List<Char> {
        return line.toCharArray().toList()
    }

    override fun solve1(data: List<List<Char>>): Int {
        val schematic = data.toGrid()
        val parts: Set<Point> = schematic.entries
            .filter {
                it.value != '.' && !it.value.isDigit()
            }.map { it.key }.toSet()

        val partDigits = parts
            .flatMap { it.getNeighbours(false) }
            .filter { schematic[it]?.isDigit() ?: false }

        val partNumbers = partDigits.map {
            expandToNumber(it, schematic)
        }.toSet()

        return partNumbers.sumOf { it.second }
    }

    private fun expandToNumber(p: Point, schematic: Map<Point, Char>): Pair<Set<Point>, Int> {
        tailrec fun expand(acc: Pair<Set<Point>, String>, left: Boolean): Pair<Set<Point>, String> {
            val point = if (left) acc.first.minBy { it.x } - Point(1, 0) else (acc.first.maxBy { it.x } + Point(1, 0))
            val atPosition = schematic[point]
            return if (atPosition == null || !atPosition.isDigit()) {
                acc
            } else {
                val newAcc = if (left) atPosition + acc.second else acc.second + atPosition
                expand(Pair(acc.first + point, newAcc), left)
            }
        }

        val start = "${schematic[p]!!}"
        val leftExpanded = expand(Pair(setOf(p), start), true)
        val rightExpanded = expand(leftExpanded, false)

        return Pair(rightExpanded.first, rightExpanded.second.toInt())
    }

    override fun solve2(data: List<List<Char>>): Int {
        val schematic = data.toGrid()
        return schematic.entries
            .filter {
                it.value == '*'
            }
            .map {
                it.key
                    .getNeighbours(false)
                    .filter { p -> schematic[p]?.isDigit() ?: false }
                    .map { p -> expandToNumber(p, schematic) }
                    .toSet()
            }
            .filter { it.size == 2 }
            .sumOf { it.first().second * it.last().second }
    }
}
