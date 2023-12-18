package day17

import util.Facing
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution


data class DijkstraNode(
    val point: Point,
    val consecutive: Int,
    val direction: Facing,
    val heat: Long
)

class ClumsyCrucible(fileName: String?) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> {
        return line.map { it.digitToInt() }
    }

    override fun solve1(data: List<List<Int>>): Long {
        val grid = data.toGrid()
        val finalPoint = Point(grid.keys.maxOf { it.x }, grid.keys.maxOf { it.y })

        val startNode = DijkstraNode(Point(0, 0), 0, Facing.RIGHT, 0)
        val queue: MutableSet<DijkstraNode> = mutableSetOf(startNode)
        val visited: MutableSet<Point> = mutableSetOf()
        while (queue.isNotEmpty() && queue.none { it.point == finalPoint }) {
            if (visited.size % 1_000 == 0) {
                println("Visited size: ${visited.size}")
            }
            val next = queue.minBy { it.heat }
            val neighbours = Facing.entries
                .asSequence()
                .filter { it != next.direction.turnRight().turnRight() }
                .mapNotNull {

                    val point = next.point + it.vector
                    if (point !in grid) null else {
                        DijkstraNode(
                            point,
                            if (it == next.direction) next.consecutive + 1 else 1,
                            it,
                            next.heat + grid[point]!!
                        )
                    }
                }
                .filter { it.consecutive <= 3 }
                .filter { it.point !in visited  }
                .toList()


            queue.remove(next)
            queue.addAll(neighbours)

            visited.add(next.point)
        }


        return queue.first { it.point == finalPoint }.heat
    }

    override fun solve2(data: List<List<Int>>): Long {
        TODO("Not yet implemented")
    }
}