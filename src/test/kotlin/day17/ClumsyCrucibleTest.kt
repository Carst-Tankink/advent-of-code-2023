package day17

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ClumsyCrucibleTest {
    val sample1 = ClumsyCrucible("/day17/sample1")

    @Test
    fun sample1star1() {
        assertEquals(102, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(94, sample1.star2())
    }
    @Test
    fun unfortunateStar2() {
        val unfortunate = ClumsyCrucible("/day17/unfortunate")
        assertEquals(71, unfortunate.star2())
    }
}