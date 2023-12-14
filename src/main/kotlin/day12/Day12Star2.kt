package day12

/**
 * --- Part Two ---
 *
 * As you look out at the field of springs, you feel like there are way more springs than the condition records list. When you examine the records, you discover that they were actually folded up this whole time!
 *
 * To unfold the records, on each row, replace the list of spring conditions with five copies of itself (separated by ?) and replace the list of contiguous groups of damaged springs with five copies of itself (separated by ,).
 *
 * So, this row:
 *
 * .# 1
 *
 * Would become:
 *
 * .#?.#?.#?.#?.# 1,1,1,1,1
 *
 * The first line of the above example would become:
 *
 * ???.###????.###????.###????.###????.### 1,1,3,1,1,3,1,1,3,1,1,3,1,1,3
 *
 * In the above example, after unfolding, the number of possible arrangements for some rows is now much larger:
 *
 * ```
 * ???.### 1,1,3 - 1 arrangement
 * .??..??...?##. 1,1,3 - 16384 arrangements
 * ?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement
 * ????.#...#... 4,1,1 - 16 arrangements
 * ????.######..#####. 1,6,5 - 2500 arrangements
 * ?###???????? 3,2,1 - 506250 arrangements
 * ```
 * After unfolding, adding all of the possible arrangement counts together produces 525152.
 *
 * Unfold your condition records; what is the new sum of possible arrangement counts?
 */
class Day12Star2 {

    private val numberRegex = """[0-9]+""".toRegex()

    fun solve() {
        val inputFile = this.javaClass.getResource("/day12/input.txt")?.readText()
        val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

        val sumOfArrangements = inputFile!!.lines().sumOf { row ->
            val columns = row.split(' ')
            val checksum =
                numberRegex.findAll((1..5).joinToString(",") { columns[1] }).map { it.value.toInt() }.toList()

            numberOfValidArrangements((1..5).joinToString("?") { columns[0] }, checksum, cache)
        }

        println("[Day12Star2] Sum of arrangements: $sumOfArrangements")
        check(sumOfArrangements == 18716325559999L)
    }

    private fun numberOfValidArrangements(
        condition: String, checksum: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long>
    ): Long {
        return cache.getOrPut(Pair(condition, checksum)) { findNumberOfValidArrangements(condition, checksum, cache) }
    }

    private fun findNumberOfValidArrangements(
        condition: String, checksum: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long>
    ): Long {
        if (condition.isEmpty()) {
            return if (checksum.isEmpty()) 1 else 0
        }

        if (condition.first() == '.') {
            return numberOfValidArrangements(condition.drop(1), checksum, cache)
        }

        if (condition.first() == '?') {
            val c1 = numberOfValidArrangements(condition.replaceFirst('?', '.'), checksum, cache)
            val c2 = numberOfValidArrangements(condition.replaceFirst('?', '#'), checksum, cache)
            return c1 + c2
        }

        if (condition.first() == '#') {
            if (checksum.isEmpty()) {
                return 0
            }

            val group = condition.takeWhile { it == '?' || it == '#' }
            if (group.length < checksum[0]) {
                return 0
            }

            if (condition.length == checksum[0]) {
                return if (checksum.size == 1) 1 else 0 // if its last check, then we found match
            } else if (condition[checksum[0]] == '#') { // must be followed by '.' or '?'
                return 0
            }

            // drop checksum + 1 (drops '.'/'?' after group)
            return numberOfValidArrangements(condition.drop(checksum[0] + 1), checksum.drop(1), cache)
        }

        return 0
    }

}