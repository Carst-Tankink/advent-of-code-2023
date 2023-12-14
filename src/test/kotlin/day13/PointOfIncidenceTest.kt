package day13

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PointOfIncidenceTest {
    @Test
    fun sample1star1() {
        val sample1 = PointOfIncidence("/day13/sample1")

        assertEquals(405, sample1.star1())
    }
}