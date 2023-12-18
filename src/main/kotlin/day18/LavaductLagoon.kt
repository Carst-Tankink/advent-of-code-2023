package day18

import util.Facing
import util.Helpers.Companion.toDecimal
import util.Point
import util.Solution

data class DigInstruction(
    val facing: Facing,
    val steps: Long,
    val edgeColour: String
)

class LavaductLagoon(fileName: String?) : Solution<DigInstruction, Long>(fileName) {
    override fun parse(line: String): DigInstruction? {
        val (f, s, c) = line.split(" ")
        val facing = when (f) {
            "U" -> Facing.UP
            "D" -> Facing.DOWN
            "L" -> Facing.LEFT
            "R" -> Facing.RIGHT
            else -> error("Unexpected direction $f")
        }

        return DigInstruction(facing, s.toLong(), c)
    }

    private tailrec fun floodFill(filled: Set<Point>, edge: Set<Point>): Set<Point> {
        val newNeighbours = edge
            .flatMap { it.getNeighbours(true) }
            .filter { it !in filled }
            .toSet()

        return if (newNeighbours.isEmpty()) filled else {
            floodFill(filled + newNeighbours, newNeighbours)
        }
    }

    override fun solve1(data: List<DigInstruction>): Long {
        val edge: Set<Point> = getEdge(data)

        val topY = edge.minOf { it.y }
        val topLeft = Point(edge.filter { it.y == topY }.minOf { it.x }, topY)

        val seed = Point(topLeft.x + 1, topLeft.y + 1)

        return floodFill(edge + setOf(seed), setOf(seed)).size.toLong()
    }

    private fun getEdge(
        data: List<DigInstruction>,
    ): Set<Point> {
        val digger = Point(0, 0)
        return data.fold(Pair(setOf(digger), digger)) { (dug, digger), ins ->
            when (ins.facing) {
                Facing.LEFT -> {
                    val newPosition = Point(digger.x - ins.steps, digger.y)
                    val newPoints = (digger.x downTo newPosition.x).map { Point(it, digger.y) }
                    Pair(dug + newPoints, newPosition)
                }

                Facing.RIGHT -> {
                    val newPosition = Point(digger.x + ins.steps, digger.y)
                    val newPoints = (digger.x..newPosition.x).map { Point(it, digger.y) }
                    Pair(dug + newPoints, newPosition)
                }

                Facing.UP -> {
                    val newPosition = Point(digger.x, digger.y - ins.steps)
                    val newPoints = (digger.y downTo newPosition.y).map { Point(digger.x, it) }
                    Pair(dug + newPoints, newPosition)
                }

                Facing.DOWN -> {
                    val newPosition = Point(digger.x, digger.y + ins.steps)
                    val newPoints = (digger.y..newPosition.y).map { Point(digger.x, it) }
                    Pair(dug + newPoints, newPosition)
                }
            }
        }.first
    }

    override fun solve2(data: List<DigInstruction>): Long {
        val decodedInstructions = data.map { decodeInstruction(it) }
        val edge = getEdge(decodedInstructions)

        val topY = edge.minOf { it.y }
        val topLeft = Point(edge.filter { it.y == topY }.minOf { it.x }, topY)

        val seed = Point(topLeft.x + 1, topLeft.y + 1)

        floodFill(edge, setOf(seed))
        TODO()
    }

    private fun decodeInstruction(it: DigInstruction): DigInstruction {
        val hexCode = it.edgeColour.drop(2).dropLast(1)

        val dir = when (hexCode.takeLast(1)) {
            "0" -> Facing.RIGHT
            "1" -> Facing.DOWN
            "2" -> Facing.LEFT
            "3" -> Facing.UP
            else -> error("Unexpected dircode")
        }

        val digits = hexCode.dropLast(1).map {
            if (it.isDigit()) it.digitToInt() else when (it) {
                'a' -> 10
                'b' -> 11
                'c' -> 12
                'd' -> 13
                'e' -> 14
                'f' -> 15
                else -> error("Unexpected digit $it")
            }
        }

        val steps = toDecimal(digits, 16)
        return DigInstruction(dir, steps, hexCode)
    }
}