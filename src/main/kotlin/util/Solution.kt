package util

abstract class Solution<Input, SolutionType>(fileName: String?) {
    val data: List<Input> = if (fileName == null) {
        emptyList()
    } else {
        javaClass
            .getResource(fileName)
            ?.readText()
            ?.lines()
            ?.mapNotNull { parse(it) } ?: emptyList()
    }

    val isSample = fileName?.contains("sample") ?: true
    fun star1(): SolutionType = solve1(data)
    fun star2(): SolutionType = solve2(data)

    abstract fun parse(line: String): Input?
    abstract fun solve1(data: List<Input>): SolutionType
    abstract fun solve2(data: List<Input>): SolutionType
}
