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
            computePruning(listOf(State(it.first, it.second)), 0)
        }
    }

    private tailrec fun computePruning(configurations: List<State>, count: Long): Long {
        return if (configurations.isEmpty()) count else {
            val (conf, clues) = configurations.first()
            val tail = configurations.drop(1)

            when {
                clues.isEmpty() && conf.all { it == SpringCondition.OPERATIONAL || it == SpringCondition.UNKNOWN } -> computePruning(
                    tail,
                    count + 1
                )

                clues.isEmpty() && conf.any { it == SpringCondition.DAMAGED } -> computePruning(tail, count)
                conf.all { it == SpringCondition.OPERATIONAL } && clues.isNotEmpty() -> computePruning(tail, count)
                else -> {
                    val interesting = conf.dropWhile { it == SpringCondition.OPERATIONAL }

                    when (interesting.first()) {
                        SpringCondition.DAMAGED -> {
                            val nextClue = clues.first()
                            val allDamaged = interesting.take(nextClue)
                            val after = interesting.drop(nextClue)
                            val nextUpOperational = after.isEmpty() ||
                                    after.first() in setOf(SpringCondition.OPERATIONAL, SpringCondition.UNKNOWN)
                            val isSolution =
                                allDamaged.count { it == SpringCondition.DAMAGED || it == SpringCondition.UNKNOWN } == nextClue &&
                                        nextUpOperational

                            val nextState = if (isSolution) listOf(State(after.drop(1), clues.drop(1))) else emptyList()

                            computePruning(nextState + tail, count)
                        }

                        SpringCondition.UNKNOWN -> {
                            val remainingConf = interesting.drop(1)
                            val nextConfs = listOf(
                                listOf(SpringCondition.OPERATIONAL) + remainingConf,
                                listOf(SpringCondition.DAMAGED) + remainingConf
                            )

                            computePruning(nextConfs.map { State(it, clues) } + tail, count)
                        }

                        SpringCondition.OPERATIONAL -> {
                            error("Unexpected operation state (should have been dropped")
                        }
                    }
                }
            }
        }
    }


    override fun solve2(data: List<Pair<List<SpringCondition>, List<Int>>>): Long {
        TODO("Not yet implemented")
    }
}