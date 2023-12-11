package day11

import util.Helpers.Companion.toGrid
import util.Helpers.Companion.transpose
import util.Point
import util.Solution
import kotlin.math.abs

enum class Space {
    EMPTY, GALAXY
}

class CosmicExpansion(fileName: String?) : Solution<List<Space>, Long>(fileName) {
    override fun parse(line: String): List<Space>? {
        return line.map {
            when (it) {
                '.' -> Space.EMPTY
                '#' -> Space.GALAXY
                else -> error("Unexpected character $it")
            }
        }
    }

    override fun solve1(data: List<List<Space>>): Long {
        val (emptyLines, emptyColumns, pairs) = findPairs(data)

        return pairs.sumOf { manhattanDistanceWithExpansion(it.first, it.second, emptyLines, emptyColumns, 1) } / 2

    }

    private fun findPairs(data: List<List<Space>>): Triple<List<Int>, List<Int>, List<Pair<Point, Point>>> {
        val map = data.toGrid()
        val emptyLines = data.mapIndexedNotNull { y, line -> if (line.all { s -> s == Space.EMPTY }) y else null }
        val emptyColumns =
            data.transpose().mapIndexedNotNull { x, line -> if (line.all { s -> s == Space.EMPTY }) x else null }

        val galaxyPoints = map.filter { it.value == Space.GALAXY }.keys

        val uniquePairs/*: Set<Pair<Point, Point>> */ = galaxyPoints.flatMap { p1 ->
            galaxyPoints.mapNotNull { p2 -> if (p1 == p2) null else Pair(p1, p2) }
        }
        return Triple(emptyLines, emptyColumns, uniquePairs)
    }

    private fun manhattanDistanceWithExpansion(
        first: Point,
        second: Point,
        emptyLines: List<Int>,
        emptyColumns: List<Int>,
        factor: Int
    ): Long {
        return abs(first.y - second.y) + abs(first.x - second.x) +
                emptyLines.count { it in (minOf(first.y, second.y)..<maxOf(first.y, second.y)) } * factor +
                emptyColumns.count { it in (minOf(first.x, second.x)..<maxOf(first.x, second.x)) } * factor

    }

    override fun solve2(data: List<List<Space>>): Long {
        val (emptyLines, emptyColumns, pairs) = findPairs(data)

        return pairs.sumOf { manhattanDistanceWithExpansion(it.first, it.second, emptyLines, emptyColumns,
            1000000 - 1) } / 2

    }
}