package day8

import util.Solution

enum class Direction { LEFT, RIGHT }
sealed interface Line
data class Instructions(val steps: List<Direction>) : Line
data object EMPTY : Line
data class Node(val current: String, val exits: Map<Direction, String>) : Line
class HauntedWasteland(fileName: String?) : Solution<Line, Long>(fileName) {
    override fun parse(line: String): Line {
        return when {
            line.isEmpty() -> EMPTY
            line.all { it == 'L' || it == 'R' } -> line.map {
                when (it) {
                    'L' -> Direction.LEFT
                    'R' -> Direction.RIGHT
                    else -> error("Unexpected input in directions")
                }
            }.let { Instructions(it) }

            else -> {
                val (current, steps) = line.split(" = ")
                val (left, right) = steps.drop(1).dropLast(1).split(", ")
                Node(current, mapOf(Direction.LEFT to left, Direction.RIGHT to right))
            }
        }
    }

    override fun solve1(data: List<Line>): Long {
        val steps = data.filterIsInstance<Instructions>().first().steps
        val nodes = data.filterIsInstance<Node>().associateBy { it.current }
        return findPathForNode(
            "AAA",
            steps,
            nodes
        ).toLong()
    }

    override fun solve2(data: List<Line>): Long {
        val steps = data.filterIsInstance<Instructions>().first().steps
        val nodes = data.filterIsInstance<Node>().associateBy { it.current }

        val individualLengths = nodes.keys
            .filter { it.endsWith("A") }
            .map { findPathForNode(it, steps, nodes) }

        println("Individual lengths: $individualLengths")

        // TODO: I just plugged the individual lengths into an LCM calculator at this point, because I didn't have an
        //  algorithm implemented to do this in code.
        return -1
    }

    private fun findPathForNode(first: String, steps: List<Direction>, nodes: Map<String, Node>): Int {
        tailrec fun findPath(current: String, pathLength: Int, s: List<Direction>): Int {
            return if (current.endsWith("Z")) pathLength else {
                val direction = s.first()
                val newNode = nodes[current]!!.exits[direction]!!
                findPath(newNode, pathLength + 1, s.drop(1) + direction)
            }
        }

        return findPath(first, 0, steps)
    }
}