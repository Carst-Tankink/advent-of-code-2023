package day19

import util.Solution


data class Rule(
    val target: String,
    val category: String? = null,
    val operator: Char? = null,
    val value: Long? = null

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
            val value = catOperator.drop(cat.length + 1).toLong()
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
        TODO("Not yet implemented")
    }
}