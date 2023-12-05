package day5

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IfYouGiveASeedAFertilizerTest {
    private val ifYouGiveASeedAFertilizer = IfYouGiveASeedAFertilizer("/day5/sample1/")

    @Test
    fun testSample1() {
        assertEquals(35, ifYouGiveASeedAFertilizer.star1())
        assertEquals(46, ifYouGiveASeedAFertilizer.star2())
    }

    @Test
    fun testLookup() {
        val lines = listOf(
            MapLine(98L to 100, 50 - 98),
            MapLine(50L to 98, 52 - 50),
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

    @Test
    fun testRangeSplits() {
        val entry: Pair<Long, Long> = 100L to 104L
        val fullyBefore = mapSingleLine(entry, MapLine(104L to 103, 1))
        assertEquals(entry, fullyBefore.first)
        assertNull(fullyBefore.second)
        assertNull(fullyBefore.third)

        val fullyAfter = mapSingleLine(entry, MapLine(99L to 100, 1))
        assertEquals(entry, fullyAfter.third)
        assertNull(fullyAfter.second)
        assertNull(fullyAfter.first)

        val fullyIn = mapSingleLine(entry, MapLine(99L to 106, 2))
        assertNull(fullyIn.first)
        assertEquals(102L to 106L, fullyIn.second)
        assertNull(fullyIn.third)

        val overlapsStart = mapSingleLine(entry, MapLine(101L to 106, -20))
        assertEquals(100L to 101L, overlapsStart.first)
        assertEquals(81L to 84L, overlapsStart.second)
        assertNull(overlapsStart.third)

        val overlapsEnd = mapSingleLine(entry, MapLine(99L to 101L, 10))
        assertNull(overlapsEnd.first)
        assertEquals(110L to 111L, overlapsEnd.second)
        assertEquals(101L to 104L, overlapsEnd.third)
    }
}
