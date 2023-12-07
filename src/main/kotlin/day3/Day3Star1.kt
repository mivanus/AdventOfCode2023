package day3

/**
 * --- Day 3: Gear Ratios ---
 *
 * You and the Elf eventually reach a gondola lift station; he says the gondola lift will take you up to the water source, but this is as far as he can bring you. You go inside.
 *
 * It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving.
 *
 * "Aaah!"
 *
 * You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. "Sorry, I wasn't expecting anyone! The gondola lift isn't working right now; it'll still be a while before I can fix it." You offer to help.
 *
 * The engineer explains that an engine part seems to be missing from the engine, but nobody can figure out which one. If you can add up all the part numbers in the engine schematic, it should be easy to work out which part is missing.
 *
 * The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)
 *
 * Here is an example engine schematic:
 * ```
 * 467..114..
 * ...*......
 * ..35..633.
 * ......#...
 * 617*......
 * .....+.58.
 * ..592.....
 * ......755.
 * ...$.*....
 * .664.598..
 * ```
 *
 * In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.
 *
 * Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?
 */
class Day3Star1 {

    private val partNumberRegex = """(\d+)""".toRegex()

    fun solve() {
        val inputFile = this.javaClass.getResource("/day3/input.txt")?.readText()
        val partNumbers = findPartNumbers(inputFile!!.lines())
        val partNumbersSum = partNumbers.sum()

        println("[Day3Star1] Part number sum: $partNumbersSum")
        check(partNumbersSum == 535235)
    }

    private fun findPartNumbers(schematic: List<String>): List<Int> {
        return schematic.flatMapIndexed { schematicLineIndex, schematicLine ->
            partNumberRegex.findAll(schematicLine)
                .filter { isPartNumber(schematic, schematicLineIndex, it.groups[1]!!.range) }
                .map { it.groups[1]!!.value.toInt() }.toList()
        }
    }

    private fun isPartNumber(schematic: List<String>, partNumberLineIndex: Int, partNumberRange: IntRange): Boolean {
        val middleRow = schematic[partNumberLineIndex]

        val rangeToCheckStart = if (partNumberRange.first > 0) partNumberRange.first - 1 else partNumberRange.first
        val rangeToCheckEnd =
            if ((partNumberRange.last + 1) < middleRow.length) partNumberRange.last + 1 else partNumberRange.last

        val rangeToCheck = IntRange(rangeToCheckStart, rangeToCheckEnd)

        var isPartNumber = middleRow.substring(rangeToCheck).any { !it.isDigit() && '.' != it }
        if (isPartNumber) {
            return true
        }

        if (partNumberLineIndex > 0) {
            val topRow = schematic[partNumberLineIndex - 1]
            isPartNumber = topRow.substring(rangeToCheck).any { !it.isDigit() && '.' != it }
        }

        if (isPartNumber) {
            return true
        }

        if ((partNumberLineIndex + 1) < schematic.size) {
            val bottomRow = schematic[partNumberLineIndex + 1]
            isPartNumber = bottomRow.substring(rangeToCheck).any { !it.isDigit() && '.' != it }
        }

        return isPartNumber;
    }

}