package day9

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MirageMaintenanceTest {
    @Test
    fun testSample1Star1() {
        val sample1 = MirageMaintenance("/day9/sample1")
        assertEquals(114, sample1.star1())
    }
}