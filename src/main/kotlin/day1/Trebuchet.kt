package day1

import util.Solution

class Trebuchet(fileName: String) : Solution<String, Int>(fileName) {
    override fun parse(line: String): String = line

    override fun solve2(data: List<String>): Int {
        val numbers = data
            .map { line -> toNumbers(line) }
        return numbers.sum()
    }

    override fun solve1(data: List<String>): Int {
        return data
            .map { it.filter { x -> x.isDigit() } }
            .map { it.first() + it.last().toString() }
            .sumOf { it.toInt() }
    }

    private fun toNumbers(line: String): Int {
        val numbers = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
        )

        val numbersRegex = numbers.keys.joinToString("|", "(", "|\\d)").toRegex()
        val reverseNumbersRegex = numbers.keys.map { it.reversed() }.joinToString("|", "(", "|\\d)").toRegex()
        val firstMatch = numbersRegex.find(line)?.groupValues?.first()
        val firstNumber = numbers[firstMatch] ?: firstMatch?.toInt()

        val lastMatch = reverseNumbersRegex.find(line.reversed())?.groupValues?.first()?.reversed()
        val lastNumber = numbers[lastMatch] ?: lastMatch?.toInt()
        return firstNumber!! * 10 + lastNumber!!
    }
}
