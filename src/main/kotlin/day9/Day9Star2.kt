package day9

/**
 * --- Part Two ---
 *
 * Of course, it would be nice to have even more history included in your report. Surely it's safe to just extrapolate backwards as well, right?
 *
 * For each history, repeat the process of finding differences until the sequence of differences is entirely zero. Then, rather than adding a zero to the end and filling in the next values of each previous sequence, you should instead add a zero to the beginning of your sequence of zeroes, then fill in new first values for each previous sequence.
 *
 * In particular, here is what the third example history looks like when extrapolating back in time:
 * ```
 * 5  10  13  16  21  30  45
 *   5   3   3   5   9  15
 *    -2   0   2   4   6
 *       2   2   2   2
 *         0   0   0
 * ```
 * Adding the new values on the left side of each sequence from bottom to top eventually reveals the new left-most history value: 5.
 *
 * Doing this for the remaining example data above results in previous values of -3 for the first history and 0 for the second history. Adding all three new values together produces 2.
 *
 * Analyze your OASIS report again, this time extrapolating the previous value for each history. What is the sum of these extrapolated values?
 *
 * @see Day9Star1
 */
class Day9Star2 {

    private val numberRegex = """(-?\d+)""".toRegex()

    fun solve() {
        val inputFile = this.javaClass.getResource("/day9/input.txt")?.readText()
        val inputFileByLine = inputFile!!.lines()

        val sumOfExtrapolatedValues =
            inputFileByLine.map { numberRegex.findAll(it).map(MatchResult::value).map(String::toLong).toList() }
                .sumOf { extrapolateNextValue(it) }

        println("[Day9Star2] Sum of extrapolated values: $sumOfExtrapolatedValues")
        check(sumOfExtrapolatedValues == 803L)
    }

    private fun extrapolateNextValue(values: List<Long>): Long {
        val extrapolatedNumbers = mutableListOf<Long>()
        extrapolatedNumbers.add(values.first())

        var currentSequence = values
        while (true) {
            val n = mutableListOf<Long>()

            (0..<currentSequence.size - 1).forEach { index ->
                val n1 = currentSequence[index]
                val n2 = currentSequence[index + 1]
                n.add(n2 - n1)
            }

            currentSequence = n
            extrapolatedNumbers.add(n.first())

            if (n.all { it == 0L }) {
                break
            }
        }

        return extrapolatedNumbers.reversed().fold(0L) { acc, l -> l - acc }
    }

}