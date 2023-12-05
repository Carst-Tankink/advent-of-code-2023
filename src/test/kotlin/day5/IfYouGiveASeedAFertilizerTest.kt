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
        val fullyBefore = splitRanges(entry, 104L to 103)
        assertEquals(entry, fullyBefore.first)
        assertNull(fullyBefore.second)
        assertNull(fullyBefore.third)

        val fullyAfter = splitRanges(entry, 99L to 100)
        assertEquals(entry, fullyAfter.third)
        assertNull(fullyAfter.second)
        assertNull(fullyAfter.first)

        val fullyIn = splitRanges(entry, 99L to 106)
        assertNull(fullyIn.first)
        assertEquals(100L to 104L, fullyIn.second)
        assertNull(fullyIn.third)

        val overlapsStart = splitRanges(entry, 101L to 106)
        assertEquals(100L to 101L, overlapsStart.first)
        assertEquals(101L to 104L, overlapsStart.second)
        assertNull(overlapsStart.third)

        val overlapsEnd = splitRanges(entry, 99L to 101L)
        assertNull(overlapsEnd.first)
        assertEquals(100L to 101L, overlapsEnd.second)
        assertEquals(101L to 104L, overlapsEnd.third)
    }
}
