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
        //?###????????
        val state = State(
            listOf(
                SpringCondition.UNKNOWN,
                SpringCondition.DAMAGED,
                SpringCondition.DAMAGED,
                SpringCondition.DAMAGED,
                SpringCondition.UNKNOWN,
                SpringCondition.UNKNOWN,
                SpringCondition.UNKNOWN,
                SpringCondition.UNKNOWN,
                SpringCondition.UNKNOWN,
                SpringCondition.UNKNOWN,
                SpringCondition.UNKNOWN,
                SpringCondition.UNKNOWN,
            ),
            listOf(3, 2, 1),
            emptyList()
        )
        val computeSolutions = subject.computeSolutions(state)
        assertEquals(10, computeSolutions.size)

    }
}
