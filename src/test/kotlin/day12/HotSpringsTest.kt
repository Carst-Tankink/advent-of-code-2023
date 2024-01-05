package day12

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HotSpringsTest {
    @Test
    fun sample1star1() {
        val sample1 = HotSprings("/day12/sample1")

        assertEquals(21, sample1.star1())
    }

}
