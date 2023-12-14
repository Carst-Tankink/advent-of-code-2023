package day13

import util.Helpers.Companion.transpose
import util.Solution

enum class Terrain {
    ASH, ROCK
}

class PointOfIncidence(fileName: String?) : Solution<List<Terrain>, Long>(fileName) {
    override fun parse(line: String): List<Terrain>? {
        return line.map {
            when (it) {
                '.' -> Terrain.ASH
                '#' -> Terrain.ROCK
                else -> error("Unexpected characted $it")
            }
        }
    }

    override fun solve1(data: List<List<Terrain>>): Long {
        val folded = data.fold(
            Pair(emptyList<List<List<Terrain>>>(), emptyList<List<Terrain>>())
        ) { (acc, temp), l ->
            if (l.isEmpty()) Pair(acc + listOf(temp), emptyList()) else Pair(acc, temp + listOf(l))
        }


        val groups: List<List<List<Terrain>>> = folded.first + listOf(folded.second)
        return groups.sumOf { mirrorPosition(it) }
    }

    private fun mirrorPosition(terrain: List<List<Terrain>>): Long {
        val horizontalMirror = findMirror(terrain)

        val verticalMirror = if (horizontalMirror == 0) findMirror(terrain.transpose()) else 0

        return verticalMirror.toLong() + 100 * horizontalMirror
    }

    private fun findMirror(terrain: List<List<Terrain>>) =
        (1..<terrain.size).firstOrNull {
            val before = terrain.take(it).reversed()
            val after = terrain.drop(it)

            before.zip(after).all { lines -> lines.first == lines.second }
        } ?: 0

    override fun solve2(data: List<List<Terrain>>): Long {
        TODO("Not yet implemented")
    }
}