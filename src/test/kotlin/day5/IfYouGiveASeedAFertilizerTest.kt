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
    fun testRangeSplits() {
        val entry: Pair<Long, Long> = 100L to 104L
        val source4 = MapLine(104L to 103, 1)
        val fullyBefore = splitRanges(entry, source4.sourceRange)
        assertEquals(entry, fullyBefore.first)
        assertNull(fullyBefore.second)
        assertNull(fullyBefore.third)

        val source3 = MapLine(99L to 100, 1)
        val fullyAfter = splitRanges(entry, source3.sourceRange)
        assertEquals(entry, fullyAfter.third)
        assertNull(fullyAfter.second)
        assertNull(fullyAfter.first)

        val source2 = MapLine(99L to 106, 2)
        val fullyIn = splitRanges(entry, source2.sourceRange)
        assertNull(fullyIn.first)
        assertEquals(100L to 104L, fullyIn.second)
        assertNull(fullyIn.third)

        val source1 = MapLine(101L to 106, -20)
        val overlapsStart = splitRanges(entry, source1.sourceRange)
        assertEquals(100L to 101L, overlapsStart.first)
        assertEquals(101L to 104L, overlapsStart.second)
        assertNull(overlapsStart.third)

        val source = MapLine(99L to 101L, 10)
        val overlapsEnd = splitRanges(entry, source.sourceRange)
        assertNull(overlapsEnd.first)
        assertEquals(100L to 101L, overlapsEnd.second)
        assertEquals(101L to 104L, overlapsEnd.third)
    }
}
