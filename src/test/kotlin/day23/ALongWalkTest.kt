package day23

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ALongWalkTest {
    val sample1 = ALongWalk("/day23/sample1")
    @Test
    fun sample1star1() {
        assertEquals(94, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(154, sample1.star2())
    }
}