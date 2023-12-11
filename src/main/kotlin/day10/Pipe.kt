package day10
class Pipes(private val pipes: List<String>) {
    private val closedLoop = Array(pipes.size) { Array(pipes[0].length) { '.' } }
    fun walkTheLoop(): Int {
        val start = findStartPoint()
        var direction = 0 to 1
        var location = start + direction
        var steps = 1
        while (location != start) {
            steps++
            direction = goThroughPipe(location, direction)
            location += direction
        }
        return steps / 2
    }

    fun closeTheLoop(): Int {
        val start = findStartPoint()
        var direction = 0 to 1
        var location = start + direction
        var steps = 1
        while (location != start) {
            steps++
            direction = goThroughPipe(location, direction)
            closedLoop[location.first][location.second] = pipes[location.first][location.second]
//            paintMap()
            location += direction
        }
        closedLoop[start.first][start.second] = 'L'
//        flood(closedLoopMap)
        return paintMap()
    }

    private fun paintMap(): Int {
        var count = 0
        val newMap = closedLoop.map {
            var inside = false
            var lastRelevantChar = '.'
            it.map {
                when (it) {
                    '.' -> {
                        lastRelevantChar = '.'
                        if (inside) {
                            count++
                            'I'
                        } else 'O'
                    }
                    '|' -> {
                        inside = !inside
                        it
                    }
                    '-' -> it
                    else -> {
                        if (lastRelevantChar == '.') {
                            lastRelevantChar = it
                            inside = !inside
                        } else {
                            if (
                                lastRelevantChar == 'J' && it == 'L' ||
                                lastRelevantChar == 'L' && it == 'J' ||
                                lastRelevantChar == '7' && it == 'F' ||
                                lastRelevantChar == 'F' && it == '7'
                            ) {
                                inside = !inside
                            }
                            lastRelevantChar = '.'
                        }
                        it
                    }

                }
            }
        }
        val closedLoopMap = enhanceMap(newMap)
        return count
    }

//    private fun flood(closedLoopMap: List<String>): List<String> {
//        val closedLoopArray = closedLoopMap.map { it.map { it }.toMutableList() }.toMutableList()
//        recursiveFlood(closedLoopArray)
//
//    }
//
//    private fun recursiveFlood(closedLoopArray: MutableList<MutableList<Char>>, location: Pair<Int, Int> = 0 to 0) {
//        closedLoopArray
//    }

    private fun doNothing() {}
    private fun enhanceMap(newMap: List<List<Any>>): List<String> {
        return newMap.map { line ->
            var top = ""
            var center = ""
            var bottom = ""
            line.forEach { char ->
                when (char) {
                    '|' -> {
                        top += " █ "
                        center += " █ "
                        bottom += " █ "
                    }
                    '-' -> {
                        top += "   "
                        center += "███"
                        bottom += "   "
                    }
                    'L' -> {
                        top += " █ "
                        center += " ██"
                        bottom += "   "
                    }
                    'J' -> {
                        top += " █ "
                        center += "██ "
                        bottom += "   "
                    }
                    '7' -> {
                        top += "   "
                        center += "██ "
                        bottom += " █ "
                    }
                    'F' -> {
                        top += "   "
                        center += " ██"
                        bottom += " █ "
                    }
                    else -> {
                        top += "   "
                        center += " $char "
                        bottom += "   "
                    }
                }
            }
            println(top + "\n" + center + "\n" + bottom)
            top + "\n" + center + "\n" + bottom
        }
    }

    private fun goThroughPipe(
        location: Pair<Int, Int>,
        direction: Pair<Int, Int>
    ): Pair<Int, Int> {
        var direction1 = direction
        return when (pipes[location.first][location.second]) {
            '|' -> direction1
            '-' -> direction1
            'L' -> {
                direction1 = if (direction1 == 1 to 0) 0 to 1 else -1 to 0
                direction1
            }
            'J' -> {
                direction1 = if (direction1 == 1 to 0) 0 to -1 else -1 to 0
                direction1
            }
            '7' -> {
                direction1 = if (direction1 == -1 to 0) 0 to -1 else 1 to 0
                direction1
            }
            else -> {
                direction1 = if (direction1 == -1 to 0) 0 to 1 else 1 to 0
                direction1
            }
        }
    }

    private fun findStartPoint(): Pair<Int, Int> {
        val y = pipes.indexOfFirst { it.contains('S') }
        return y to pipes[y].indexOf('S')
    }
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
    this.first + other.first to this.second + other.second