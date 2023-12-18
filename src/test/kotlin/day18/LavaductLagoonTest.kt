package day18

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LavaductLagoonTest {
    val sample1 = LavaductLagoon("/day18/sample1")

    @Test
    fun sample1star1() {
        assertEquals(62, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(952408144115, sample1.star2())
    }
}