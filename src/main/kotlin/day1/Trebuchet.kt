package day1

import util.Solution

class Trebuchet(fileName: String) : Solution<String, Int>(fileName) {
    override fun parse(line: String): String = line

    override fun solve2(data: List<String>): Int {
        return data.sumOf { line -> toNumbers(line) }
    }

    override fun solve1(data: List<String>): Int {
        return data
            .map { line -> line.filter { it.isDigit() }.map { it.digitToInt() } }
            .sumOf { it.first() * 10 + it.last() }
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

        val numbersRegex = createRegex(numbers.keys)
        val firstMatch = numbersRegex.find(line)?.value
        val firstNumber = numbers.getNumericValue(firstMatch!!)

        val reverseNumbersRegex = createRegex(numbers.keys.map { it.reversed() })
        val lastMatch = reverseNumbersRegex.find(line.reversed())?.value?.reversed()
        val lastNumber = numbers.getNumericValue(lastMatch!!)

        return firstNumber * 10 + lastNumber
    }

    private fun Map<String, Int>.getNumericValue(firstMatch: String): Int = this[firstMatch] ?: firstMatch.toInt()

    private fun createRegex(words: Iterable<String>): Regex {
        return (words.joinToString("|") + "|\\d").toRegex()
    }
}
