package day12

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HotSpringsTest {
    private val sample1 = HotSprings("/day12/sample1")

    @Test
    fun sample1star1() {
        assertEquals(21, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(525152, sample1.star2())
    }
}
