package day11

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CosmicExpansionTest {
    @Test
    fun sample1star1() {
        val sample1 = CosmicExpansion("/day11/sample1")
        assertEquals(374, sample1.star1())
    }
}