package day2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CubeConundrumTest {

    @Test
    fun testSampleData() {
        val cubeConundrum = CubeConundrum(null)

        val data = listOf(
            Game(
                1,
                listOf(
                    Round(red = 4, green = 3),
                    Round(red = 1, green = 2, blue = 6),
                    Round(green = 2),
                ),
            ),
            Game(
                2,
                listOf(
                    Round(blue = 1, green = 2),
                    Round(green = 3, blue = 4, red = 1),
                    Round(green = 1, blue = 1),
                ),
            ),
            Game(
                3,
                listOf(
                    Round(green = 8, blue = 6, red = 20),
                    Round(blue = 5, red = 4, green = 13),
                    Round(green = 5, red = 1),
                ),
            ),
            Game(
                4,
                listOf(
                    Round(green = 1, red = 3, blue = 6),
                    Round(green = 3, red = 6),
                    Round(green = 3, blue = 15, red = 14),
                ),
            ),
            Game(
                5,
                listOf(
                    Round(red = 6, blue = 1, green = 3),
                    Round(blue = 2, red = 1, green = 2),
                ),
            ),
        )
        assertEquals(8, cubeConundrum.solve1(data))
    }

    @Test
    fun testSampleParsingData() {
        val cubeConundrum = CubeConundrum("/day2/sample1")

        assertEquals(8, cubeConundrum.star1())
        assertEquals(2286, cubeConundrum.star2())
    }

    @Test
    fun testPower() {
        val cubeConundrum = CubeConundrum(null)
        assertEquals(48, cubeConundrum.cubePower(Round(4, 2, 6)))
        assertEquals(12, cubeConundrum.cubePower(Round(1, 3, 4)))
    }

}
