package day1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TrebuchetTest {
    private val day1Folder = "/day1"

    @Test
    fun sampleStar1() {
        val trebuchet = Trebuchet("$day1Folder/sample1")
        assertEquals(142, trebuchet.star1())
    }

    @Test
    fun sampleStar2() {
        val trebuchet = Trebuchet("$day1Folder/sample2")
        assertEquals(281, trebuchet.star2())
    }
}
