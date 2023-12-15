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
    val fullConfiguration: List<SpringCondition>
) {
    fun isSolved(): Boolean {
        return configuration.none { it == SpringCondition.UNKNOWN } && checksums.isEmpty()
    }
}

class HotSprings(fileName: String?) : Solution<Pair<List<SpringCondition>, List<Int>>, Long>(fileName) {
    override fun parse(line: String): Pair<List<SpringCondition>, List<Int>> {
        val (springConditions, checksums) = line.split(" ")

        return Pair(
            springConditions.map {
                when (it) {
                    '.' -> SpringCondition.OPERATIONAL
                    '#' -> SpringCondition.DAMAGED
                    '?' -> SpringCondition.UNKNOWN
                    else -> error("Unexpected Spring condition $it")
                }
            },
            checksums.split(",").map { it.toInt() }
        )
    }

    override fun solve1(data: List<Pair<List<SpringCondition>, List<Int>>>): Long {
        return data.sumOf {
            val solutions = computeSolutions(State(it.first, it.second, emptyList()))
            solutions.size.toLong()
        }
    }


    private fun computeNextStates(state: State): Set<State> {
        val configuration = state.configuration
        val checksums = state.checksums
        return if ((checksums.isEmpty() || configuration.isEmpty()) && !state.isSolved()) emptySet() else {
            when (val hd = configuration.first()) {
                SpringCondition.OPERATIONAL -> setOf(
                    State(
                        configuration.drop(1),
                        checksums,
                        state.fullConfiguration + hd
                    )
                )

                SpringCondition.DAMAGED -> {
                    val sequence = configuration.take(checksums.first())
                    val nextConfig = configuration.drop(checksums.first())
                    if (sequence.all { it == SpringCondition.DAMAGED || it == SpringCondition.UNKNOWN } && (nextConfig.isEmpty() || nextConfig.first() == SpringCondition.OPERATIONAL || nextConfig.first() == SpringCondition.UNKNOWN)) {
                        setOf(
                            State(
                                nextConfig.drop(1),
                                checksums.drop(1),
                                state.fullConfiguration + List(checksums.first()) { SpringCondition.DAMAGED } + if (nextConfig.isNotEmpty()) listOf(
                                    SpringCondition.OPERATIONAL
                                ) else emptyList()
                            )
                        )
                    } else {
                        emptySet()
                    }
                }

                SpringCondition.UNKNOWN -> {
                    setOf(
                        State(
                            listOf(SpringCondition.DAMAGED) + configuration.drop(1),
                            checksums,
                            listOf(SpringCondition.DAMAGED) + state.fullConfiguration.drop(1)
                        ),
                        State(
                            listOf(SpringCondition.OPERATIONAL) + configuration.drop(1),
                            checksums,
                            listOf(SpringCondition.OPERATIONAL) + state.fullConfiguration.drop(1)
                        )
                    )
                }
            }
        }
    }

    fun computeSolutions(state: State): Set<List<SpringCondition>> {
        return if (state.isSolved()) setOf(state.fullConfiguration) else {
            val nextStates = computeNextStates(state)
            nextStates.flatMap { computeSolutions(it) }.toSet()
        }
    }


    override fun solve2(data: List<Pair<List<SpringCondition>, List<Int>>>): Long {
        TODO("Not yet implemented")
    }
}