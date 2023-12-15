package day14

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParabolicReflectorDishTest {
    val sample1 = ParabolicReflectorDish("/day14/sample1")

    @Test
    fun sample1star1() {
        assertEquals(136, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(64, sample1.star2())
    }
}