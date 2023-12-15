package day15

import util.Solution

class LensLibrary(fileName: String?) : Solution<List<String>, Long>(fileName) {
    override fun parse(line: String): List<String> {
        return line.split(",")
    }

    override fun solve1(data: List<List<String>>): Long {
        return data.first().sumOf { hash(it).toLong() }
    }

    override fun solve2(data: List<List<String>>): Long {

        tailrec fun placeLenses(
            instructions: List<String>,
            boxes: List<List<Pair<String, Long>>>
        ): List<List<Pair<String, Long>>> {
            return if (instructions.isEmpty()) boxes else {
                val ins = instructions.first()
                val label = ins.takeWhile { it != '-' && it != '=' }
                val box = hash(label)
                val op = ins.drop(label.length).first()
                val newBoxes = boxes.mapIndexed { idx, v ->
                    if (idx == box) {
                        if (op == '=') {
                            val focalLength = ins.drop(label.length + 1).toLong()
                            if (v.none { it.first == label }) v + Pair(
                                label,
                                focalLength
                            ) else {
                                v.map {
                                    if (it.first == label) Pair(label, focalLength)
                                    else it
                                }
                            }
                        } else v.filterNot { it.first == label }
                    } else v
                }
                placeLenses(instructions.drop(1), newBoxes)
            }
        }

        val placedLenses = placeLenses(data.first(), List(256) { emptyList() })


        return placedLenses.foldIndexed(0L)
        { i, acc, lenses ->
            acc + lenses.mapIndexed { p, (_, fl) -> (i + 1) * (p + 1) * fl }.sum()
        }
    }

    fun hash(input: String): Int {
        return input.fold(0) { acc, c ->
            ((acc + c.code) * 17) % 256
        }
    }
}