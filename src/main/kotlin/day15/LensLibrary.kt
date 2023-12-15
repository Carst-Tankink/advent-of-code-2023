package day15

import util.Solution

class LensLibrary(fileName: String?) : Solution<List<String>, Long>(fileName) {
    override fun parse(line: String): List<String> {
        return line.split(",")
    }

    override fun solve1(data: List<List<String>>): Long {
        return data.first().sumOf { hash(it) }
    }

    override fun solve2(data: List<List<String>>): Long {
        TODO("Not yet implemented")
    }

    fun hash(input: String): Long {
        return input.fold(0) { acc, c ->
            ((acc + c.code) * 17) % 256
        }
    }
}