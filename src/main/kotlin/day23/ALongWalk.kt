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
        return 1
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

        val paths = map.filterValues { it != HikingTrail.FOREST }.keys
        val start = paths.find { it.y == 0L }!!


        tailrec fun stepsToJunctions(
            acc: Set<Pair<Set<Point>, Int>>,
            toExplore: Set<List<Point>>,
        ): Set<Pair<Set<Point>, Int>> {
            return if (toExplore.isEmpty()) acc else {
                val head = toExplore.first()
                val tail = toExplore.drop(1).toSet()

                val currentPoint = head.first()
                val nextPoints = currentPoint
                    .getNeighbours(true)
                    .filter { it in paths }
                    .filterNot { it in head }

                return when (nextPoints.size) {
                    0 -> {
                        val edge = setOf(head.last(), currentPoint) to head.size - 1
                        stepsToJunctions(acc + edge, tail)
                    }

                    1 -> stepsToJunctions(acc, setOf(nextPoints + head) + tail)

                    else -> {
                        val edge = setOf(head.last(), currentPoint) to head.size - 1

                        if (edge !in acc) {
                            val newPaths = nextPoints.map {
                                listOf(it, currentPoint)
                            }.toSet()
                            stepsToJunctions(acc + edge, newPaths + tail)
                        } else {
                            stepsToJunctions(acc, tail)
                        }
                    }
                }
            }
        }

        val junctionMap = stepsToJunctions(emptySet(), setOf(listOf(start)))

        val maxY = map.maxOf { it.key.y }
        val end = paths.find { it.y == maxY }!!

        tailrec fun findPathsThroughJunctions(
            acc: Set<List<Pair<Point, Int>>>,
            todo: Set<List<Pair<Point, Int>>>,
        ): Set<List<Pair<Point, Int>>> {
            return if (todo.isEmpty()) {
                acc
            } else {
                val h = todo.first()
                val t = todo.drop(1).toSet()

                val c = h.first()
                return if (c.first == end) {
                    findPathsThroughJunctions(acc + setOf(h), t)
                } else {
                    val neighbours = junctionMap
                        .filter { c.first in it.first }
                        .map { (pos, p) -> pos.find { it != c.first }!! to p }
                        .filterNot { it.first in h.map { p -> p.first } }
                    val next = neighbours.map { listOf(it) + h }.toSet()
                    findPathsThroughJunctions(acc, next + t)
                }
            }
        }

        val ps = findPathsThroughJunctions(
            emptySet(),
            setOf(listOf(start to 0))
        )
        return ps.maxOf { p -> p.sumOf { it.second.toLong() } }
    }
}