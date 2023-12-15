package day14

import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

enum class RocksOrEmpty {
    EMPTY,
    ROUND,
    CUBE
}

class ParabolicReflectorDish(fileName: String?) : Solution<List<RocksOrEmpty>, Long>(fileName) {
    override fun parse(line: String): List<RocksOrEmpty> {
        return line.map {
            when (it) {
                '.' -> RocksOrEmpty.EMPTY
                'O' -> RocksOrEmpty.ROUND
                '#' -> RocksOrEmpty.CUBE
                else -> error("Unexpected character $it")
            }
        }
    }

    override fun solve1(data: List<List<RocksOrEmpty>>): Long {
        val grid = data.toGrid()
        val roundRockLocations = grid.filterValues { it == RocksOrEmpty.ROUND }.keys
        val cubeLocations = grid.filterValues { it == RocksOrEmpty.CUBE }.keys
        val allNorth = moveRocksUp(roundRockLocations, cubeLocations)
        val maxY = grid.maxOf { it.key.y } + 1
        return allNorth.sumOf { maxY - it.y }
    }

    private fun moveRocksUp(roundRockLocations: Set<Point>, cubes: Set<Point>): Set<Point> {
        tailrec fun rec(todo: Set<Point>, finished: Set<Point>): Set<Point> {
            return if (todo.isEmpty()) finished else {
                val next = todo.minBy { it.y }
                val blockingLocations = cubes.filter { it.x == next.x } + finished.filter { it.x == next.x }
                val newY: Long = blockingLocations.map { it.y }.filter { it < next.y }.maxOrNull() ?: -1L
                rec(todo - next, finished + Point(next.x, newY + 1))
            }
        }

        return rec(roundRockLocations, emptySet())
    }

    override fun solve2(data: List<List<RocksOrEmpty>>): Long {
        val grid = data.toGrid()
        val maxX = grid.maxOf { it.key.x }
        val maxY = grid.maxOf { it.key.y } + 1
        val roundRockLocations = grid.filterValues { it == RocksOrEmpty.ROUND }.keys
        val cubeLocations = grid.filterValues { it == RocksOrEmpty.CUBE }.keys

        val finalPosition = findCycles(roundRockLocations, cubeLocations, maxX)
        return finalPosition.sumOf { maxY - it.y }
    }

    private fun findCycles(roundRocks: Set<Point>, cubes: Set<Point>, maxX: Long): Set<Point> {
        fun oneStep(p: Set<Point>): Set<Point> = moveRocks(p, cubes, maxX)
        tailrec fun findNu(tortoise: Set<Point>, hare: Set<Point>): Set<Point> {
            return if (tortoise == hare) hare else {
                val newTortoise = oneStep(tortoise)
                val newHare = oneStep(oneStep(hare))
                findNu(newTortoise, newHare)
            }
        }

        tailrec fun findMu(tortoise: Set<Point>, hare: Set<Point>, mu: Long): Pair<Long, Set<Point>> {
            return if (tortoise == hare) Pair(mu, tortoise) else {
                findMu(oneStep(tortoise), oneStep(hare), mu + 1)
            }
        }

        tailrec fun findLambda(tortoise: Set<Point>, hare: Set<Point>, lambda: Long): Long {
            return if (tortoise == hare) lambda else {
                findLambda(tortoise, oneStep(hare), lambda + 1)
            }
        }

        val nu = findNu(oneStep(roundRocks), oneStep(oneStep(roundRocks)))
        val (mu, tortoise) = findMu(roundRocks, nu, 0)
        val lambda = findLambda(tortoise, oneStep(tortoise), 0)


        println("Found cycle! First crossing is $nu.\nFirst point is after $mu repetitions.\nCycle length is $lambda")

        val actualSteps = 1_000_000_000 % lambda

        tailrec fun repeat(current: Long, rocks: Set<Point>): Set<Point> {
            return if (current == 0L) rocks else {
                repeat(current - 1, oneStep(rocks))
            }
        }

        return repeat(actualSteps + mu, roundRocks)
    }

    private fun moveRocks(rocks: Set<Point>, cubes: Set<Point>, maxX: Long): Set<Point> {
        val allNorth = moveRocksUp(rocks, cubes)
        val westCubes = cubes.rotateRight(maxX)
        val allWest = moveRocksUp(allNorth.rotateRight(maxX), westCubes)
        val southCubes = westCubes.rotateRight(maxX)
        val allSouth = moveRocksUp(allWest.rotateRight(maxX), southCubes)
        return moveRocksUp(allSouth.rotateRight(maxX), southCubes.rotateRight(maxX)).rotateRight(maxX)

    }

    private fun Set<Point>.rotateRight(maxX: Long): Set<Point> = this.map { Point(maxX - it.y, it.x) }.toSet()
}