package day23

import util.Facing
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

enum class HikingTrail {
    PATH,
    FOREST,
    SLOPE_UP,
    SLOPE_RIGHT,
    SLOPE_DOWN,
    SLOPE_LEFT
}

class ALongWalk(fileName: String?) : Solution<List<HikingTrail>, Long>(fileName) {
    override fun parse(line: String): List<HikingTrail>? {
        return line.map {
            when (it) {
                '.' -> HikingTrail.PATH
                '#' -> HikingTrail.FOREST
                '^' -> HikingTrail.SLOPE_UP
                '>' -> HikingTrail.SLOPE_RIGHT
                'v' -> HikingTrail.SLOPE_DOWN
                '<' -> HikingTrail.SLOPE_LEFT
                else -> error("Unexpected character parsing input $it")
            }
        }
    }

    override fun solve1(data: List<List<HikingTrail>>): Long {
        return 0L
        val map = data.toGrid()
        val maxY = map.maxOf { it.key.y }

        val start = map.entries.find { it.key.y == 0L && it.value == HikingTrail.PATH }
        val end = map.entries.find { it.key.y == maxY && it.value == HikingTrail.PATH }!!

        tailrec fun scenicRoutes(paths: Set<List<Point>>, acc: Set<List<Point>>): Set<List<Point>> {
            return when {
                paths.isEmpty() -> acc
                end.key in paths.first() -> scenicRoutes(paths.drop(1).toSet(), acc + setOf(paths.first()))
                else -> {
                    val first = paths.first()
                    val currentPoint = first.first()
                    val neighbours: Set<Point> = when (map[currentPoint]) {
                        HikingTrail.PATH -> {
                            Facing.entries.map {
                                it to currentPoint + it.vector
                            }
                                .filter { (d, p) ->
                                    when (map[p]) {
                                        HikingTrail.PATH -> true
                                        HikingTrail.FOREST -> false
                                        HikingTrail.SLOPE_UP -> d != Facing.DOWN
                                        HikingTrail.SLOPE_RIGHT -> d != Facing.LEFT
                                        HikingTrail.SLOPE_DOWN -> d != Facing.UP
                                        HikingTrail.SLOPE_LEFT -> d != Facing.RIGHT
                                        null -> false
                                    }
                                }
                                .map { it.second }
                                .toSet()
                        }

                        HikingTrail.SLOPE_UP -> setOf(currentPoint + Facing.UP.vector)
                        HikingTrail.SLOPE_RIGHT -> setOf(currentPoint + Facing.RIGHT.vector)
                        HikingTrail.SLOPE_DOWN -> setOf(currentPoint + Facing.DOWN.vector)
                        HikingTrail.SLOPE_LEFT -> setOf(currentPoint + Facing.LEFT.vector)
                        else -> error("Impossible position")
                    }
                    val newPaths: Set<List<Point>> = neighbours
                        .filterNot { it in first }
                        .map { listOf(it) + first }.toSet()

                    if (newPaths.isEmpty()) error("Empty paths, no progress") else
                        scenicRoutes(paths.drop(1).toSet() + newPaths, acc)
                }
            }
        }

        return scenicRoutes(setOf(listOf(start!!.key)), emptySet()).maxOf { it.size }.toLong() - 1
    }

    override fun solve2(data: List<List<HikingTrail>>): Long {
        val map = data.toGrid()
        val maxY = map.maxOf { it.key.y }

        val start = map.entries.find { it.key.y == 0L && it.value == HikingTrail.PATH }
        val end = map.entries.find { it.key.y == maxY && it.value == HikingTrail.PATH }!!

        tailrec fun scenicRoutes(paths: Set<List<Point>>, acc: Set<List<Point>>): Set<List<Point>> {
            if (paths.size % 100 == 0 || paths.size < 100) {
                println("To go: ${paths.size}, acc: ${acc.size}")
            }

            return when {
                paths.isEmpty() -> acc
                end.key in paths.first() -> scenicRoutes(paths.drop(1).toSet(), acc + setOf(paths.first()))
                else -> {
                    val first = paths.first()
                    val currentPoint = first.first()
                    val neighbours: Set<Point> =
                        currentPoint.getNeighbours(true)
                            .filter { it in map && map[it] != HikingTrail.FOREST }
                            .toSet()
                    val newPaths: Set<List<Point>> = neighbours
                        .filterNot { it in first }
                        .map { listOf(it) + first }
                        .filter { it != first }
                        .toSet()
                    scenicRoutes(paths.drop(1).toSet() + newPaths, acc)
                }
            }
        }

        return scenicRoutes(setOf(listOf(start!!.key)), emptySet()).maxOf { it.size }.toLong() - 1
    }
}