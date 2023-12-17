package day16

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TheFloorWillBeLavaTest {
    val sample1 = TheFloorWillBeLava("/day16/sample1")

    @Test
    fun sample1Star1() {
        assertEquals(46, sample1.star1())
    }

    @Test
    fun sample1Star2() {
        assertEquals(51, sample1.star2())
    }
}