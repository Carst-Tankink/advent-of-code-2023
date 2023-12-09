package day9

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MirageMaintenanceTest {
    private val sample1 = MirageMaintenance("/day9/sample1")

    @Test
    fun sample1Star1() {
        assertEquals(114, sample1.star1())
    }

    @Test
    fun fsample1Star2() {
        assertEquals(2, sample1.star2())
    }
}