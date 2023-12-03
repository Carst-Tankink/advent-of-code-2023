package day2

import util.Solution
import kotlin.math.max

data class Round(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0,
)

data class Game(
    val id: Int,
    val rounds: List<Round>,
)

class CubeConundrum(fileName: String?) : Solution<Game, Int>(fileName) {
    override fun parse(line: String): Game {
        val gameRounds = line.split(": ")
        val id = gameRounds[0].split(" ")[1].toInt()
        val rounds = gameRounds[1].split("; ").map { parseRound(it) }
        return Game(id, rounds)
    }

    private fun parseRound(roundData: String): Round {
        val cubes = roundData.split(", ")
        val colorsToNumber = cubes.map { it.split(" ") }
            .associate { it[1] to it[0].toInt() }
        return Round(
            red = colorsToNumber["red"] ?: 0,
            green = colorsToNumber["green"] ?: 0,
            blue = colorsToNumber["blue"] ?: 0,
        )
    }

    override fun solve1(data: List<Game>): Int {
        return data
            .filter { isPossible(it) }
            .sumOf { it.id }
    }

    private fun isPossible(game: Game): Boolean {
        return game.rounds.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }
    }

    override fun solve2(data: List<Game>): Int {
        val minimalPossible: List<Round> = data.map { game ->
            game.rounds.fold(Round(0, 0, 0)) { acc, round ->
                Round(
                    red = max(acc.red, round.red),
                    green = max(acc.green, round.green),
                    blue = max(acc.blue, round.blue),
                )
            }
        }

        return minimalPossible.sumOf { cubePower(it) }
    }

    fun cubePower(round: Round): Int = round.red * round.green * round.blue
}
