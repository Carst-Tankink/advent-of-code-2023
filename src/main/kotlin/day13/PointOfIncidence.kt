package day13

import util.Helpers.Companion.transpose
import util.Solution

enum class Terrain {
    ASH, ROCK
}

class PointOfIncidence(fileName: String?) : Solution<List<Terrain>, Int>(fileName) {
    override fun parse(line: String): List<Terrain> {
        return line.map {
            when (it) {
                '.' -> Terrain.ASH
                '#' -> Terrain.ROCK
                else -> error("Unexpected character $it")
            }
        }
    }

    override fun solve1(data: List<List<Terrain>>): Int {
        return groupFields(data).sumOf { mirrorPosition(it) }
    }

    private fun mirrorPosition(terrain: List<List<Terrain>>): Int {
        val horizontalMirror = findMirror(terrain)
        val verticalMirror = if (horizontalMirror == 0) findMirror(terrain.transpose()) else 0

        return verticalMirror + 100 * horizontalMirror
    }

    private fun findMirror(terrain: List<List<Terrain>>) =
        (1..<terrain.size).firstOrNull {
            val before = terrain.take(it).reversed()
            val after = terrain.drop(it)

            before.zip(after).all { lines -> lines.first == lines.second }
        } ?: 0

    private fun groupFields(data: List<List<Terrain>>): List<List<List<Terrain>>> {
        val folded = data.fold(
            Pair(emptyList<List<List<Terrain>>>(), emptyList<List<Terrain>>())
        ) { (acc, temp), l ->
            if (l.isEmpty()) Pair(acc + listOf(temp), emptyList()) else Pair(acc, temp + listOf(l))
        }

        return folded.first + listOf(folded.second)
    }

    override fun solve2(data: List<List<Terrain>>): Int {
        return groupFields(data).sumOf { mirrorPositionWithSmudges(it) }


    }

    private fun mirrorPositionWithSmudges(field: List<List<Terrain>>): Int {
        val horizontalMirror = findMirrorWithSmudge(field)
        val verticalMirror = if (horizontalMirror == 0) findMirrorWithSmudge(field.transpose()) else 0

        return verticalMirror + 100 * horizontalMirror
    }

    private fun findMirrorWithSmudge(terrain: List<List<Terrain>>) =
        (1..<terrain.size).firstOrNull {
            val before = terrain.take(it).reversed()
            val after = terrain.drop(it)

            val differences = before.zip(after)
                .map { (l1, l2) -> l1.zip(l2).count { (t1, t2) -> t1 != t2 } }

            differences.sum() == 1
        } ?: 0
}