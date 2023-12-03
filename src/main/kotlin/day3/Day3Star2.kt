package day3

/**
 * --- Part Two ---
 *
 * The engineer finds the missing part and installs it in the engine! As the engine springs to life, you jump in the closest gondola, finally ready to ascend to the water source.
 *
 * You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately, the gondola has a phone labeled "help", so you pick it up and the engineer answers.
 *
 * Before you can explain the situation, she suggests that you look out the window. There stands the engineer, holding a phone in one hand and waving with the other. You're going so slowly that you haven't even left the station. You exit the gondola.
 *
 * The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of multiplying those two numbers together.
 *
 * This time, you need to find the gear ratio of every gear and add them all up so that the engineer can figure out which gear needs to be replaced.
 *
 * Consider the same engine schematic again:
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
 * In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one part number.) Adding up all of the gear ratios produces 467835.
 *
 * What is the sum of all of the gear ratios in your engine schematic?
 *
 * @see Day3Star1
 */
class Day3Star2 {

    private val partNumberRegex = """(\d+)""".toRegex()

    fun solve() {
        val inputFile = this.javaClass.getResource("/day3/input.txt")?.readText()
        val gears = findGears(inputFile!!.lines())
        val gearRatioSum = gears.values.sum()

        println("[Day3Star2] Gear ratio sum: $gearRatioSum")
    }

    private fun findGears(schematic: List<String>): Map<Pair<Int, Int>, Int> {
        val possibleGearsMap = mutableMapOf<Pair<Int, Int>, List<Int>>()

        schematic.forEachIndexed { schematicLineIndex, schematicLine ->
            partNumberRegex.findAll(schematicLine).forEach {
                val partNumber = it.groups[1]!!.value.toInt()
                val stars = findAdjacentStars(schematic, schematicLineIndex, it.groups[1]!!.range)
                stars.forEach { starLocation ->
                    possibleGearsMap.computeIfAbsent(starLocation) { mutableListOf() }.addLast(partNumber)
                }
            }
        }

        return possibleGearsMap.filter { it.value.size == 2 }
            .mapValues { entry -> entry.value.reduce { a, b -> a * b } };
    }

    private fun findAdjacentStars(
        schematic: List<String>, partNumberLineIndex: Int, partNumberRange: IntRange
    ): List<Pair<Int, Int>> {
        val middleRow = schematic[partNumberLineIndex]
        val topRow: String? = if (partNumberLineIndex > 0) schematic[partNumberLineIndex - 1] else null
        val bottomRow: String? =
            if ((partNumberLineIndex + 1) < schematic.size) schematic[partNumberLineIndex + 1] else null

        val rangeToCheckStart = if (partNumberRange.first > 0) partNumberRange.first - 1 else partNumberRange.first
        val rangeToCheckEnd =
            if ((partNumberRange.last + 1) < middleRow.length) partNumberRange.last + 1 else partNumberRange.last

        val gears = mutableListOf<Pair<Int, Int>>();

        IntRange(rangeToCheckStart, rangeToCheckEnd).forEach { i ->
            if (middleRow[i] == '*') {
                gears.add(Pair(partNumberLineIndex, i))
            }

            if (topRow != null && topRow[i] == '*') {
                gears.add(Pair(partNumberLineIndex - 1, i))
            }

            if (bottomRow != null && bottomRow[i] == '*') {
                gears.add(Pair(partNumberLineIndex + 1, i))
            }
        }

        return gears
    }

}