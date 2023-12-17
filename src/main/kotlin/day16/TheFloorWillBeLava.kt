package day16

import util.Facing
import util.Grid
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

enum class Contraption {
    EMPTY,
    NORTH_EAST_MIRROR,
    SOUTH_EAST_MIRROR,
    HORIZONTAL_SPLITTER,
    VERTICAL_SPLITTER
}

data class Beam(val pos: Point, val facing: Facing)

class TheFloorWillBeLava(fileName: String?) : Solution<List<Contraption>, Int>(fileName) {
    override fun parse(line: String): List<Contraption>? {
        return line.map {
            when (it) {
                '.' -> Contraption.EMPTY
                '/' -> Contraption.NORTH_EAST_MIRROR
                '\\' -> Contraption.SOUTH_EAST_MIRROR
                '-' -> Contraption.HORIZONTAL_SPLITTER
                '|' -> Contraption.VERTICAL_SPLITTER
                else -> error("Unexpected character")
            }
        }
    }

    override fun solve1(data: List<List<Contraption>>): Int {
        return energizeTiles(setOf(Beam(Point(0, 0), Facing.RIGHT)), data.toGrid())
    }

    private fun energizeTiles(start: Set<Beam>, grid: Grid<Contraption>): Int {
        fun findNext(atPoint: Contraption?, beam: Beam): Set<Beam> {
            return when (atPoint) {
                null -> emptySet()
                Contraption.EMPTY -> setOf(beam.copy(pos = beam.pos + beam.facing.vector))
                Contraption.NORTH_EAST_MIRROR -> when (beam.facing) {
                    Facing.LEFT -> setOf(Beam(beam.pos + Facing.DOWN.vector, Facing.DOWN))
                    Facing.RIGHT -> setOf(Beam(beam.pos + Facing.UP.vector, Facing.UP))
                    Facing.UP -> setOf(Beam(beam.pos + Facing.RIGHT.vector, Facing.RIGHT))
                    Facing.DOWN -> setOf(Beam(beam.pos + Facing.LEFT.vector, Facing.LEFT))
                }

                Contraption.SOUTH_EAST_MIRROR -> when (beam.facing) {
                    Facing.LEFT -> setOf(Beam(beam.pos + Facing.UP.vector, Facing.UP))
                    Facing.RIGHT -> setOf(Beam(beam.pos + Facing.DOWN.vector, Facing.DOWN))
                    Facing.UP -> setOf(Beam(beam.pos + Facing.LEFT.vector, Facing.LEFT))
                    Facing.DOWN -> setOf(Beam(beam.pos + Facing.RIGHT.vector, Facing.RIGHT))
                }

                Contraption.HORIZONTAL_SPLITTER -> when (beam.facing) {
                    Facing.LEFT, Facing.RIGHT -> setOf(beam.copy(pos = beam.pos + beam.facing.vector))
                    Facing.UP, Facing.DOWN -> setOf(
                        Beam(beam.pos + Facing.LEFT.vector, Facing.LEFT),
                        Beam(beam.pos + Facing.RIGHT.vector, Facing.RIGHT)
                    )
                }

                Contraption.VERTICAL_SPLITTER -> when (beam.facing) {
                    Facing.UP, Facing.DOWN -> setOf(beam.copy(pos = beam.pos + beam.facing.vector))
                    Facing.RIGHT, Facing.LEFT -> setOf(
                        Beam(beam.pos + Facing.UP.vector, Facing.UP),
                        Beam(beam.pos + Facing.DOWN.vector, Facing.DOWN)
                    )
                }
            }
        }

        tailrec fun energize(energized: Set<Beam>, beams: Set<Beam>): Set<Point> {
            return if (beams.isEmpty()) energized.map { it.pos }.toSet() else {
                val newBeams = beams.flatMap {
                    val atPoint = grid[it.pos]
                    findNext(atPoint, it)
                }.filterNot { it in energized || it.pos !in grid }
                    .toSet()

                energize(energized + beams, newBeams)
            }
        }

        return energize(emptySet(), start).size
    }

    override fun solve2(data: List<List<Contraption>>): Int {
        val grid = data.toGrid()
        val maxX = grid.keys.maxOf { it.x }
        val maxY = grid.keys.maxOf { it.y }
        val verticalBeams = (0..maxX).flatMap {
            setOf(
                Beam(Point(it, 0), Facing.DOWN),
                Beam(Point(it, maxY), Facing.UP)
            )
        }.toSet()

        val horizontalBeams = (0..maxY).flatMap {
            setOf(
                Beam(Point(0, it), Facing.RIGHT),
                Beam(Point(maxX, it), Facing.LEFT)
            )
        }

        return (verticalBeams + horizontalBeams).maxOf {
            energizeTiles(setOf(it), grid)
        }
    }


}