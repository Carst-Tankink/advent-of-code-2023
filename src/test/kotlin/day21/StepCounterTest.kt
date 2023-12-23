package day21

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StepCounterTest {

    @Test
    fun sample1_6_steps() {
        val sample1 = StepCounter("/day21/sample1")

        assertEquals(16, sample1.reachable(6).size)
    }
}
