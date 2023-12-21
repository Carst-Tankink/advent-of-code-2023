package day19

import util.Solution
import java.lang.Integer.max
import java.lang.Integer.min


data class Rule(
    val target: String,
    val category: String? = null,
    val operator: Char? = null,
    val value: Int? = null

) {
    fun matches(p: Part): Boolean {

        val categoryValue = when (category) {
            "x" -> p.x
            "m" -> p.m
            "a" -> p.a
            "s" -> p.s
            null -> 0
            else -> error("Unexpected category for rule $category")
        }

        return when (operator) {
            null -> true
            '<' -> categoryValue < value!!
            '>' -> categoryValue > value!!
            else -> error("Unexpected operator $operator")
        }
    }

    fun newCategoryRange(categoryRange: Pair<Int, Int>): Pair<Int, Int> {
        return when (operator) {
            null -> categoryRange
            '<' -> Pair(categoryRange.first, min(value!! - 1, categoryRange.second))
            '>' -> Pair(max(categoryRange.first, value!! + 1), categoryRange.second)
            else -> error("Unexpected operator $operator")
        }
    }
}

sealed interface PlentyInput
data class Workflow(val name: String, val rules: List<Rule>) : PlentyInput
data class Part(val x: Int, val m: Int, val a: Int, val s: Int) : PlentyInput
data object EMPTY : PlentyInput
class APlenty(fileName: String?) : Solution<PlentyInput, Long>(fileName) {
    override fun parse(line: String): PlentyInput {
        return when {
            line.startsWith("{") -> {
                val (x, m, a, s) = line.drop(1).dropLast(1).split(",").map { it.split("=").last().toInt() }
                Part(x, m, a, s)
            }

            line.isEmpty() -> EMPTY
            else -> {
                val name = line.takeWhile { it != '{' }
                val rules = line.drop(name.length + 1).dropLast(1).split(",").map { parseRule(it) }
                return Workflow(name, rules)
            }
        }
    }

    private fun parseRule(r: String): Rule {
        return if (r.contains(":")) {
            val (catOperator, target) = r.split(":")
            val cat = catOperator.takeWhile { it !in setOf('<', '>') }
            val op = catOperator.drop(cat.length).first()
            val value = catOperator.drop(cat.length + 1).toInt()
            Rule(target, cat, op, value)
        } else {
            Rule(target = r)
        }
    }

    override fun solve1(data: List<PlentyInput>): Long {
        val workflows = data.filterIsInstance<Workflow>().associateBy { it.name }
        val parts = data.filterIsInstance<Part>()

        return parts
            .filter { isAccepted(it, workflows) }
            .sumOf { it.x + it.m + it.a + it.s.toLong() }
    }

    private fun isAccepted(p: Part, workflows: Map<String, Workflow>): Boolean {
        tailrec fun applyRules(rules: List<Rule>): String {
            val matchingRule = rules.first { it.matches(p) }
            val target = matchingRule.target
            return if (target in setOf("A", "R")) target else {
                applyRules(workflows[target]!!.rules)
            }
        }
        return applyRules(workflows["in"]!!.rules) == "A"
    }

    override fun solve2(data: List<PlentyInput>): Long {
        val workflows = data.filterIsInstance<Workflow>().associate { it.name to it.rules }
        val pathsToA: List<List<String>> = findPathsToA(workflows).map { it.reversed() }
        return pathsToA.sumOf { combinations(it, workflows) }
    }

    private fun combinations(p: List<String>, workflows: Map<String, List<Rule>>): Long {
        val steps = p.zipWithNext { p1, p2 ->
            workflows[p1]!!.first { it.target == p2 }
        }
        val initialRange = Pair(1, 4000)
        val allowedRanges = mapOf(
            "x" to initialRange,
            "m" to initialRange,
            "a" to initialRange,
            "s" to initialRange
        )

        val finalRanges = steps.fold(allowedRanges) { acc, r ->
            val category = r.category
            if (category == null) acc else {
                (acc - category) + (category to r.newCategoryRange(acc[category]!!))
            }
        }

        return finalRanges.values.fold(1) { acc, r ->
            acc * (r.second - r.first)

        }
    }

    private fun findPathsToA(workflows: Map<String, List<Rule>>): Set<List<String>> {
        tailrec fun rec(queue: List<List<String>>, acc: Set<List<String>>): Set<List<String>> {
            return if (queue.isEmpty()) acc else {
                val prefix = queue.first()
                val rule = prefix.first()
                val (finishedPaths, newPrefixes) = workflows[rule]!!.map {
                    listOf(it.target) + prefix
                }.partition { it.first() == "A" || it.first() == "R" }

                rec(queue.drop(1) + newPrefixes, acc + finishedPaths.filter { it.first() == "A" })
            }
        }

        return rec(listOf(listOf("in")), emptySet())
    }
}