package day7

import util.Solution

enum class Card {
    ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
}

data class Hand(val cards: List<Card>, val bid: Int)

enum class Strength {
    FIVE_KIND, FOUR_KIND, FULL_HOUSE, THREE_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD
}

class CamelCards(fileName: String?) : Solution<Hand, Long>(fileName) {
    override fun parse(line: String): Hand? {
        val (cards, bid) = line.split(" ")

        return Hand(
            cards.map {
                when (it) {
                    'A' -> Card.ACE
                    'K' -> Card.KING
                    'Q' -> Card.QUEEN
                    'J' -> Card.JACK
                    'T' -> Card.TEN
                    '9' -> Card.NINE
                    '8' -> Card.EIGHT
                    '7' -> Card.SEVEN
                    '6' -> Card.SIX
                    '5' -> Card.FIVE
                    '4' -> Card.FOUR
                    '3' -> Card.THREE
                    '2' -> Card.TWO
                    else -> error("Unexpected card face")
                }

            },
            bid.toInt()
        )
    }

    override fun solve1(data: List<Hand>): Long {
        val sorted = data.sortedWith { h1, h2 ->
            val compareStrength = strength(h2.cards).compareTo(strength(h1.cards))
            if (compareStrength == 0) compareCards(h2.cards, h1.cards) else compareStrength
        }

        return sorted.foldIndexed(0L) { idx, acc, hand -> acc + (idx + 1) * hand.bid }
    }

    override fun solve2(data: List<Hand>): Long {
        val sorted = data.sortedWith { h1, h2 ->
            val compareStrength = strengthWithJoker(h2.cards).compareTo(strengthWithJoker(h1.cards))
            if (compareStrength == 0) compareCardsWithJoker(h1.cards, h2.cards) else compareStrength
        }

        return sorted.foldIndexed(0L) { idx, acc, hand -> acc + (idx + 1) * hand.bid }
    }

    private fun strength(cards: List<Card>): Strength {
        val groups = cards.groupBy { it }.values
        return when {
            groups.size == 1 -> Strength.FIVE_KIND
            groups.size == 2 && groups.groupsOfSize(4) == 1 -> Strength.FOUR_KIND
            groups.size == 2 && groups.groupsOfSize(3) == 1 && groups.groupsOfSize(2) == 1 -> Strength.FULL_HOUSE
            groups.size == 3 && groups.groupsOfSize(3) == 1 && groups.groupsOfSize(1) == 2 -> Strength.THREE_KIND
            groups.size == 3 && groups.groupsOfSize(2) == 2 -> Strength.TWO_PAIR
            groups.size == 4 && groups.groupsOfSize(2) == 1 -> Strength.ONE_PAIR
            groups.size == 5 -> Strength.HIGH_CARD
            else -> error("Unknown strength $groups")
        }
    }

    fun strengthWithJoker(cards: List<Card>): Strength {
        return if (!cards.contains(Card.JACK)) strength(cards) else {
            val numberOfJokers = cards.count { it == Card.JACK }
            if (numberOfJokers == 5) Strength.FIVE_KIND else {
                val highestOther = cards.filter { it != Card.JACK }.groupBy { it }.maxBy { it.value.size }.key
                strength(cards.filter { it != Card.JACK } + List(numberOfJokers) { highestOther })
            }
        }
    }

    private fun Collection<List<Card>>.groupsOfSize(size: Int): Int =
        this.count { it.size == size }

    private fun compareCards(cards: List<Card>, cards1: List<Card>): Int {
        return cards.zip(cards1) { c1, c2 ->
            c1.compareTo(c2)
        }.first { it != 0 }
    }

    fun compareCardsWithJoker(cards: List<Card>, cards1: List<Card>): Int {
        return cards.zip(cards1) { c1, c2 ->
            when {
                (c1 == c2) -> 0
                (c1 == Card.JACK) -> 1
                (c2 == Card.JACK) -> -1
                else -> c1.compareTo(c2)
            }
        }.first { it != 0 }
    }
}