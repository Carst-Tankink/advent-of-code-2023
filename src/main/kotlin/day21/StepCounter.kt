package day21

import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

enum class Garden {
    START,
    PLOT,
    ROCK
}

class StepCounter(fileName: String?) : Solution<List<Garden>, Long>(fileName) {
    override fun parse(line: String): List<Garden> {
        return line.map {
            when (it) {
                'S' -> Garden.START
                '.' -> Garden.PLOT
                '#' -> Garden.ROCK
                else -> error("Unexpected character on map $it")
            }
        }
    }

    override fun solve1(data: List<List<Garden>>): Long {
        val reachable: Set<Point> = reachable(64)
        return reachable.size.toLong()
    }

    fun reachable(steps: Int): Set<Point> {
        val grid = data.toGrid()
        tailrec fun findReachable(positions: Set<Point>, steps: Int): Set<Point> {
            return if (steps == 0) positions else {
                val neighbours = positions.asSequence().flatMap { it.getNeighbours(true) }
                    .filter { it in grid }
                    .filter { grid[it] != Garden.ROCK }
                    .toSet()
                findReachable(neighbours, steps - 1)
            }
        }

        val start = grid.entries.find { it.value == Garden.START }!!.key
        return findReachable(setOf(start), steps)
    }

    override fun solve2(data: List<List<Garden>>): Long {
        TODO("Not yet implemented")
    }
}