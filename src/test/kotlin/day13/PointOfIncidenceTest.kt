package day13

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PointOfIncidenceTest {
    val sample1 = PointOfIncidence("/day13/sample1")

    @Test
    fun sample1star1() {
        assertEquals(405, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(400, sample1.star2())
    }
}