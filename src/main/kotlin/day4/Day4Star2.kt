package day4

import java.util.stream.IntStream

/**
 * --- Part Two ---
 *
 * Just as you're about to report your findings to the Elf, one of you realizes that the rules have actually been printed on the back of every card this whole time.
 *
 * There's no such thing as "points". Instead, scratchcards only cause you to win more scratchcards equal to the number of winning numbers you have.
 *
 * Specifically, you win copies of the scratchcards below the winning card equal to the number of matches. So, if card 10 were to have 5 matching numbers, you would win one copy each of cards 11, 12, 13, 14, and 15.
 *
 * Copies of scratchcards are scored like normal scratchcards and have the same card number as the card they copied. So, if you win a copy of card 10 and it has 5 matching numbers, it would then win a copy of the same cards that the original card 10 won: cards 11, 12, 13, 14, and 15. This process repeats until none of the copies cause you to win any more cards. (Cards will never make you copy a card past the end of the table.)
 *
 * This time, the above example goes differently:
 * ```
 * Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
 * Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
 * Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
 * Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
 * Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
 * Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
 * ```
 * - Card 1 has four matching numbers, so you win one copy each of the next four cards: cards 2, 3, 4, and 5.
 * - Your original card 2 has two matching numbers, so you win one copy each of cards 3 and 4.
 * - Your copy of card 2 also wins one copy each of cards 3 and 4.
 * - Your four instances of card 3 (one original and three copies) have two matching numbers, so you win four copies each of cards 4 and 5.
 * - Your eight instances of card 4 (one original and seven copies) have one matching number, so you win eight copies of card 5.
 * - Your fourteen instances of card 5 (one original and thirteen copies) have no matching numbers and win no more cards.
 * - Your one instance of card 6 (one original) has no matching numbers and wins no more cards.
 * - Once all of the originals and copies have been processed, you end up with 1 instance of card 1, 2 instances of card 2, 4 instances of card 3, 8 instances of card 4, 14 instances of card 5, and 1 instance of card 6. In total, this example pile of scratchcards causes you to ultimately have 30 scratchcards!
 *
 * Process all of the original and copied scratchcards until no more scratchcards are won. Including the original set of scratchcards, how many total scratchcards do you end up with?
 *
 * @see Day4Star1
 */
class Day4Star2 {

    private val numberRegex = """(\d+)""".toRegex()

    fun solve() {
        val inputFile = this.javaClass.getResource("/day4/input.txt")?.readText()

        val numberOfCards = mutableMapOf<Int, Int>()

        inputFile!!.lines().forEachIndexed { index, game ->
            val gameNumber = index + 1

            // add original card
            numberOfCards[gameNumber] = numberOfCards.getOrDefault(gameNumber, 0).plus(1)

            val numberOfMatchingNumbers = getNumberOfMatchingNumbers(game)
            if (numberOfMatchingNumbers > 0) {
                val numberOfCurrentCardOwned = numberOfCards.getOrDefault(gameNumber, 0);

                // add copies of next [numberOfMatchingNumbers] cards
                IntStream.rangeClosed(gameNumber + 1, gameNumber + numberOfMatchingNumbers).forEach {
                    // add numberOfCurrentCardsOwned copies
                    numberOfCards[it] = numberOfCards.getOrDefault(it, 0).plus(numberOfCurrentCardOwned)
                }
            }
        }

        val totalCards = numberOfCards.values.reduce(Int::plus)

        println("[Day4Star2] Total scratchcards: $totalCards")
        check(totalCards == 5489600)
    }

    private fun getNumberOfMatchingNumbers(game: String): Int {
        val gameLists = game.split("|");
        val winningNumbers = numberRegex.findAll(gameLists[0].substringAfter(':')).map { it.value.toInt() }.toList()
        val cardNumbers = numberRegex.findAll(gameLists[1].substringAfter(':')).map { it.value.toInt() }.toList()
        return cardNumbers.count { winningNumbers.contains(it) }
    }

}