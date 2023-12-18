package day17

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ClumsyCrucibleTest {
    @Test
    fun sample1star1() {
        val sample1 = ClumsyCrucible("/day17/sample1")
        assertEquals(102, sample1.star1())
    }
}