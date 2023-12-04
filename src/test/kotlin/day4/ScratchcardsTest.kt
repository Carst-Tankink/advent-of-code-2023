package day4

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ScratchcardsTest {
    private val scratchcards = Scratchcards("/day4/sample1")

    @Test
    fun sample1Star1() {
        assertEquals(13, scratchcards.star1())
    }

    @Test
    fun sample1Star2() {
        assertEquals(30, scratchcards.star2())
    }
}
