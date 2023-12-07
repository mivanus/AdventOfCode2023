package day7

/**
 * --- Part Two ---
 *
 * To make things a little more interesting, the Elf introduces one additional rule. Now, J cards are jokers - wildcards that can act like whatever card would make the hand the strongest type possible.
 *
 * To balance this, J cards are now the weakest individual cards, weaker even than 2. The other cards stay in the same order: A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J.
 *
 * J cards can pretend to be whatever card is best for the purpose of determining hand type; for example, QJJQ2 is now considered four of a kind. However, for the purpose of breaking ties between two hands of the same type, J is always treated as J, not the card it's pretending to be: JKKK2 is weaker than QQQQ2 because J is weaker than Q.
 *
 * Now, the above example goes very differently:
 * ```
 * 32T3K 765
 * T55J5 684
 * KK677 28
 * KTJJT 220
 * QQQJA 483
 * ```
 * - 32T3K is still the only one pair; it doesn't contain any jokers, so its strength doesn't increase.
 * - KK677 is now the only two pair, making it the second-weakest hand.
 * - T55J5, KTJJT, and QQQJA are now all four of a kind! T55J5 gets rank 3, QQQJA gets rank 4, and KTJJT gets rank 5.
 * With the new joker rule, the total winnings in this example are 5905.
 *
 * Using the new joker rule, find the rank of every hand in your set. What are the new total winnings?
 *
 * @see Day7Star1
 */
class Day7Star2 {

    private val handRegex = """(.{5}) (\d+)""".toRegex()

    private val cardStrength = mapOf(
        '2' to 2,
        '3' to 3,
        '4' to 4,
        '5' to 5,
        '6' to 6,
        '7' to 7,
        '8' to 8,
        '9' to 9,
        'T' to 10,
        'J' to 1,
        'Q' to 12,
        'K' to 13,
        'A' to 14
    )

    fun solve() {
        val inputFile = this.javaClass.getResource("/day7/input.txt")?.readText()

        val winnings = inputFile!!.lines().asSequence().map { handRegex.find(it) }
            .map { Hand(it!!.groupValues[1], it.groupValues[2].toInt()) }.sortedWith(this::handComparator)
            .mapIndexed { i, h -> (i + 1) * h.bid }.sum()

        println("[Day7Star2] Winnings: $winnings")
        check(winnings == 253718286)
    }

    private fun handComparator(o1: Hand?, o2: Hand?): Int {
        if (o1 == null && o2 == null) {
            return 0
        } else if (o1 == null) {
            return -1
        } else if (o2 == null) {
            return 1
        }

        if (o1.hand == o2.hand) {
            return 0
        }

        if (o1.strength < o2.strength) {
            return -1
        } else if (o1.strength > o2.strength) {
            return 1
        }

        return o1.hand.mapIndexed { index, c ->
            cardStrength[o2.hand[index]]?.let { cardStrength[c]!!.compareTo(it) } ?: 1
        }.first { it != 0 }
    }

    private data class Hand(val hand: String, val bid: Int) {
        val strength = strength(hand)

        override fun toString(): String {
            return "$hand $bid $strength"
        }

        private fun strength(hand: String): Int {
            val map = mutableMapOf<Char, Int>()
            hand.forEach {
                map.computeIfAbsent(it) { 0 }
                map[it] = map[it]!! + 1
            }

            val jokers = map.remove('J')
            jokers?.let { numberOfJokers ->
                if (map.isNotEmpty()) {
                    map.maxBy { it.value }.let { map[it.key] = it.value + numberOfJokers }
                }
            }

            return when (map.size) {
                5 -> 1 // High card
                4 -> 2 // One pair
                3 -> if (map.values.max() == 3) 4 else 3 // Three of a kind or Two pairs
                2 -> if (map.values.max() == 4) 6 else 5 // Four of a kind or Full house
                1 -> 7 // Five of a kind
                0 -> 7 // Five jokers
                else -> throw IllegalArgumentException()
            }
        }
    }

}