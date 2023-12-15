package day15

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LensLibraryTest {
    val sample1 = LensLibrary("/day15/sample1")
    @Test
    fun testHash() {
        assertEquals(52, sample1.hash("HASH"))
    }


    @Test
    fun sample1Star1() {
        assertEquals(1320, sample1.star1())
    }
}