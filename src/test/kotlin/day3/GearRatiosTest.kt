package day3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GearRatiosTest {
    @Test
    fun testSampleStar1() {
        val gearRatios = GearRatios("/day3/sample")

        assertEquals(4361, gearRatios.star1())
    }
}
