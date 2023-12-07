package day7

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CamelCardsTest {

    private val camelCards = CamelCards("/day7/sample1")

    @Test
    fun testSampleStar1() {
        assertEquals(6440, camelCards.star1())
    }

    @Test
    fun testSampleStar2() {
        assertEquals(5905, camelCards.star2())
    }

    @Test
    fun testJokersWild() {
        assertEquals(Strength.FIVE_KIND, camelCards.strengthWithJoker(List(5) { Card.JACK }))
    }

    @Test
    fun testJokersWeak() {
        assertEquals(
            1, camelCards.compareCardsWithJoker(
                listOf(Card.JACK, Card.KING, Card.KING, Card.KING, Card.TWO),
                listOf(Card.TWO, Card.TWO, Card.TWO, Card.TWO, Card.THREE)
            )
        )
    }

    @Test
    fun testEdgeCases() {
        val sample2 = CamelCards("/day7/sample2")
        assertEquals(6839, sample2.star2())
    }
}