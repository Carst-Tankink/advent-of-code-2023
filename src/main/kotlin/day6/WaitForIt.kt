package day6

import util.Solution

class WaitForIt(fileName: String?) : Solution<List<Int>, Int>(fileName) {
    override fun parse(line: String): List<Int> {
        return line.split("""\s+""".toRegex()).drop(1).map { it.toInt() }
    }

    override fun solve1(data: List<List<Int>>): Int {
        return data[0].zip(data[1]) { time, record ->
            waysToBeatRecord(time.toLong(), record.toLong())
        }.fold(1) { acc, i -> acc * i }
    }

    private fun waysToBeatRecord(time: Long, record: Long): Int {
        return (0..time).map { hold -> hold * (time - hold) }.count { it > record }
    }

    override fun solve2(data: List<List<Int>>): Int {
        val time = data[0].joinToString("") { it.toString() }.toLong()
        val record = data[1].joinToString("") { it.toString() }.toLong()
        return waysToBeatRecord(time, record)
    }
}
