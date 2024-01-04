package day12

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HotSpringsTest {
    @Test
    fun sample1star1() {
        val sample1 = HotSprings("/day12/sample1")

        assertEquals(21, sample1.star1())
    }

    @Test
    fun testStates() {
        val subject = HotSprings(null)
        val state = State(subject.parseLine("?###????????"), listOf(3, 2, 1))
        val computeSolutions = subject.computeSolutions(listOf(state), emptySet())
        assertEquals(10, computeSolutions.size)

    }


}
