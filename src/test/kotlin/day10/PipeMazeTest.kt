package day10

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PipeMazeTest {
    @Test
    fun sample1() {
        val sample1 = PipeMaze("/day10/sample1")
        assertEquals(4, sample1.star1())
    }

    @Test
    fun sample2() {
        val sample2 = PipeMaze("/day10/sample2")
        assertEquals(8, sample2.star1())
    }


    @Test
    fun sample3star2() {
        val sample3 = PipeMaze("/day10/sample3")
        assertEquals(8, sample3.star2())
    }

    @Test
    fun sample4star2() {
        val sample4 = PipeMaze("/day10/sample4")
        assertEquals(10, sample4.star2())
    }

    @Test
    fun sample5star2() {
        val sample5 = PipeMaze("/day10/sample5")
        assertEquals(4, sample5.star2())    }
}