package day19

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class APlentyTest {
    val sample1 = APlenty("/day19/sample1")

    @Test
    fun sample1star1() {
        assertEquals(19114, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(167409079868000, sample1.star2())
    }
}