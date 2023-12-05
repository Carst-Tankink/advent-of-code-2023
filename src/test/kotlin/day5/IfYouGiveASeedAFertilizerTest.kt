package day5

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IfYouGiveASeedAFertilizerTest {
    val ifYouGiveASeedAFertilizer = IfYouGiveASeedAFertilizer("/day5/sample1/")

    @Test
    fun testSample1() {
        assertEquals(35, ifYouGiveASeedAFertilizer.star1())
        assertEquals(46, ifYouGiveASeedAFertilizer.star2())
    }

    @Test
    fun testLookup() {
        val lines = listOf(
            MapLine(50, 98, 2),
            MapLine(52, 50, 48),
        )

        assertEquals(0, lines.lookup(0))
        assertEquals(1, lines.lookup(1))
        assertEquals(48, lines.lookup(48))
        assertEquals(49, lines.lookup(49))
        assertEquals(52, lines.lookup(50))
        assertEquals(53, lines.lookup(51))
        assertEquals(99, lines.lookup(97))
        assertEquals(50, lines.lookup(98))
        assertEquals(51, lines.lookup(99))
        assertEquals(100, lines.lookup(100))
    }
}
