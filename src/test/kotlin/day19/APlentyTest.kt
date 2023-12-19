package day19

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class APlentyTest {
    @Test
    fun sample1star1() {
        val sample1 = APlenty("/day19/sample1")
        assertEquals(19114, sample1.star1())
    }
}