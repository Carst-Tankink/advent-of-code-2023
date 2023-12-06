package day6

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WaitForItTest {
    @Test
    fun testSample1() {
        val legendary = WaitForIt("/day6/sample1")
        assertEquals(288, legendary.star1())
        assertEquals(71503, legendary.star2())
    }
}
