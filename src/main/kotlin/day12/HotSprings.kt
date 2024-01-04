package day12

import util.Solution

enum class SpringCondition {
    OPERATIONAL,
    DAMAGED,
    UNKNOWN
}

data class State(
    val configuration: List<SpringCondition>,
    val checksums: List<Int>,
) {
    fun isSolved(): Boolean {
        tailrec fun checkSolution(conditions: List<SpringCondition>, checks: List<Int>): Boolean {
            return when {
                conditions.isEmpty() && checks.isEmpty() -> true
                conditions.any {  it == SpringCondition.DAMAGED} && checks.isEmpty() -> false
                conditions.isEmpty() && checks.isNotEmpty() -> false
                else -> {
                    val firstCondition = conditions.first()

                    if (firstCondition == SpringCondition.OPERATIONAL) {
                        checkSolution(conditions.drop(1), checks)
                    } else {
                        val firstCheck = checks.first()
                        if (conditions.take(firstCheck).count { it == SpringCondition.DAMAGED } == firstCheck) {
                            val afterConditions = conditions.drop(firstCheck)
                            if (afterConditions.isEmpty() || afterConditions.first() == SpringCondition.OPERATIONAL) {
                                checkSolution(afterConditions, checks.drop(1))
                            } else {
                                false
                            }
                        } else {
                            false
                        }
                    }

                }
            }

        }
        return checkSolution(configuration, checksums)
    }
}

class HotSprings(fileName: String?) : Solution<Pair<List<SpringCondition>, List<Int>>, Long>(fileName) {
    override fun parse(line: String): Pair<List<SpringCondition>, List<Int>> {
        val (springConditions, checksums) = line.split(" ")

        return Pair(
            parseLine(springConditions),
            checksums.split(",").map { it.toInt() }
        )
    }

    fun parseLine(springConditions: String) = springConditions.map {
        when (it) {
            '.' -> SpringCondition.OPERATIONAL
            '#' -> SpringCondition.DAMAGED
            '?' -> SpringCondition.UNKNOWN
            else -> error("Unexpected Spring condition $it")
        }
    }

    override fun solve1(data: List<Pair<List<SpringCondition>, List<Int>>>): Long {
        return data.sumOf {
            val solutions = computeSolutions(listOf(State(it.first, it.second)), emptySet())
            solutions.size.toLong()
        }
    }

    tailrec fun computeSolutions(configurations: List<State>, acc: Set<State>): Set<List<SpringCondition>> {
        return if (configurations.isEmpty()) acc.map { it.configuration }.toSet() else {
            val head = configurations.first()
            val tail = configurations.drop(1)

            val configuration = head.configuration
            val isComplete = configuration.none { it == SpringCondition.UNKNOWN }
            val (nextStates: List<State>, solutions: Set<State>) = if (isComplete) Pair(
                emptyList<State>(),
                if (head.isSolved()) setOf(head) else emptySet()
            ) else {
                val idx = configuration.indexOf(SpringCondition.UNKNOWN)
                val replacements: List<State> =
                    setOf(SpringCondition.DAMAGED, SpringCondition.OPERATIONAL).map { newCondition ->
                        configuration.mapIndexed { i, s -> if (i == idx) newCondition else s }
                    }.map {
                        State(it, head.checksums)
                    }
                Pair(replacements, emptySet())
            }

            computeSolutions(nextStates + tail, solutions + acc)
        }
    }


    override fun solve2(data: List<Pair<List<SpringCondition>, List<Int>>>): Long {
        TODO("Not yet implemented")
    }
}