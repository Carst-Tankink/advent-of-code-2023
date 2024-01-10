package day17

import util.Facing
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution


data class CrucibleState(
    val point: Point,
    val consecutive: Int,
    val direction: Facing,
)

class ClumsyCrucible(fileName: String?) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> {
        return line.map { it.digitToInt() }
    }

    override fun solve1(data: List<List<Int>>): Long {
        val grid = data.toGrid()
        val end = Point(grid.maxOf { it.key.x }, grid.maxOf { it.key.y })
        return findMinimalHeat(data, { p -> p.point == end }) { s ->
            val turns = turnCrucible(s)
            val forward = if (s.consecutive < 3) s.copy(
                point = s.point + s.direction.vector,
                consecutive = s.consecutive + 1
            ) else null
            (turns + forward).filterNotNull<CrucibleState>()
        }
    }

    private fun turnCrucible(s: CrucibleState) = setOf(s.direction.turnRight(), s.direction.turnLeft())
        .map { CrucibleState(s.point + it.vector, 1, it) }

    override fun solve2(data: List<List<Int>>): Long {
        val grid = data.toGrid()
        val end = Point(grid.maxOf { it.key.x }, grid.maxOf { it.key.y })
        return findMinimalHeat(data, { p -> p.point == end && p.consecutive >= 4 }) { s ->
            val turns = if (s.consecutive >= 4 || s.consecutive == 0) turnCrucible(s) else emptyList()
            val straight = if (s.consecutive < 10) listOf(
                CrucibleState(s.point + s.direction.vector, s.consecutive + 1, s.direction)
            ) else emptyList()
            turns + straight
        }
    }

    private fun findMinimalHeat(
        data: List<List<Int>>,
        stopCondition: (CrucibleState) -> Boolean,
        computeNextState: (CrucibleState) -> List<CrucibleState>
    ): Long {
        val grid = data.toGrid()

        val startState = CrucibleState(
            Point(0, 0),
            0,
            Facing.RIGHT
        )

        Point(grid.maxOf { it.key.x }, grid.maxOf { it.key.y })

        val todo: MutableSet<CrucibleState> = mutableSetOf(startState)
        val visited: MutableSet<CrucibleState> = mutableSetOf()
        val distanceMap: MutableMap<CrucibleState, Long> = mutableMapOf()
        distanceMap[startState] = 0L

        while (todo.isNotEmpty()) {
            val first = todo.minBy { distanceMap[it] ?: Long.MAX_VALUE }
            if (stopCondition(first)) return distanceMap[first]!! else {
                todo.remove(first)
                val newStates = computeNextState(first)
                    .filter { it.point in grid }
                    .filter { it !in visited }

                newStates.forEach {
                    val current = distanceMap[it] ?: Long.MAX_VALUE
                    distanceMap[it] = minOf(current, distanceMap[first]!! + grid[it.point]!!)
                }


                todo.addAll(newStates)
                visited.add(first)

            }
        }

        return distanceMap.filterKeys { stopCondition(it) }.minOf { it.value }
    }

}