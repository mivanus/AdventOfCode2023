package day1

/**
 * --- Part Two ---
 *
 * Your calculation isn't quite right. It looks like some of the digits are actually spelled out with letters: one, two, three, four, five, six, seven, eight, and nine also count as valid "digits".
 *
 * Equipped with this new information, you now need to find the real first and last digit on each line. For example:
 *```
 * two1nine
 * eightwothree
 * abcone2threexyz
 * xtwone3four
 * 4nineeightseven2
 * zoneight234
 * 7pqrstsixteen
 * ```
 *
 * In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together produces 281.
 *
 * What is the sum of all of the calibration values?
 *
 * @see Day1Star1
 */
class Day1Star2 {

    private val numberMap = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    private val searchRegex = """(?=(1|2|3|4|5|6|7|8|9|one|two|three|four|five|six|seven|eight|nine))""".toRegex()

    fun solve() {
        val inputFile = this.javaClass.getResource("/day1/input.txt")?.readText()
        val calibrationSum = inputFile?.lines()?.sumOf { extractCalibrationValue(it) }

        println("[Day1Star2] Calibration sum: $calibrationSum")
        check(calibrationSum == 55291)
    }

    private fun extractCalibrationValue(calibrationInput: String): Int {
        val normalizedInput = searchRegex.findAll(calibrationInput).map {
            val v = it.groups[1]?.value
            numberMap.getOrDefault(v, v)
        }

        val firstDigit = normalizedInput.first()
        val lastDigit = normalizedInput.last()

        return (firstDigit + "" + lastDigit).toInt()
    }

}