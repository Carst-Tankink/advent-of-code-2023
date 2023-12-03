package day3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GearRatiosTest {
    private val gearRatios = GearRatios("/day3/sample")

    @Test
    fun testSampleStar1() {
        assertEquals(4361, gearRatios.star1())
    }

    @Test
    fun testSampleStar2() {
        assertEquals(467835, gearRatios.star2())
    }
}
