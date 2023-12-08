package day8

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HauntedWastelandTest {
    @Test
    fun testSample1() {
        val sample1 = HauntedWasteland("/day8/sample1")
        assertEquals(2, sample1.star1())
    }

    @Test
    fun testSample2() {
        val sample2 = HauntedWasteland("/day8/sample2")
        assertEquals(6, sample2.star1())
    }

//    @Test
//    fun testSample3Star2() {
//        val sample3 = HauntedWasteland("/day8/sample3")
//        assertEquals(6, sample3.star2())
//    }
}