package day10

import util.Grid
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun connects(d: Direction): Boolean = when (this) {
        NORTH -> d == SOUTH
        EAST -> d == WEST
        SOUTH -> d == NORTH
        WEST -> d == EAST
    }

}

sealed interface Location
data object EMPTY : Location
data object START : Location
enum class PipePiece(val exit1: Direction, val exit2: Direction) : Location {
    VERTICAL(Direction.SOUTH, Direction.NORTH),
    HORIZONTAL(Direction.EAST, Direction.WEST),
    L_BEND(Direction.NORTH, Direction.EAST),
    J_BEND(Direction.NORTH, Direction.WEST),
    SEVEN_BEND(Direction.SOUTH, Direction.WEST),
    F_BEND(Direction.SOUTH, Direction.EAST)
}

class PipeMaze(fileName: String?) : Solution<List<Location>, Int>(fileName) {
    override fun parse(line: String): List<Location> {
        return line.map {
            when (it) {
                '|' -> PipePiece.VERTICAL
                '-' -> PipePiece.HORIZONTAL
                'L' -> PipePiece.L_BEND
                'J' -> PipePiece.J_BEND
                '7' -> PipePiece.SEVEN_BEND
                'F' -> PipePiece.F_BEND
                'S' -> START
                '.' -> EMPTY
                else -> error("Unexpected character $it")
            }
        }
    }

    override fun solve1(data: List<List<Location>>): Int {
        return findLoop(data.toGrid()).first
    }

    private fun findLoop(map: Grid<Location>): Pair<Int, List<Point>> {
        val start = map.entries.find { it.value == START }!!.key

        val firstSteps = Direction.entries.mapNotNull {
            val nextPos = start + it

            val piece = map[nextPos]
            if (piece is PipePiece && (piece.exit1.connects(it) || piece.exit2.connects(it))) {
                nextPos
            } else {
                null
            }
        }

        fun findNext(point: Point, path: List<Point>): Point {
            val pipePiece = map[point]!! as PipePiece
            val firstNeighbour = point + pipePiece.exit1
            return if (firstNeighbour !in path) firstNeighbour else point + pipePiece.exit2
        }

        tailrec fun rec(path: List<Point>, steps: Int): Pair<Int, List<Point>> {
            val nextStepLeft = findNext(path.first(), path)
            val nextStepRight = findNext(path.last(), path)

            return if (nextStepLeft == nextStepRight) Pair(steps + 1, path + nextStepRight) else {
                rec(listOf(nextStepLeft) + path + nextStepRight, steps + 1)
            }
        }

        return rec(listOf(firstSteps.first(), start, firstSteps.last()), 1)
    }

    override fun solve2(data: List<List<Location>>): Int {
        val map = data.toGrid()
        val loop = findLoop(map)

        return (0..map.maxOf { it.key.y }).sumOf {
            findInsidePoints(it, map, loop.second).size
        }
    }

    private fun findInsidePoints(y: Long, map: Map<Point, Location>, loop: List<Point>): Set<Point> {
        val startIndex = loop.indexOfFirst { map[it] == START }

        val startY = loop[startIndex].y
        val startIsVertical = loop[startIndex - 1].y < startY || loop[startIndex + 1].y < startY

        val verticalPieces = setOf(
            PipePiece.VERTICAL,
            PipePiece.L_BEND,
            PipePiece.J_BEND
        ) + if (startIsVertical) setOf(START) else emptySet()

        tailrec fun ray(point: Point, acc: Set<Point>, inside: Boolean): Set<Point> {
            val nextPoint = point + Point(1, 0)
            return when (point) {
                !in map -> acc
                in loop -> {
                    val piece = map[point]
                    ray(nextPoint, acc, if (piece in verticalPieces) !inside else inside)
                }

                else -> {
                    val newAcc = if (inside) acc + point else acc
                    ray(nextPoint, newAcc, inside)
                }
            }
        }

        return ray(Point(0, y), emptySet(), false)
    }

}

private operator fun Point.plus(dir: Direction): Point {
    return when (dir) {
        Direction.NORTH -> this + Point(0, -1)
        Direction.EAST -> this + Point(1, 0)
        Direction.SOUTH -> this + Point(0, 1)
        Direction.WEST -> this + Point(-1, 0)
    }
}
