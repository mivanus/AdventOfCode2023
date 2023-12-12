package day11

import kotlin.math.abs

/**
 * --- Part Two ---
 *
 * The galaxies are much older (and thus much farther apart) than the researcher initially estimated.
 *
 * Now, instead of the expansion you did before, make each empty row or column one million times larger. That is, each empty row should be replaced with 1000000 empty rows, and each empty column should be replaced with 1000000 empty columns.
 *
 * (In the example above, if each empty row or column were merely 10 times larger, the sum of the shortest paths between every pair of galaxies would be 1030. If each empty row or column were merely 100 times larger, the sum of the shortest paths between every pair of galaxies would be 8410. However, your universe will need to expand far beyond these values.)
 *
 * Starting with the same initial image, expand the universe according to these new rules, then find the length of the shortest path between every pair of galaxies. What is the sum of these lengths?
 *
 * @see Day11Star1
 */
class Day11Star2 {

    private val galaxyAge = 1_000_000

    fun solve() {
        val inputFile = this.javaClass.getResource("/day11/input.txt")?.readText()
        val inputLines = inputFile!!.lines()

        val sumOfDistances = calculateDistanceSum(inputLines)

        println("[Day11Star2] Sum of galaxy distances: $sumOfDistances")
        check(sumOfDistances == 702152204842)
    }

    private fun calculateDistanceSum(image: List<String>): Long {
        val galaxies = findGalaxies(image)
        val expandableRows = expandableRows(image)
        val expandableColumns = expandableColumns(image)

        var sumOfDistances = 0L
        for (n1 in galaxies.indices) {
            val galaxy1 = galaxies[n1]

            for (n2 in galaxies.indices) {
                if (n2 <= n1) {
                    continue
                }

                val galaxy2 = galaxies[n2]

                val distance = abs(galaxy1.first - galaxy2.first) + abs(galaxy1.second - galaxy2.second)

                val numberOfNodesToExpand = expandableColumns.count {
                    if (galaxy1.first > galaxy2.first) galaxy1.first > it && it > galaxy2.first
                    else galaxy1.first < it && it < galaxy2.first
                } + expandableRows.count {
                    if (galaxy1.second > galaxy2.second) galaxy1.second > it && it > galaxy2.second
                    else galaxy1.second < it && it < galaxy2.second
                }

                sumOfDistances += distance + numberOfNodesToExpand * galaxyAge - numberOfNodesToExpand
            }
        }
        return sumOfDistances
    }

    private fun findGalaxies(image: List<String>): List<Pair<Int, Int>> {
        val galaxies = mutableListOf<Pair<Int, Int>>()

        for (x in image[0].indices) {
            for (y in image.indices) {
                if (image[y][x] == '#') {
                    galaxies.add(Pair(x, y))
                }
            }
        }

        return galaxies
    }

    private fun expandableRows(
        image: List<String>
    ): List<Int> {

        val rows = mutableListOf<Int>()

        image.forEachIndexed { i, line ->
            if (line.all { it == '.' }) {
                rows.add(i)
            }
        }

        return rows
    }

    private fun expandableColumns(
        image: List<String>
    ): List<Int> {

        val columns = mutableListOf<Int>()

        for (x in image[0].indices) {
            var isEmptyColumn = true
            for (y in image.indices) {
                if (image[y][x] != '.') {
                    isEmptyColumn = false
                }
            }

            if (isEmptyColumn) {
                columns.add(x)
            }
        }

        return columns
    }


}